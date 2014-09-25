package fi.vm.sade.ryhmasahkoposti.service.impl;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentContainer;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Properties;

import com.google.common.base.Optional;

@Service
public class EmailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailSender.class);

    @Value("${ryhmasahkoposti.smtp.host}")
    String smtpHost;
    @Value("${ryhmasahkoposti.smtp.port}")
    String smtpPort;
    
    public void handleMail(EmailMessage emailMessage, String emailAddress,
                           Optional<? extends AttachmentContainer> additionalAttachments) throws Exception {
        try {
            MimeMessage message = createMail(emailMessage, emailAddress, additionalAttachments);
            if (EmailConstants.TEST_MODE.equals("NO")) {
                LOGGER.debug("Sending message: " + message.toString());
                long start = System.currentTimeMillis();
                Transport.send(message);
                long took = System.currentTimeMillis() -start;
                LOGGER.debug("Message sent took: " + took);
            } else {
                mockSendMail(emailMessage, emailAddress, additionalAttachments); //just log the message
            }
        } catch (Exception e) {
            LOGGER.error("Failed to send message to " + emailAddress + ": " + emailMessage.getBody(), e);
            throw e;
        }
    }

    public MimeMessage createMail(EmailMessage emailMessage, String emailAddress,
                                  Optional<? extends AttachmentContainer> additionalAttachments)
            throws MessagingException, UnsupportedEncodingException {
        Session session = createSession();
        MimeMessage msg = new MimeMessage(session);

        msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(emailAddress, false));
        msg.setFrom(new InternetAddress(emailMessage.getFrom(), emailMessage.getSender()));
        msg.setSubject(emailMessage.getSubject(), emailMessage.getCharset());
        if (emailMessage.getReplyTo() != null) {
            InternetAddress[] replyToAddrs = InternetAddress.parse(emailMessage.getReplyTo(), false);
            msg.setReplyTo(replyToAddrs);
        }

        MimeMultipart msgContent = new MimeMultipart("mixed");
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(getContent(emailMessage), getContentType(emailMessage) + ";charset=" + emailMessage.getCharset());
        bodyPart.setHeader("Content-Transfer-Encoding", getContentTransferEncoding());
        msgContent.addBodyPart(bodyPart);

        boolean valid = emailMessage.isValid();
        if (valid && emailMessage.getAttachments() != null) {
            valid = insertAttachments(emailMessage, msgContent);
        }
        if (valid && additionalAttachments.isPresent() && additionalAttachments.get().getAttachments() != null) {
            valid = insertAttachments(additionalAttachments.get(), msgContent);
        }

        if (valid) {
            msg.setContent(msgContent);
            msg.setHeader("Content-Transfer-Encoding", getContentTransferEncoding());
            msg.saveChanges(); //updates changes to headers
            return msg;
        }
        return null;
    }

    private String getContent(EmailMessage emailMessage) {
        if(!emailMessage.isHtml()) {
            //Change LF to CRLF (SMTP standard)
            //The extra space at the end is a "temporary" bug fix to plaintext LF issue
            return emailMessage.getBody().replaceAll("\n", "\r\n") + " ";
        }
        return emailMessage.getBody();
    }
    
    /*
     * 8BITMIME SMTP Extension should be supported by 99% of SMTP servers.
     */
    private String getContentTransferEncoding() {
        return "8bit";
    }

    private String getContentType(EmailMessage emailMessage) {
        return emailMessage.isHtml() ? "text/html" : "text/plain";
    }

    private boolean insertAttachments(AttachmentContainer container, MimeMultipart multipart) throws MessagingException {
        for (EmailAttachment attachment : container.getAttachments()) {
            if ((attachment.getData() != null) && (attachment.getName() != null)) {
                ByteArrayDataSource ds = new ByteArrayDataSource(attachment.getData(), attachment.getContentType());

                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.setDataHandler(new DataHandler(ds));
                attachmentPart.setFileName(attachment.getName());
                attachmentPart.setHeader("Content-Type", attachment.getContentType());
                multipart.addBodyPart(attachmentPart);

            } else {
                LOGGER.error("Failed to insert attachment - it is not valid " + attachment.getName());
                return false;
            }
        }
        return true;
    }

    private Session createSession() {
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", smtpHost);
        mailProps.put("mail.smtp.port", smtpPort);
        Session session = Session.getInstance(mailProps);
        return session;
    }

    private void mockSendMail(EmailMessage emailMessage, String emailAddress,
                              Optional<? extends AttachmentContainer> additionalAttachments) {
        // Log the content of the message
        StringBuffer sb = new StringBuffer("Email dummysender:");
        sb.append("\nFROM:    ");
        sb.append(emailMessage.getFrom());
        sb.append("\nTO:      ");
        sb.append(emailAddress);
        sb.append("\nSUBJECT: ");
        sb.append(emailMessage.getSubject());
        if (emailMessage.getAttachments() != null && emailMessage.getAttachments().size() > 0) {
            sb.append("\nATTACHMENTS:");
            for (EmailAttachment attachment : emailMessage.getAttachments()) {
                sb.append(" ");
                sb.append(attachment.getName());
                sb.append("(");
                sb.append(attachment.getContentType());
                sb.append(")");
            }
        }
        if (additionalAttachments.isPresent()) {
            sb.append("\nADDITIONAL ATTACHMENTS:");
            for (EmailAttachment attachment : additionalAttachments.get().getAttachments()) {
                sb.append(" ");
                sb.append(attachment.getName());
                sb.append("(");
                sb.append(attachment.getContentType());
                sb.append(")");
            }
        }
        sb.append("\n");
        sb.append(emailMessage.getBody());
        LOGGER.debug("MockSendMail:\n" + sb.toString());
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    private String getTimestamp() {
        java.util.Date date = new java.util.Date();
        return "RYHMAVIESTI: " + new Timestamp(date.getTime()) + ": ";
    }
    
    @Override
    public String toString() {
        return "EmailSender: [smtp host " + smtpHost + " port " + smtpPort + "]";
    }

}
