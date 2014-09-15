package fi.vm.sade.ryhmasahkoposti.service.impl;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedRecipientReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.service.EmailSendQueueService;
import fi.vm.sade.ryhmasahkoposti.service.EmailService;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;
import fi.vm.sade.ryhmasahkoposti.service.ReportedAttachmentService;
import fi.vm.sade.ryhmasahkoposti.service.dto.EmailQueueHandleDto;
import fi.vm.sade.ryhmasahkoposti.util.TemplateBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.inject.Named;
import javax.mail.internet.MimeMessage;
import javax.persistence.OptimisticLockException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
    
    private static final Logger log = LoggerFactory.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailServiceImpl.class);

    private static final int MAX_CACHE_ENTRIES = 10;
    // If we have pool size of 10 it takes approx. 14s to process a 250 recipient / queue
    // concurrently with 10 executors. And we have max 1 min before a new cron check call
    // add to add in new calls. So assume the optimum maximum to start at once to be:
    // 60/14*10 ~= 42 (if we start more, they would be queued and only cause extra queries later)
    private static final int MAX_TASKS_TO_START_AT_ONCE = 42;

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

    private TemplateBuilder templateBuilder = new TemplateBuilder();

    @Value("${ryhmasahkoposti.require.virus.check:true}")
    private boolean virusCheckRequired;


    private Map<Long, EmailMessageDTO> messageCache = new LinkedHashMap<Long, EmailMessageDTO>(MAX_CACHE_ENTRIES + 1) {
        private static final long serialVersionUID = 1L;

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
        MimeMessage message = emailSender.createMail(emailMessage, emailAddress);
        message.writeTo(baos);
        return new String(baos.toByteArray());
    }

    /**
     * Spring scheduler method
     */
    @Override
    public void checkEmailQueues() {
        emailQueueService.checkForStoppedProcesses();
        int numberOfQueues = emailQueueService.getNumberOfUnhandledQueues();
        if (numberOfQueues > 0) {
            int numberOfProcessesToStart = Math.min(MAX_TASKS_TO_START_AT_ONCE, numberOfQueues);
            log.info("Found {} unhanled queues. Starting {} new handleEmailQueue processes.",
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
        int maxConcurrentLockFailures = MAX_TASKS_TO_START_AT_ONCE -1,
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
        if (queue != null) {
            log.info("Handling EmailQueue={}. Sending emails using: {}", queue.getId(), emailSender);
            int sent = 0;
            int errors = 0;
            int queueSize = queue.getRecipients().size();
            Date lastHandledAt = new Date();
            for (EmailRecipientDTO emailRecipient : queue.getRecipients()) {
                long recipientStart = System.currentTimeMillis();
                if (!emailQueueService.continueQueueHandling(queue)) {
                    long took = System.currentTimeMillis() - start;
                    log.error("Email send queue handler for SendQueue={} stopped." +
                            "Queue size: {}, sent: {}, errors: {}, took: {} ms.",
                            queueSize, sent, errors, took, queue.getId());
                    return;
                }
                if (handleRecipient(emailRecipient)) {
                    sent++;
                } else {
                    errors++;
                }
                long singleRecipientTook = System.currentTimeMillis()-recipientStart;
                log.debug("Handled single recipient in {} ms.", singleRecipientTook);
            }
            emailQueueService.queueHandled(queue.getId());
            long took = System.currentTimeMillis() - start;
            log.info("Sending emails done. SendQueue={}: size: {}, sent: {}, errors: {}, took: {} ms.",
                    queue.getId(), queueSize, sent, errors, took);
        } else {
            log.info("Checked for open email sending queues and found none.");
        }
    }

    private boolean handleRecipient(EmailRecipientDTO er) {
        long vStart = System.currentTimeMillis();
        log.info("Handling " + er + " " + er.getRecipientID());
        boolean success = false;
        if (rrService.startSending(er)) {
            log.info("Handling really " + er + " " + er.getRecipientID());
            Long messageId = er.getEmailMessageID();

            try {
                EmailMessageDTO message = getMessage(messageId);
                if (virusCheckRequired(message)) {
                    doVirusCheck(message);
                }
                
                if (virusCheckPassed(message)) {
                    if (StringUtils.equalsIgnoreCase(message.getType(), ReportedMessage.TYPE_TEMPLATE)) {
                        rebuildMessage(er, messageId, message);
                    }
                    emailSender.handleMail(message, er.getEmail());
                } else {
                    throw new Exception("Virus check problem. File infected: " + message.isInfected());
                }
                rrService.recipientHandledSuccess(er, ""); // result is left empty in OK situations
                success = true;
            } catch (Exception e) {
                String failureCause = getFailureCause(e);
                rrService.recipientHandledFailure(er, failureCause);
            }
        }
        long took = System.currentTimeMillis() - vStart;
        log.info("Message handling took " + took);
        return success;
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
        } else
            throw new Exception("Template build error. messageId=" + messageId);

        String buildMessageSubject = templateBuilder.buildTempleMessage(message.getSubject(), 
            message.getMessageReplacements(), recipientReplacements);
        if (!StringUtils.isEmpty(buildMessageSubject)) {
            message.setSubject(buildMessageSubject);
        } else
            throw new Exception("Message subject build error. messageId=" + messageId);
        

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

    private boolean virusCheckRequired(EmailMessageDTO message) {
        return virusCheckRequired && !message.isVirusChecked();
    }

    private void doVirusCheck(EmailMessageDTO message) {
        try {
            emailAVChecker.checkMessage(emailSender, message);
        } catch (Exception e) {
            log.error("Virus check failed" + e);
        }
    }

    private boolean virusCheckPassed(EmailMessageDTO message) {
        return !virusCheckRequired || (message.isVirusChecked() && !message.isInfected());
    }
}
