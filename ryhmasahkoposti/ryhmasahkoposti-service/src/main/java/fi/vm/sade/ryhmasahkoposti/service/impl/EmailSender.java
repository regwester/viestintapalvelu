package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.codec.binary.Base64;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;


public class EmailUtil {
    private final static Logger log = Logger.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailUtil.class.getName()); 
    
	public static Properties props;

	public static boolean sendMail(EmailMessage emailMessage, String emailAddress) {		 
		final String username = "username";
		final String password = "password";		
		
		if (emailMessage == null) {
			throw new IllegalStateException("Email message missing");
		}
		
//		String smtp = "localhost"; 
//		String smtp = "smtp.gmail.com"; //"localhost"; //EmailConstants.SMTP; // Util.getProp(SMTP); /* !!!!! */
//		String port = "587";
			
		MimeMessage message = null;
		boolean sentOk = false;

		// build the message (both in real and debug mode)
		try {
			Properties mailProps = new Properties();			
			mailProps.put("mail.smtp.host", EmailConstants.SMTP);
			mailProps.put("mail.smtp.port", EmailConstants.PORT);	

			
//			mailProps.put("mail.smtp.socketFactory.port", "465");
//			mailProps.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//			mailProps.put("mail.smtp.socketFactory.fallback", "false");
//			mailProps.put("mail.smtp.auth", "true");
//			mailProps.put("mail.smtp.port", "465");			
//			
//			mailProps.put("mail.smtp.starttls.enable", "true");

//			mailProps.put("mail.smtp.port", port);	
			
//			mailProps.put("mail.smtp.auth", "true");			
//			mailProps.put("mail.smtp.localhost", smtp);
			

			Session session = Session.getInstance(mailProps);
			
//			Session session = Session.getInstance(mailProps, 
//			new javax.mail.Authenticator() {
//				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication(username, password);
//				}
//			});
			
			MimeMessage msg = new MimeMessage(session);
//			InternetAddress[] toAddrs = InternetAddress.parse(emailMessage.getHeader().getEmail(), false);
			InternetAddress[] toAddrs = InternetAddress.parse(emailAddress, false);
			msg.setRecipients(Message.RecipientType.TO, toAddrs);
			msg.setFrom(new InternetAddress(emailMessage.getSenderEmail())); //msgFrom)); /* !!!!! */
			msg.setSubject(emailMessage.getSubject(), emailMessage.getCharset());

			// Setup message part (part I)
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setText(emailMessage.getBody() + emailMessage.getFooter());
			mimeBodyPart.setContent(emailMessage.getBody() + emailMessage.getFooter(), (emailMessage.isHtml() ? "text/html" : "text/plain") + "; charset=" + emailMessage.getCharset());

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			if (emailMessage.getAttachments() != null) {
				for (EmailAttachment attachment : emailMessage.getAttachments()) {
					DataSource ds = stringToDataSource(attachment);
					if ((attachment.getAttachment() != null) && (attachment.getName() != null) && (ds != null)) {
						// Attachment part (part II)
						mimeBodyPart = new MimeBodyPart();
						mimeBodyPart.setDataHandler(new DataHandler(ds));
						mimeBodyPart.setFileName(attachment.getName());
						multipart.addBodyPart(mimeBodyPart);
						mimeBodyPart.setHeader("Content-Type", attachment.getContentType());
					} else {
						msg = null;
						log.log(Level.SEVERE, "Failed to insert attachment - it is not valid " + attachment.getName());
						
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
//			log.log(Level.SEVERE, "Failed to build message to " + emailMessage.getHeader().getEmail() + ": " + emailMessage.getBody() + emailMessage.getFooter(), e);
			log.log(Level.SEVERE, "Failed to build message to " + emailAddress + ": " + emailMessage.getBody() + emailMessage.getFooter(), e);
		}

		if (message != null) { // message was created successfully
			
			if (EmailConstants.TEST_MODE.equals("NO")) {

//				String logMsg = " in mailsending (Smtp: " + EmailConstants.SMTP + "), message '" + emailMessage.getSubject() + "' to '" + emailMessage.getHeader().getEmail() + "'";
				String logMsg = " in mailsending (Smtp: " + EmailConstants.SMTP + "), message '" + emailMessage.getSubject() + "' to '" + emailAddress + "'";
				try {
					Transport.send(message);
					log.info("Success"+logMsg);
					sentOk = true;
				} catch (MessagingException e) {
					sentOk = false;					
					log.log(Level.SEVERE, " Problems" +logMsg + ": " + e);
				}
				
			} else { // just log what would have been sent
				StringBuffer sb = new StringBuffer("Email dummysender:");
				sb.append("\nFROM:    ");
				sb.append(emailMessage.getSenderEmail());
				sb.append("\nTO:      ");
//				sb.append(emailMessage.getHeader().getEmail());
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
				sb.append(emailMessage.getBody() + emailMessage.getFooter());
				log.info(sb.toString());
				sentOk = true;
			}
		}

		return sentOk;
	}
	
	
	private static DataSource stringToDataSource (EmailAttachment attachment) {
        byte[] encoded = attachment.getAttachment().getBytes();
        byte[] decoded = Base64.decodeBase64(encoded);      
        String attach = new String(decoded);
		
        try {
			return new ByteArrayDataSource(attach, attachment.getContentType());

        } catch (IOException e) {
			attachment = null;
			e.printStackTrace();
			return null;
		}		
	}
	
}
