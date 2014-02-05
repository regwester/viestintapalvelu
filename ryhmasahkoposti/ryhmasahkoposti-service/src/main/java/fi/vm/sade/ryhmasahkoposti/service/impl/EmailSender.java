package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;

@Service
public class EmailSender {
	private final static Logger log = Logger
			.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailSender.class
					.getName());

	@Value("${ryhmasahkoposti.smtp.host}")
	String smtpHost;
	@Value("${ryhmasahkoposti.smtp.port}")
	String smtpPort;

	public boolean sendMail(EmailMessage emailMessage, String emailAddress) {

		if (emailMessage == null) {
			throw new IllegalStateException("Email message missing");
		}

		MimeMessage message = null;
		boolean sentOk = false;

		// build the message (both in real and debug mode)
		try {
			Properties mailProps = new Properties();
			mailProps.put("mail.smtp.host", smtpHost);
			mailProps.put("mail.smtp.port", smtpPort);
			Session session = Session.getInstance(mailProps);
			MimeMessage msg = new MimeMessage(session);
			InternetAddress[] toAddrs = InternetAddress.parse(emailAddress,
					false);

			msg.setRecipients(Message.RecipientType.TO, toAddrs);
			msg.setFrom(new InternetAddress(emailMessage.getSenderEmail())); 
			msg.setSubject(emailMessage.getSubject(), emailMessage.getCharset());
			if (emailMessage.getReplyToAddress() != null) {
				InternetAddress[] replyToAddrs = InternetAddress.parse(emailMessage.getReplyToAddress(),
						false);
				msg.setReplyTo(replyToAddrs);
			}
			
			// Setup message part (part I)
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setText(emailMessage.getBody()
					+ emailMessage.getFooter());
			mimeBodyPart.setContent(
					emailMessage.getBody() + emailMessage.getFooter(),
					(emailMessage.isHtml() ? "text/html" : "text/plain")
							+ "; charset=" + emailMessage.getCharset());

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			if (emailMessage.getAttachments() != null) {
				for (EmailAttachment attachment : emailMessage.getAttachments()) {
					//DataSource ds = stringToDataSource(attachment);
					if ((attachment.getData() != null)
							&& (attachment.getName() != null)) {
						ByteArrayDataSource ds = new ByteArrayDataSource(attachment.getData(), 
								attachment.getContentType());
						
						// Attachment part (part II)
						mimeBodyPart = new MimeBodyPart();
						mimeBodyPart.setDataHandler(new DataHandler(ds));
						mimeBodyPart.setFileName(attachment.getName());
						multipart.addBodyPart(mimeBodyPart);
						mimeBodyPart.setHeader("Content-Type",
								attachment.getContentType());
					} else {
						msg = null;
						log.log(Level.SEVERE,
								"Failed to insert attachment - it is not valid "
										+ attachment.getName());

						break;
					}
				}
			}

			if (msg != null) {
				msg.setContent(multipart); // parts to message
				message = msg;

			}
		} catch (Exception e) {
			sentOk = false;
			log.log(Level.SEVERE, "Failed to build message to " + emailAddress
					+ ": " + emailMessage.getBody() + emailMessage.getFooter(),
					e);
		}

		if (message != null) { // message was created successfully

			if (EmailConstants.TEST_MODE.equals("NO")) {

				String logMsg = " in mailsending (Smtp: " + EmailConstants.SMTP
						+ "), message '" + emailMessage.getSubject() + "' to '"
						+ emailAddress + "'";
				try {
					Transport.send(message);
					log.info("Success" + logMsg);
					sentOk = true;
				} catch (MessagingException e) {
					sentOk = false;
					log.log(Level.SEVERE, " Problems" + logMsg + ": " + e);
				}

			} else { // just log what would have been sent
				StringBuffer sb = new StringBuffer("Email dummysender:");
				sb.append("\nFROM:    ");
				sb.append(emailMessage.getSenderEmail());
				sb.append("\nTO:      ");
				// sb.append(emailMessage.getHeader().getEmail());
				sb.append(emailAddress);
				sb.append("\nSUBJECT: ");
				sb.append(emailMessage.getSubject());
				if (emailMessage.getAttachments() != null
						&& emailMessage.getAttachments().size() > 0) {
					sb.append("\nATTACHMENTS:");
					for (EmailAttachment attachment : emailMessage
							.getAttachments()) {

						sb.append(" ");
						sb.append(attachment.getName());
						sb.append("(");
						sb.append(attachment.getContentType());
						sb.append(")");
					}
				}
				sb.append("\n");
				sb.append(emailMessage.getBody() + emailMessage.getFooter());
				log.info(sb.toString());
				sentOk = true;
			}
		}

		return sentOk;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	@Override
	public String toString() {
		return "EmailSender: [smtp host " + smtpHost + " port " + smtpPort
				+ "]";
	}

}
