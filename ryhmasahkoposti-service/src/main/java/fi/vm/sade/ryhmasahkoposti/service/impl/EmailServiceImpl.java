package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.mail.internet.MimeMessage;
import javax.persistence.OptimisticLockException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;

import fi.vm.sade.ryhmasahkoposti.api.dto.*;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import fi.vm.sade.ryhmasahkoposti.service.*;
import fi.vm.sade.ryhmasahkoposti.service.dto.EmailQueueHandleDto;
import fi.vm.sade.ryhmasahkoposti.service.dto.EmailRecipientDtoConverter;
import fi.vm.sade.ryhmasahkoposti.service.dto.ReportedRecipientAttachmentSaveDto;
import fi.vm.sade.ryhmasahkoposti.util.TemplateBuilder;

import static fi.vm.sade.ryhmasahkoposti.util.CollectionUtils.addToMappedList;
import static fi.vm.sade.ryhmasahkoposti.util.CollectionUtils.combine;
import static fi.vm.sade.ryhmasahkoposti.util.OptionalHelper.as;

@Service
public class EmailServiceImpl implements EmailService {
    public static final String ADDITIONAL_ATTACHMENT_URI_PARAMETER = "additionalAttachmentUri";
    public static final String ADDITIONAL_ATTACHMENT_URIS_PARAMETER = "additionalAttachmentUris";

    private static final Logger log = LoggerFactory.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailServiceImpl.class);

    private static final int MAX_CACHE_ENTRIES = 10;
    // If we have pool size of 10 it takes approx. 14s to process a 250 recipient / queue
    // concurrently with 10 executors. And we have max 1 min before a new cron check call
    // add to add in new calls. So assume the optimum maximum to start at once to be:
    // 60/14*10 ~= 42 (if we start more, they would be queued and only cause extra queries later)
    @Value("${ryhmasahkoposti.max.sendqueue.tasks.to.add:42}")
    private int maxTasksToStartAtOnce = 42;

    @Autowired
    private GroupEmailReportingService rrService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private ReportedMessageDAO emailDao;

    @Autowired
    private EmailAVChecker emailAVChecker;

    @Autowired
    private EmailRecipientDtoConverter emailRecipientDtoConverter;

    @Autowired
    private EmailSendQueueService emailQueueService;

    @Named("emailExecutor")
    @Autowired
    private ThreadPoolTaskExecutor emailExecutor;

    @Autowired
    private List<EmailAttachmentDownloader> emailDownloaders;

    @Autowired
    private ReportedMessageAttachmentService reportedMessageAttachmentService;

    @Autowired
    private TemplateBuilder templateBuilder;

    @Value("${ryhmasahkoposti.require.virus.check:true}")
    private boolean virusCheckRequired;

    private Map<Long, EmailMessageDTO> messageCache = new LinkedHashMap<Long, EmailMessageDTO>(MAX_CACHE_ENTRIES + 1) {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, EmailMessageDTO> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        };
    };

    @Override
    public Long getCount(String oid) {
        return emailDao.findNumberOfUserMessages(oid);
    }

    /**
     * @param emailData
     * @param emailAddress
     * @return the EML file contents for preview
     * @throws Exception
     */
    public String getEML(EmailData emailData, String emailAddress) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        EmailRecipientMessage emailMessage
                = emailRecipientDtoConverter.convertPreview(emailData, emailAddress,new EmailRecipientMessage());
        loadReferencedAttachments(emailMessage.getRecipient(), new EmailSendQueState().withoutSavingAttachments());
        templateBuilder.applyTemplate(emailMessage);

        MimeMessage message = emailSender.createMail(emailMessage, emailAddress,
                optionalEmailRecipientAttachments(emailMessage.getRecipient()));
        message.writeTo(baos);
        return new String(baos.toByteArray());
    }

    /**
     * Called after an email is registered to be send in async manner and
     * additionally scheduled as a cron job.
     *
     * Triggers handleEmailQueue tasks into emailExecutor (where 10 tasks are
     * run simultaneously and the rest are queued)
     * @see #handleEmailQueue()
     */
    @Override
    public void checkEmailQueues() {
        // Check if there are any stopped processes (no updates in a long time) to transfer them from
        // PROCESSING state back to WAITING_FOR_HANDLER state:
        emailQueueService.checkForStoppedProcesses();

        int numberOfQueues = emailQueueService.getNumberOfUnhandledQueues();
        if (numberOfQueues > 0) {
            // If there are queues, make maximum of maxTasksToStartAtOnce
            // handleEmailQueue tasks, since additional tasks are to be added
            // in the becoming cron job as well (if there are too many, some of them just do nothing
            // but of course we will want to avoid those):
            int numberOfProcessesToStart = Math.min(maxTasksToStartAtOnce, numberOfQueues);
            log.info("Found {} unhandled queues. Starting {} new handleEmailQueue processes.",
                    numberOfQueues, numberOfProcessesToStart);
            for (int i = 0; i < numberOfProcessesToStart; ++i) {
                emailExecutor.submit(new Runnable() {
                    public void run() {
                        handleEmailQueue();
                    }
                });
            }
            log.info("Processes scheduled to emailExecutor.");
        } else {
            log.debug("Found no unhandled messages queues.");
        }
    }

    /**
     * Reserves and handles a single {@link fi.vm.sade.ryhmasahkoposti.model.SendQueue} if unhandled
     * (WAITING_FOR_HANDLER state) EmailSendQueues exists
     *
     * Before each recipient, ensures that the handling of the queue can still be continued and writes a new
     * updated timestamp to the EmailQueue. (If this timestamp is too old the scheduled background task may interpret
     * the queue as stopped (due to an error or power outage, that is) and put it back to WAITING_FOR_HANDLER state)
     *
     * After the queue is handled, calls {@link fi.vm.sade.ryhmasahkoposti.service.EmailSendQueueService#queueHandled(long)}
     * to mark this EmailSendQuee to
     */
    public void handleEmailQueue() {
        long start = System.currentTimeMillis();

        EmailQueueHandleDto queue = reserveQueue();
        if (queue != null) {
            log.info("Handling EmailQueue={}. Sending emails using: {}", queue.getId(), emailSender);
            int sent = 0,
                errors = 0,
                queueSize = queue.getRecipients().size();
            EmailSendQueState state = new EmailSendQueState();

            for (EmailRecipientDTO emailRecipient : queue.getRecipients()) {
                long recipientStart = System.currentTimeMillis();

                if (!emailQueueService.continueQueueHandling(queue)) {
                    long took = System.currentTimeMillis() - start;
                    log.error("Email send queue handler for SendQueue={} stopped." +
                            "Queue size: {}, sent: {}, errors: {}, took: {} ms.",
                            queueSize, sent, errors, took, queue.getId());
                    return;
                }

                if (handleRecipient(emailRecipient, state)) {
                    sent++;
                } else {
                    errors++;
                }

                long singleRecipientTook = System.currentTimeMillis()-recipientStart;
                log.debug("Handled single recipient in {} ms.", singleRecipientTook);
            }
            emailQueueService.queueHandled(queue.getId());
            cleanupEmailSendQueueAfterHandled(state);

            long took = System.currentTimeMillis() - start;
            log.info("Sending emails done. SendQueue={}: size: {}, sent: {}, errors: {}, took: {} ms.",
                    queue.getId(), queueSize, sent, errors, took);
        } else {
            log.info("Checked for open email sending queues and found none.");
        }
    }

    /**
     * Reserves an unhandled (WAITING_FOR_HANDLER state) {@link fi.vm.sade.ryhmasahkoposti.model.SendQueue}
     * for the calling processes assuring that no other process has the same email queue by calling
     * {@link fi.vm.sade.ryhmasahkoposti.service.EmailSendQueueService#reserveNextQueueToHandle()}. Retries the
     * reservation if it leads to an {@link javax.persistence.OptimisticLockException}
     *
     * @return the email queue to handle or null if none (NOTE: same queue may contain recipients for different emails)
     */
    private EmailQueueHandleDto reserveQueue() {
        int maxConcurrentLockFailures = maxTasksToStartAtOnce -1,
                triedTimes = 0;
        EmailQueueHandleDto queue = null;
        while (queue == null && triedTimes++ < maxConcurrentLockFailures) {
            try {
                queue = emailQueueService.reserveNextQueueToHandle();
            } catch(OptimisticLockException e) {
                log.debug("OptimisticLockException while trying to reserveNextQueueToHandle. Failure: {} / {}.",
                        maxConcurrentLockFailures);
            }
        }
        if (triedTimes > 0 && queue == null) {
            log.warn("Exeeced max number of OptimisticLockExceptions while trying to reserveNextQueueToHandle. Gave up.");
        }
        return queue;
    }

    /**
     * Called after the handling of an email send queue.
     *
     * Cleans up the downloaded attachments from external services by collecting the downloaded URIs for
     * each service and calling the cleanup as a batch.
     *
     * @param state of the send queue
     */
    private void cleanupEmailSendQueueAfterHandled(EmailSendQueState state) {
        // We don't want to store beans nor expect them to implement hashCode/equals, but each have an unique class:
        Map<Class<? extends EmailAttachmentDownloader>, EmailAttachmentDownloader> downloaders
                = new HashMap<Class<? extends EmailAttachmentDownloader>, EmailAttachmentDownloader>();
        Map<Class<? extends EmailAttachmentDownloader>, List<String>> urisByDownloaders
                = new HashMap<Class<? extends EmailAttachmentDownloader>, List<String>>();
        for (String uri : state.getDownloadedAttachmentUris()) {
            EmailAttachmentDownloader downloader = downloaderForUri(uri);
            if (addToMappedList(urisByDownloaders, downloader.getClass(), uri)) {
                downloaders.put(downloader.getClass(), downloader);
            }
        }
        // Now for each different downloader, mark their URIs downloaded:
        for (Map.Entry<EmailAttachmentDownloader, List<String>> entry
                : combine(downloaders, urisByDownloaders).entrySet()) {
            entry.getKey().reportDownloaded(entry.getValue());
        }
    }

    /**
     * Processes a single recipient of an email message.
     *
     * No-throw.
     *
     * Downloads the possible recipient related attachments from external services.
     *
     * Does a virus check if required (generally and) by the message (not already virus checked) or
     * if message contains recipient specific attachments. If virus check fails will lead to failure
     * and the message not being send.
     *
     * For email sending, calls {@link fi.vm.sade.ryhmasahkoposti.service.impl.EmailSender#handleMail(fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage, String, com.google.common.base.Optional)}
     *
     * @param emailRecipient the recipient to send email to (NOTE: same queue may contain recipients for different emails)
     * @param emailSendQueState state of the send queue
     * @return true on success, false on failure
     */
    private boolean handleRecipient(EmailRecipientDTO emailRecipient, EmailSendQueState emailSendQueState) {
        long vStart = System.currentTimeMillis();
        log.info("Handling " + emailRecipient + " " + emailRecipient.getRecipientID());
        boolean success = false;
        if (rrService.startSending(emailRecipient)) {
            log.info("Handling really " + emailRecipient + " " + emailRecipient.getRecipientID());

            try {
                // loads remote attachments if available 
                loadReferencedAttachments(emailRecipient, emailSendQueState);
                
                // get recipient specific email message 
                EmailRecipientMessage message = getVirusCheckedRecipientMessage(emailRecipient);
                
                if (virusCheckPassed(message)) {
                    templateBuilder.applyTemplate(message);
                    emailSender.handleMail(message, emailRecipient.getEmail(), optionalEmailRecipientAttachments(emailRecipient));
                } else {
                    throw new Exception("Virus check problem. File infected: " + message.isInfected());
                }
                rrService.recipientHandledSuccess(emailRecipient, ""); // result is left empty in OK situations
                success = true;
            } catch (Exception e) {
                String failureCause = getFailureCause(e);
                log.error("Failure when handling EmailRecipient="+emailRecipient.getRecipientID()+": "+e.getMessage(), e);
                rrService.recipientHandledFailure(emailRecipient, failureCause);
                success = false;
            }
        }
        long took = System.currentTimeMillis() - vStart;
        log.info("Message handling took " + took);
        return success;
    }

    protected Optional<? extends AttachmentContainer> optionalEmailRecipientAttachments(EmailRecipientDTO emailRecipient) {
        return emailRecipient.getAttachments() != null ? Optional.of(emailRecipient)
                : Optional.<AttachmentContainer>absent();
    }

    /**
     * Loads possible external attachments by EmailAttachmentDownloader
     *
     * @see fi.vm.sade.ryhmasahkoposti.service.EmailAttachmentDownloader
     * @param emailRecipient the recipient
     * @param emailSendQueState the email send queue state to report any downloaded attachment URIs
     * @throws Exception if downloading an attachment failed
     */
    private void loadReferencedAttachments(EmailRecipientDTO emailRecipient, EmailSendQueState emailSendQueState)
            throws Exception {
        Optional<List> attachmentReferences = as(
                effectiveReplacementField(emailRecipient, ADDITIONAL_ATTACHMENT_URIS_PARAMETER), List.class);
        if (attachmentReferences.isPresent()) {
            List uris = attachmentReferences.get();
            for (Object uri : uris) {
                downloadAttachment(emailRecipient, emailSendQueState, uri.toString());
            }
        }
        Optional<String> attachmentReference = as(
                effectiveReplacementField(emailRecipient, ADDITIONAL_ATTACHMENT_URI_PARAMETER), String.class);
        if (attachmentReference.isPresent()) {
            String uri = attachmentReference.get();
            downloadAttachment(emailRecipient, emailSendQueState, uri);
        }
    }

    private Optional<Object> effectiveReplacementField(EmailRecipient recipient, String key) {
        if (recipient.getRecipientReplacements() == null || key == null) {
            return Optional.absent();
        }
        for (ReportedRecipientReplacementDTO replacement : recipient.getRecipientReplacements()) {
            if (replacement.getName() != null && key.equals(replacement.getName())) {
                return Optional.fromNullable(replacement.getEffectiveValue());
            }
        }
        return Optional.absent();
    }

    private void downloadAttachment(EmailRecipientDTO er, EmailSendQueState emailSendQueState, String uri)
            throws Exception {
        log.info("Downloading attachment for EmailRecipientDTO={}, URI={}", er.getRecipientID(), uri);
        EmailAttachment attachment = downloaderForUri(uri).download(uri);
        if (attachment != null) {
            if (emailSendQueState.isSaveAttachments()) {
                ReportedRecipientAttachmentSaveDto dto = new ReportedRecipientAttachmentSaveDto();
                dto.setAttachment(attachment.getData());
                dto.setAttachmentName(attachment.getName());
                dto.setContentType(attachment.getContentType());
                dto.setReportedRecipientId(er.getRecipientID());
                reportedMessageAttachmentService.saveReportedRecipientAttachment(dto);
            }
            er.getAttachments().add(attachment);
        } else {
            throw new IllegalArgumentException("Attachment with URI="
                    + uri + " for EmailRecipient="+er.getRecipientID()+" not found.");
        }
        emailSendQueState.addDownloadedAttachmentUri(uri);
        log.debug("Downloaded attachment URI={}", uri);
    }

    protected EmailAttachmentDownloader downloaderForUri(String uri) {
        for (EmailAttachmentDownloader downloader : emailDownloaders) {
            if (downloader.isApplicableForUri(uri)) {
                return downloader;
            }
        }
        throw new IllegalArgumentException("Unknown attachment URI: " + uri);
    }

    private String getFailureCause(Exception e) {
        String failureCause = e.getMessage();
        if (failureCause == null) {
            failureCause = "e.getMessage() == null";
        } else if (failureCause.length() > 250) {
            failureCause = failureCause.substring(0, 250);
        }
        return failureCause;
    }

    private EmailRecipientMessage getVirusCheckedRecipientMessage(EmailRecipientDTO emailRecipient) {
        Long messageId = emailRecipient.getEmailMessageID();
        EmailMessageDTO message = getMessage(messageId);
        
        if (virusCheckRequired(message, emailRecipient)) {
            // check message and recipient attachments 
            doVirusCheck(message, emailRecipient);
        }

        return emailRecipientDtoConverter.convert(emailRecipient, new EmailRecipientMessage(), message);
    }
    
    private EmailMessageDTO getMessage(Long messageId) {
        EmailMessageDTO message = messageCache.get(messageId);
        if (message == null) {
            message = rrService.getMessage(messageId);
            messageCache.put(messageId, message);
        }
        return message;
    }

    private boolean virusCheckRequired(EmailMessageDTO message, EmailRecipientDTO recipient) {
        return virusCheckRequired && (
                !message.isVirusChecked()
                        || (recipient.getAttachments() != null && !recipient.getAttachments().isEmpty())
        );
    }

    private void doVirusCheck(EmailMessageDTO message, EmailRecipientDTO recipient) {
        try {
            emailAVChecker.checkMessage(emailSender, message, Optional.fromNullable(recipient));
        } catch (Exception e) {
            log.error("Virus check failed" + e);
        }
    }
    
    private boolean virusCheckPassed(EmailRecipientMessage message) {
        return !virusCheckRequired || (message.isVirusChecked() && !message.isInfected());
    }

    public void setMaxTasksToStartAtOnce(int maxTasksToStartAtOnce) {
        this.maxTasksToStartAtOnce = maxTasksToStartAtOnce;
    }

    public void setVirusCheckRequired(boolean virusCheckRequired) {
        this.virusCheckRequired = virusCheckRequired;
    }

    public void setTemplateBuilder(TemplateBuilder templateBuilder) {
        this.templateBuilder = templateBuilder;
    }
}
