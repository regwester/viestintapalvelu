package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedRecipientReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.service.EmailService;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;
import fi.vm.sade.ryhmasahkoposti.service.ReportedAttachmentService;
import fi.vm.sade.ryhmasahkoposti.util.TemplateBuilder;

@Service
public class EmailServiceImpl implements EmailService {
    
    private static final Logger log = LoggerFactory.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailServiceImpl.class);

    private static final int MAX_CACHE_ENTRIES = 10;

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

    private TemplateBuilder templateBuilder = new TemplateBuilder();

    @Value("${ryhmasahkoposti.require.virus.check:true}")
    private boolean virusCheckRequired;

    @Value("${ryhmasahkoposti.queue.handle.size}")
    private String queueSizeString = "1000";

    private Map<Long, EmailMessageDTO> messageCache = new LinkedHashMap<Long, EmailMessageDTO>(MAX_CACHE_ENTRIES + 1) {
        private static final long serialVersionUID = 1L;

        protected boolean removeEldestEntry(Map.Entry<Long, EmailMessageDTO> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        };
    };

    @Override
    public Long getCount(String oid) {
        return emailDao.findNumberOfReportedMessage(oid);
    }

    /**
     * Spring scheduler method
     */
    public void handleEmailQueue() {
        int queueMaxSize = 1000;
        try {
            queueMaxSize = Integer.parseInt(queueSizeString);
        } catch (NumberFormatException nfe) {
            // never mind use default
        }
        long start = System.currentTimeMillis();
        log.info("Handling queue (max size " + queueMaxSize + "). Sending emails using: " + emailSender);

        List<EmailRecipientDTO> queue = rrService.getUnhandledMessageRecipients(queueMaxSize);

        int sent = 0;
        int errors = 0;
        int queueSize = queue.size();
        for (EmailRecipientDTO er : queue) {
            if (handleRecipient(er)) {
                sent++;
            } else {
                errors++;
            }
        }
        long took = System.currentTimeMillis() - start;
        log.info("Sending emails done. Queue: " + queueSize + ", sent: " + sent + ", errors: " + errors + ", took: "
                + took + " ms.");
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
