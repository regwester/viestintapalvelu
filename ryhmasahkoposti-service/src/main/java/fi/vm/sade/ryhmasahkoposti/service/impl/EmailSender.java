package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;

@Service
public class EmailSender {
    private final static Logger log = Logger.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailSender.class
            .getName());

    @Value("${ryhmasahkoposti.smtp.host}")
    String smtpHost;
    @Value("${ryhmasahkoposti.smtp.port}")
    String smtpPort;
    
    public void handleMail(EmailMessage emailMessage, String emailAddress) throws Exception {
        
        if (emailMessage == null) {
            throw new IllegalStateException("Email message missing");
        }

        MimeMessage message = createMimeMessage(emailMessage, emailAddress);
        
        if (message != null) { // message was created successfully
            if (EmailConstants.TEST_MODE.equals("NO")) {
                sendMail(message);
            } else {
                mockSendMail(emailMessage, emailAddress); //just log the message
            }
        } else {
            throw new Exception("MimeMessage creation failed!");
        }
    }

    private MimeMessage createMimeMessage(EmailMessage emailMessage, String emailAddress) {
        
        MimeMessage message = null;
        try {
            message = createMail(emailMessage, emailAddress);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to build message to " + emailAddress + ": " + emailMessage.getBody(), e);
        }
        return message;
    }

    public MimeMessage createMail(EmailMessage emailMessage, String emailAddress) throws MessagingException, UnsupportedEncodingException {
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
        
        if (emailMessage.getAttachments() != null) {
            insertAttachments(emailMessage, msgContent);
        }

        if (emailMessage.isValid()) {
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

    private void insertAttachments(EmailMessage emailMessage, MimeMultipart multipart) throws MessagingException {
        for (EmailAttachment attachment : emailMessage.getAttachments()) {
            if ((attachment.getData() != null) && (attachment.getName() != null)) {
                ByteArrayDataSource ds = new ByteArrayDataSource(attachment.getData(), attachment.getContentType());

                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.setDataHandler(new DataHandler(ds));
                attachmentPart.setFileName(attachment.getName());
                attachmentPart.setHeader("Content-Type", attachment.getContentType());
                multipart.addBodyPart(attachmentPart);

            } else {
                log.log(Level.SEVERE, "Failed to insert attachment - it is not valid " + attachment.getName());
                emailMessage.setInvalid();
                break;
            }
        }
    }

    private Session createSession() {
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", smtpHost);
        mailProps.put("mail.smtp.port", smtpPort);
        Session session = Session.getInstance(mailProps);
        return session;
    }

    private void sendMail(MimeMessage message) throws Exception {
        
        String logMsg = " in mailsending (Smtp: " + EmailConstants.SMTP + "), " + " SUBJECT '"
                + message.getSubject() + "' FROM '" + message.getFrom() + "' REPLYTO '"
                + (message.getReplyTo() == null ? "" : message.getReplyTo()) + "' TO '"
                + message.getRecipients(MimeMessage.RecipientType.TO)[0].toString() + "'";
        try {
            Transport.send(message);
            log.info(getTimestamp() + "Success" + logMsg);
        } catch (MessagingException e) {
            log.log(Level.SEVERE, getTimestamp() + " Problems" + logMsg + ": " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    private void mockSendMail(EmailMessage emailMessage, String emailAddress) {
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
        sb.append("\n");
        sb.append(emailMessage.getBody());
        log.info(sb.toString());
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
