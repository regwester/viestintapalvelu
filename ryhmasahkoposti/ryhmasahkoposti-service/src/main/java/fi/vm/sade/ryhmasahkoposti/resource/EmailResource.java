package fi.vm.sade.ryhmasahkoposti.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataParam;
//import com.google.inject.Inject;
//import com.google.inject.Singleton;
//
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailResponse;
import fi.vm.sade.ryhmasahkoposti.service.EmailService;
//import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;

@Component
@Path("email")
public class EmailResource {
	
    private final EmailService emailService;
    
	@Autowired    
    public EmailResource(EmailService emailBuilder) {
        this.emailService = emailBuilder;
    }

	@POST
	@Consumes("application/json")
//	@Produces("application/json")
	@Path("sendGroupEmail")
//	public List<EmailResponse> sendGroupEmail(EmailData emailData) {
	public void sendGroupEmail(EmailData emailData) {
		EmailMessage  email = emailData.getEmail();

		// Getting footer with the first ones language code
	    email.setFooter(emailData.getHeaders().get(0).getLanguageCode());  
		
//		String deliveryCode = getDeliverycode();		

//    	List<EmailResponse> responses = new ArrayList<EmailResponse>();
		
		for (EmailRecipient header : emailData.getHeaders()) {
//			header.setDeliveryCode(deliveryCode); 
//			email.setHeader(header);
			
			System.err.println("Save to DB - " + header.getEmail());
			
			//EmailResponse resp = emailService.sendEmail(email);		
//	    	responses.add(resp);			
			
		}
		
//		return responses;
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
    
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	@Path("sendEmail")
//    public Response sendEmail(EmailMessage input, @Context HttpServletResponse response) {
//System.err.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//
//		String deliveryCode = getDeliverycode();
//		input.setDeliveryCode(deliveryCode);
//		
////		EmailResponse resp = emailBuilder.sendEmail(input);
//		resp = emailBuilder.sendEmail(input);
//    	
//        if (!"".equals(resp.getStatus())) { 
//        	return Response.status(200).entity(resp).build();        	
//        } else {
//        	return Response.serverError().build();        	
//        }
//    }

	
	
	
	
	
	
//	@POST
//	@Consumes("application/json")
//	@Produces("application/json")
//	@Path("sendEmails")
//    public Response sendEmails(List<EmailMessage> input, @Context HttpServletResponse response) { 
//		String deliveryCode = getDeliverycode();
//
//    	List<EmailResponse> responses = new ArrayList<EmailResponse>();
//		
//		for (EmailMessage email : input) {			
//			email.setDeliveryCode(deliveryCode);
//			EmailResponse resp = emailBuilder.sendEmail(email);
//	    	responses.add(resp);			
//		}
//		return Response.status(200).entity(responses).build();
//    }

	
	
//	@GET
//	@Produces("application/json")
//	public EmailResponse getRespond() {
//System.err.println("--------------------------------------------------------------------------------");
//	    return resp;
//	}
	

//    @GET
//    @Consumes("application/json")
//    @Produces("application/json")
//    @Path("email")
//    public Response email(EmailMessage input, @Context HttpServletResponse response) {
//System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//    	EmailResponse resp = new EmailResponse(input.getRecipient(), "KO", input.getSubject());
//    	
//    	return Response.status(200).entity(resp).build();
//    }
	
}
