package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.mail.internet.MimeMessage;
import javax.persistence.OptimisticLockException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;

import fi.vm.sade.ryhmasahkoposti.api.dto.*;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.service.*;
import fi.vm.sade.ryhmasahkoposti.service.dto.EmailQueueHandleDto;
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
    private ReportedAttachmentService liiteService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private ReportedMessageDAO emailDao;

    @Autowired
    private EmailAVChecker emailAVChecker;

    @Autowired
    private EmailSendQueueService emailQueueService;

    @Named("emailExecutor")
    @Autowired
    private ThreadPoolTaskExecutor emailExecutor;

    @Autowired
    private List<EmailAttachmentDownloader> emailDownloaders;

    private TemplateBuilder templateBuilder = new TemplateBuilder();

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

    public String getEML(EmailMessage emailMessage, String emailAddress) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MimeMessage message = emailSender.createMail(emailMessage, emailAddress,
                Optional.<AttachmentContainer>absent());
        message.writeTo(baos);
        return new String(baos.toByteArray());
    }

    @Override
    public void checkEmailQueues() {
        emailQueueService.checkForStoppedProcesses();
        int numberOfQueues = emailQueueService.getNumberOfUnhandledQueues();
        if (numberOfQueues > 0) {
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

    private boolean handleRecipient(EmailRecipientDTO er, EmailSendQueState emailSendQueState) {
        long vStart = System.currentTimeMillis();
        log.info("Handling " + er + " " + er.getRecipientID());
        boolean success = false;
        if (rrService.startSending(er)) {
            log.info("Handling really " + er + " " + er.getRecipientID());
            Long messageId = er.getEmailMessageID();
            try {
                EmailMessageDTO message = getMessage(messageId);
                loadReferencedAttachments(er, emailSendQueState);

                if (virusCheckRequired(message, er)) {
                    doVirusCheck(message, er);
                }

                if (virusCheckPassed(message)) {
                    if (StringUtils.equalsIgnoreCase(message.getType(), ReportedMessage.TYPE_TEMPLATE)) {
                        rebuildMessage(er, messageId, message);
                    }
                    emailSender.handleMail(message, er.getEmail(), Optional.of(er));
                } else {
                    throw new Exception("Virus check problem. File infected: " + message.isInfected());
                }
                rrService.recipientHandledSuccess(er, ""); // result is left empty in OK situations
                success = true;
            } catch (Exception e) {
                String failureCause = getFailureCause(e);
                rrService.recipientHandledFailure(er, failureCause);
                success = false;
            }
        }
        long took = System.currentTimeMillis() - vStart;
        log.info("Message handling took " + took);
        return success;
    }

    private void loadReferencedAttachments(EmailRecipientDTO er, EmailSendQueState emailSendQueState) throws Exception {
        Optional<List> attachmentReferences = as(effectiveReplacementField(er, ADDITIONAL_ATTACHMENT_URIS_PARAMETER), List.class);
        if (attachmentReferences.isPresent()) {
            List uris = attachmentReferences.get();
            for (Object uri : uris) {
                downloadAttachment(er, emailSendQueState, uri.toString());
            }
        }
        Optional<String> attachmentReference = as(effectiveReplacementField(er, ADDITIONAL_ATTACHMENT_URI_PARAMETER), String.class);
        if (attachmentReference.isPresent()) {
            String uri = attachmentReference.get();
            downloadAttachment(er, emailSendQueState, uri);
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

    private void downloadAttachment(EmailRecipientDTO er, EmailSendQueState emailSendQueState, String uri) throws Exception {
        log.info("Downloading attachment for EmailRecipientDTO={}, URI={}", er.getRecipientID(), uri);
        EmailAttachment attachment = downloaderForUri(uri).download(uri);
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
        } else {
            if (failureCause.length() > 250)
                failureCause = failureCause.substring(0, 250);
        }
        return failureCause;
    }

    private EmailMessageDTO getMessage(Long messageId) {
        EmailMessageDTO message = messageCache.get(messageId);
        if (message == null) {
            message = rrService.getMessage(messageId);
            messageCache.put(messageId, message);
        }
        return message;
    }
    
    private void rebuildMessage(EmailRecipientDTO er, Long messageId, EmailMessageDTO message) throws Exception {
        log.info("Build message content using template for messageId=" + messageId);
        List<ReportedRecipientReplacementDTO> recipientReplacements = getRecipientReplacements(er);

        String buildMessage = templateBuilder.buildTempleMessage(message.getBody(),
                message.getMessageReplacements(), recipientReplacements);

        if (!StringUtils.isEmpty(buildMessage)) {
            message.setBody(buildMessage);
        } else {
            throw new Exception("Template build error. messageId=" + messageId);
        }

        String buildMessageSubject = templateBuilder.buildTempleMessage(message.getSubject(), 
            message.getMessageReplacements(), recipientReplacements);
        if (!StringUtils.isEmpty(buildMessageSubject)) {
            message.setSubject(buildMessageSubject);
        } else {
            throw new Exception("Message subject build error. messageId=" + messageId);
        }
    }

    private List<ReportedRecipientReplacementDTO> getRecipientReplacements(EmailRecipientDTO er) {
        List<ReportedRecipientReplacementDTO> recipientReplacements = null;
        try {
            recipientReplacements = rrService.getRecipientReplacements(er.getRecipientID());
        } catch (IOException e) {
            log.error("Getting recipient replacements failed for receipientId:"
                    + er.getRecipientID());
        }
        return recipientReplacements;
    }

    private boolean virusCheckRequired(EmailMessageDTO message, EmailRecipientDTO recipient) {
        return virusCheckRequired && (
                    !message.isVirusChecked()
                ||  (recipient.getAttachments() != null && !recipient.getAttachments().isEmpty())
        );
    }

    private void doVirusCheck(EmailMessageDTO message, EmailRecipientDTO recipient) {
        try {
            emailAVChecker.checkMessage(emailSender, message, Optional.fromNullable(recipient));
        } catch (Exception e) {
            log.error("Virus check failed" + e);
        }
    }

    private boolean virusCheckPassed(EmailMessageDTO message) {
        return !virusCheckRequired || (message.isVirusChecked() && !message.isInfected());
    }

    public void setMaxTasksToStartAtOnce(int maxTasksToStartAtOnce) {
        this.maxTasksToStartAtOnce = maxTasksToStartAtOnce;
    }

    public void setVirusCheckRequired(boolean virusCheckRequired) {
        this.virusCheckRequired = virusCheckRequired;
    }
}
