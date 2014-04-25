package fi.vm.sade.ryhmasahkoposti.resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailSendId;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.api.resource.EmailResource;
import fi.vm.sade.ryhmasahkoposti.service.EmailService;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;

@Component
public class EmailResourceImpl implements EmailResource {
    private final static Logger log = 
    		Logger.getLogger(fi.vm.sade.ryhmasahkoposti.resource.EmailResourceImpl.class.getName());	
     
    @Autowired    
    private EmailService emailService;
    
	@Autowired
	private GroupEmailReportingService sendDbService;

    @SuppressWarnings("unchecked")
    @Override
    public String addAttachment(@Context HttpServletRequest request, @Context HttpServletResponse response) 
        throws IOException, URISyntaxException, ServletException {

		log.log(Level.INFO, "Adding attachment "+request.getMethod());
        
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        AttachmentResponse result = null;
        
        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List<FileItem> items = upload.parseRequest(request);
                Iterator<FileItem> iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    result = storeAttachment(item);
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            response.setStatus(400);
            response.getWriter().append("Not a multipart request");
        }
		log.log(Level.INFO, "Added attachment: " + result);
		JSONObject json = new JSONObject(result.toMap());
		return json.toString();
    }

    @Override
    public EmailData getEmailDataAsJSON() {
        EmailData emailData = new EmailData();
        emailData.setSenderOid("lahettajan oid");
        
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setBody("s-postiviestin sisalto");
        emailMessage.setCallingProcess("kutsuvaprosessi esim. valinta");
        emailMessage.setCharset("koodisto");
        emailMessage.setFrom("lahettajan s-postiosoite");
        emailMessage.setHtml(false);
        emailMessage.setOrganizationOid("lahettajan organisaation oid-tunnus");
        emailMessage.setReplyTo("vastaus s-postiosoite");
        emailMessage.setSenderOid("lahettajan oid-tunnus");
        emailMessage.setSubject("s-postin aihe");
        
        List<EmailRecipient> emailRecipients = new LinkedList<EmailRecipient>();
        EmailRecipient emailRecipient = new EmailRecipient();
        emailRecipient.setEmail("vastaanottajan s-postiosoite");
        emailRecipient.setLanguageCode("vastaanottajan kielikoodi");
        emailRecipient.setOid("vastaanottajan oid-tunnus");
        emailRecipient.setOidType("arvoksi tyhja");
        emailRecipients.add(emailRecipient);
        
        emailData.setEmail(emailMessage);
        emailData.setRecipient(emailRecipients);
        
        return emailData;
    }

    @Override
    public Response initGroupEmail() {
        Response response = Response.ok("ok").build();
        return response;
    }

    @Override
    public EmailResponse sendEmail(EmailMessage input) {
		EmailResponse response = emailService.sendEmail(input); 
		return response;
    }
	

	@Override
    public List<EmailResponse> sendEmails(List<EmailMessage> input) {
        List<EmailResponse> responses = new ArrayList<EmailResponse>();     
        for (EmailMessage email : input) {          
            EmailResponse resp = emailService.sendEmail(email);
            responses.add(resp);            
        }
        return responses;   
    }
    
	@Override
    public SendingStatusDTO sendEmailStatus(String sendId) {
		log.log(Level.INFO, "sendEmailStatus called with ID: " + sendId + ".");

		return sendDbService.getSendingStatus(Long.valueOf(sendId));
    }

	@Override
    public EmailSendId sendGroupEmail(EmailData emailData) {
		// Setting footer with the first ones language code
	    String languageCode = emailData.getRecipient().get(0).getLanguageCode();
	    // Footer is moved to the end of the body here
	    emailData.setEmailFooter(languageCode);
	    
		String sendId = "";
		try {
			sendId = Long.toString( sendDbService.addSendingGroupEmail(emailData));
			log.log(Level.INFO, "DB index is " + sendId);
			
		} catch (IOException e) {	
			log.log(Level.SEVERE, "Problems in writing send data info to DB, "+ e.getMessage());
		}
		return new EmailSendId(sendId);
    }
    
	@Override
    public ReportedMessageDTO sendResult(String sendId) {
		log.log(Level.INFO, "sendResult called with ID: " + sendId + ".");

		return sendDbService.getReportedMessage(Long.valueOf(sendId));
    }

    private AttachmentResponse storeAttachment(FileItem item) throws Exception {
        AttachmentResponse result = new AttachmentResponse();
        
        if (!item.isFormField()) {
            Long id = sendDbService.saveAttachment(item);
        	
            String fileName = item.getName();
            String contentType = item.getContentType();
            byte[] data = item.get();
            result.setFileName(fileName);
            result.setContentType(contentType);
            result.setFileSize(data.length);    
            result.setUuid(id.toString());
        }
        return result;
    }
}
