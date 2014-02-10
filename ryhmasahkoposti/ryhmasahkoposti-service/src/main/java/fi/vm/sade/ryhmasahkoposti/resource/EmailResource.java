package fi.vm.sade.ryhmasahkoposti.resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailSendId;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.service.EmailService;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;

@Component
@Path("email")
public class EmailResource {
    private final static Logger log = Logger.getLogger(fi.vm.sade.ryhmasahkoposti.resource.EmailResource.class.getName());	
    private final EmailService emailService;
    
	@Autowired    
    public EmailResource(EmailService emailService) {
        this.emailService = emailService;
    }
	@Autowired
	private GroupEmailReportingService sendDbService;

	
	

	@POST
	@Consumes("application/json")
	@Produces("application/json")	
	@Path("sendGroupEmail")
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
    
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("sendEmailStatus")
	public SendingStatusDTO sendEmailStatus(String sendId) {
		log.log(Level.INFO, "sendEmailStatus called with ID: " + sendId + ".");

		return sendDbService.getSendingStatus(Long.valueOf(sendId));
    }
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("sendResult")
	public ReportedMessageDTO sendResult(String sendId) {
		log.log(Level.INFO, "sendResult called with ID: " + sendId + ".");

		return sendDbService.getReportedMessage(Long.valueOf(sendId));
    }
		
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("sendEmail")
	public EmailResponse sendEmail(EmailMessage input) {
		EmailResponse response = emailService.sendEmail(input); 
		return response;
    }
    
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("sendEmails")
    public List<EmailResponse> sendEmails(List<EmailMessage> input) { 
    	List<EmailResponse> responses = new ArrayList<EmailResponse>();		
		for (EmailMessage email : input) {			
			EmailResponse resp = emailService.sendEmail(email);
	    	responses.add(resp);			
		}
		return responses;
    }
    
//	private String getDeliverycode() {
//		String timeStamp = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
//		long epoch = System.currentTimeMillis(); // /1000;
//
//		return timeStamp + "_" + Long.toString(epoch);
//	}
    
//	// save uploaded file to new location
//	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
// 
//		try {
//			OutputStream out = new FileOutputStream(new File(
//					uploadedFileLocation));
//			int read = 0;
//			byte[] bytes = new byte[1024];
// 
//			out = new FileOutputStream(new File(uploadedFileLocation));
//			while ((read = uploadedInputStream.read(bytes)) != -1) {
//				out.write(bytes, 0, read);
//			}
//			out.flush();
//			out.close();
//		} catch (IOException e) { 
//			e.printStackTrace();
//		} 
//	}
	
	
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    @Path("addAttachment")
    public AttachmentResponse addAttachment(@Context HttpServletRequest request, @Context HttpServletResponse response) 
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
        return result;
    }
    
    public AttachmentResponse storeAttachment(FileItem item) throws Exception {
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
