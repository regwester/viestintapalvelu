package fi.vm.sade.ryhmasahkoposti.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
//import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
//import com.sun.jersey.multipart.FormDataParam;
//import com.google.inject.Inject;
//import com.google.inject.Singleton;
//
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailSendId;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenTilanneDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.service.EmailService;
import fi.vm.sade.ryhmasahkoposti.service.RyhmasahkopostinRaportointiService;

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
	private RyhmasahkopostinRaportointiService sendDbService;

	
	

	@POST
	@Consumes("application/json")
	@Produces("application/json")	
//	@Produces("text/plain")
	@Path("sendGroupEmail")
//	public List<EmailResponse> sendGroupEmail(EmailData emailData) {
	public EmailSendId sendGroupEmail(EmailData emailData) {
		EmailMessage email = emailData.getEmail();		
	    email.setFooter(emailData.getRecipient().get(0).getLanguageCode()); // Setting footer with the first ones language code  
	    	    
	    LahetyksenAloitusDTO emailInfo = new LahetyksenAloitusDTO();	    
	    copyEmailInfo(email, emailInfo);
		
	    List<LahetettyVastaanottajalleDTO> recipients = new ArrayList<LahetettyVastaanottajalleDTO>(); 	    
	    
		for (EmailRecipient header : emailData.getRecipient()) {
			log.log(Level.INFO, "Adding " + header.getEmail() + " to be sending list.");
			
			LahetettyVastaanottajalleDTO recipient = new LahetettyVastaanottajalleDTO();
			copyRecipientInfo(header, recipient);
			
			recipients.add(recipient);
		}
		emailInfo.setVastaanottajat(recipients);
		
		String sendId = "";
		try {
			sendId = Long.toString( sendDbService.raportoiLahetyksenAloitus(emailInfo) );
			log.log(Level.INFO, "DB index is " + sendId);
			
		} catch (IOException e) {	
			log.log(Level.SEVERE, "Problems in writing send data info to DB, "+ e.getMessage());
		}
		return new EmailSendId(sendId);
//		return responses;
    }

	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("sendEmailStatus")
	public LahetyksenTilanneDTO sendEmailStatus(String sendId) {
		log.log(Level.INFO, "sendEmailStatus called with ID: " + sendId + ".");

		return sendDbService.haeLahetyksenTulos(Long.valueOf(sendId));
    }
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("sendResult")
	public RaportoitavaViestiDTO sendResult(String sendId) {
		log.log(Level.INFO, "sendResult called with ID: " + sendId + ".");

		/// TMÄM ON VÄÄRÄ KUTSU
		return sendDbService.haeRaportoitavaViesti(Long.valueOf(sendId));
    }
	
	
	private void copyRecipientInfo(EmailRecipient header, LahetettyVastaanottajalleDTO recipient) {
		recipient.setLahetysalkoi(new Date());
		recipient.setVastaanottajaOid(header.getOid());
		recipient.setVastaanottajanOidTyyppi(header.getOidType());
		recipient.setVastaanottajanSahkoposti(header.getEmail());
		recipient.setKielikoodi(header.getLanguageCode());
	}

	private void copyEmailInfo(EmailMessage email, LahetyksenAloitusDTO emailInfo) {
		emailInfo.setProsessi(email.getCallingProcess());
	    emailInfo.setLahettajanSahkopostiosoite(email.getOwnerEmail());
	    emailInfo.setVastauksensaajaOid(email.getSenderEmail());
	    emailInfo.setVastauksenSaajanOidTyyppi(email.getSenderOidType());
	    emailInfo.setVastauksensaajanSahkoposti(email.getSenderEmail());
	    emailInfo.setAihe(email.getSubject());
	    emailInfo.setViesti(email.getBody()+email.getFooter());
	    emailInfo.setHtmlViesti(email.isHtml());
	    emailInfo.setMerkisto(email.getCharset());
	}
	
	
		
//	@POST
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Path("loadEmailAttachment")
//	public String loadEmailAttachment(	@FormDataParam("attachment") InputStream uploadedInputStream, 
//										@FormDataParam("attachment") FormDataContentDisposition fileDetail) {
//	 
//System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");		
//		String uploadedFileLocation = "d://" + fileDetail.getFileName();
//	 
//		// save it
//		writeToFile(uploadedInputStream, uploadedFileLocation);
//	 
//		String output = "File uploaded to : " + uploadedFileLocation;
//		return output;
////		return Response.status(200).entity(output).build();
//	}
    
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("sendEmail")
	public EmailResponse sendEmail(EmailMessage input) {
//		String deliveryCode = getDeliverycode();
//		input.setDeliveryCode(deliveryCode);
		
		EmailResponse response = emailService.sendEmail(input); 
		return response;
    }
    
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("sendEmails")
    public List<EmailResponse> sendEmails(List<EmailMessage> input) { 
		String deliveryCode = getDeliverycode();

    	List<EmailResponse> responses = new ArrayList<EmailResponse>();
		
		for (EmailMessage email : input) {			
//			email.setDeliveryCode(deliveryCode);
			EmailResponse resp = emailService.sendEmail(email);
	    	responses.add(resp);			
		}
		return responses;
    }
    
	private String getDeliverycode() {
		String timeStamp = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
		long epoch = System.currentTimeMillis(); // /1000;

		return timeStamp + "_" + Long.toString(epoch);
	}
    
    
  
	
	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream,
		String uploadedFileLocation) {
 
		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
 
			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
 
			e.printStackTrace();
		}
 
	}	
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    @Path("addAttachment")
    public AttachmentResponse addAttachment(@Context HttpServletRequest request,
            @Context HttpServletResponse response) throws IOException,
            URISyntaxException, ServletException {

        System.out.println("Adding attachment "+request.getMethod());
        
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
        System.out.println(result);
        return result;
    }
    
    public AttachmentResponse storeAttachment(FileItem item) throws Exception {
        AttachmentResponse result = new AttachmentResponse();
        
        if (!item.isFormField()) {
            Long id = sendDbService.tallennaLiite(item);
        	
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
