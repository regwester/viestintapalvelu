package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
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
import fi.vm.sade.ryhmasahkoposti.service.ReportedAttachmentService;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;
import fi.vm.sade.ryhmasahkoposti.util.TemplateBuilder;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = Logger.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailServiceImpl.class
            .getName());

    private static final int MAX_CACHE_ENTRIES = 10;

    @Autowired
    private GroupEmailReportingService rrService;

    @Autowired
    private ReportedAttachmentService liiteService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailAVChecker emailAVChecker;

    private TemplateBuilder templateBuilder = new TemplateBuilder();

    @Value("${ryhmasahkoposti.require.virus.check}")
    String virusChekcRequired;

    @Value("${ryhmasahkoposti.queue.handle.size}")
    String queueSizeString = "1000";

    private Map<Long, EmailMessageDTO> messageCache = new LinkedHashMap<Long, EmailMessageDTO>(MAX_CACHE_ENTRIES + 1) {
        private static final long serialVersionUID = 1L;

        protected boolean removeEldestEntry(Map.Entry<Long, EmailMessageDTO> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        };
    };

    @Override
    public EmailResponse sendEmail(EmailMessage email) {

        // email.setFooter(email.getHeader().getLanguageCode());
        log.info("Send email info: " + email.toString());

        boolean sendStatus;
        try {
            sendStatus = emailSender.sendMail(email, "tähän vastaanottajan osoite jotenkin");
        } catch (Exception e) {
            sendStatus = false;
        }
        String status = (sendStatus ? "OK" : "Error");
        // email.setSendStatus(status); LAITETAAN KANTAAN.

        // MITEN TÄMÄ MUUTTUU
        // EmailResponse resp = new EmailResponse(email.getHeader(), status,
        // email.getSubject(), Integer.toString(email.getAttachments().size()));
        EmailResponse resp = new EmailResponse(status, email.getSubject());
        log.info("Email  response: " + resp.toString());
        return resp;
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

        boolean checkForViruses = true;
        if (virusChekcRequired != null && virusChekcRequired.equalsIgnoreCase("false")) {
            checkForViruses = false;
        }

        int sent = 0;
        int errors = 0;
        int queueSize = queue.size();
        for (EmailRecipientDTO er : queue) {
            long vStart = System.currentTimeMillis();
            log.info("Handling " + er + " " + er.getRecipientID());
            if (rrService.startSending(er)) {
                log.info("Handling really " + er + " " + er.getRecipientID());
                Long messageId = er.getEmailMessageID();
                EmailMessageDTO message = messageCache.get(messageId);
                if (message == null) {
                    try {
                        message = rrService.getMessage(messageId);
                        messageCache.put(messageId, message);
                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Get message failed", e);
                    }
                }

                String result = "";
                boolean success = false;
                try {
                    if (checkForViruses && !message.isVirusChecked()) {
                        try {
                            emailAVChecker.checkMessage(emailSender, message);
                        } catch (Exception e) {
                            log.severe("Virus check failed" + e);
                        }
                    }
                    if (!checkForViruses || (message.isVirusChecked() && !message.isInfected())) {

                        // Build message by running replacements if a template
                        // message is used
                        if (StringUtils.equalsIgnoreCase(message.getType(), ReportedMessage.TYPE_TEMPLATE)) {
                            log.info("Build message content using template for messageId=" + messageId);

                            List<ReportedRecipientReplacementDTO> recipientReplacements = null;
                            try {
                                recipientReplacements = rrService.getRecipientReplacements(er.getRecipientID());
                            } catch (IOException e) {
                                log.severe("Getting recipient replacements failed for receipientId:"
                                        + er.getRecipientID());
                            }

                            String buildMessage = templateBuilder.buildTempleMessage(message.getBody(),
                                    message.getMessageReplacements(), recipientReplacements);

                            if (!StringUtils.isEmpty(buildMessage)) {
                                message.setBody(buildMessage);
                                success = emailSender.sendMail(message, er.getEmail());
                            } else
                                result = "Template build error. messageId=" + messageId;
                        } else
                            success = emailSender.sendMail(message, er.getEmail());
                    } else {
                        result = "Virus check problem. File infected: " + message.isInfected();
                    }
                } catch (Exception e) {
                    result = e.getMessage();
                    if (result == null) {
                        result = "e.getMessage() == null";
                    } else {
                        if (result.length() > 250)
                            result = result.substring(0, 250);
                    }
                }

                if (success) {
                    result = "";
                    rrService.recipientHandledSuccess(er, result); // result is
                                                                   // not
                                                                   // updated in
                                                                   // OK
                                                                   // situations.
                                                                   // It's left
                                                                   // empty.
                    sent++;
                } else {
                    rrService.recipientHandledFailure(er, result);
                    errors++;
                }
            }
            long took = System.currentTimeMillis() - vStart;
            log.info("Message handling took " + took);
        }
        long took = System.currentTimeMillis() - start;
        log.info("Sending emails done. Queue: " + queueSize + ", sent: " + sent + ", errors: " + errors + ", took: "
                + took + " ms.");
    }
}
