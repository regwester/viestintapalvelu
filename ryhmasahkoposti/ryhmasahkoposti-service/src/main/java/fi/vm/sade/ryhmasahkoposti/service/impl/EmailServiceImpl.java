package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.IOException;
import java.util.logging.Logger;



//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.DocumentException;

//import com.google.gson.Gson;

import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailResponse;
import fi.vm.sade.ryhmasahkoposti.service.EmailService;


@Service
public class EmailServiceImpl implements EmailService {

	public EmailResponse sendEmail(EmailMessage email) {
	    final Logger log = Logger.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailServiceImpl.class.getName());
	    
//	    email.setFooter(email.getHeader().getLanguageCode());
	    log.info("Send email info: " + email.toString());
	    
	    boolean sendStatus = EmailUtil.sendMail(email, "tähän vastaanottajan osoite jotenkin");	   
	    String      status = (sendStatus ? "OK" : "Error");
//	    email.setSendStatus(status);  LAITETAAN KANTAAN.
	
//      MITEN TÄMÄ MUUTTUU	    
//    	EmailResponse resp = new EmailResponse(email.getHeader(), status, email.getSubject(), Integer.toString(email.getAttachments().size()));					
    	EmailResponse resp = new EmailResponse(status, email.getSubject() );					
    	log.info("Email  response: " + resp.toString());
    	return resp;
	}
	
//    public static HttpResponse get(Object json, String url) throws IOException, DocumentException {
//        DefaultHttpClient client = new DefaultHttpClient();
//        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
//        HttpPost post = new HttpPost(url);
//        post.setHeader("Content-Type", "application/json;charset=utf-8");
//        post.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(json), ContentType.APPLICATION_JSON));
//        HttpResponse response = client.execute(post);
//        
//        return response;
//    }
    
  
//    public static EmailResponse getEmailResponse (HttpResponse response) throws IOException {
//        HttpEntity entity = response.getEntity();
//        String responseStr = ""; 
//        if (entity != null) {
//        	responseStr = EntityUtils.toString(entity);
//        } else {
//        	return new EmailResponse();
//        }
//
//        try {
//	        Gson gson = new Gson();
//	        return gson.fromJson(responseStr, EmailResponse.class);
//		} catch (Exception e) {
//			return new EmailResponse(responseStr,"");
//		}                
//    }
//	
//    public static EmailResponse[] getEmailResponses (HttpResponse response) throws IOException {
//        HttpEntity entity = response.getEntity();
//        String responseStr = ""; 
//        if (entity != null) {
//        	responseStr = EntityUtils.toString(entity);
//        } else {
//        	return new EmailResponse[]{};
//        }
//
//        try {
//	        Gson gson = new Gson();
//	        return gson.fromJson(responseStr, EmailResponse[].class);
//		} catch (Exception e) {
//			return new EmailResponse[] {new EmailResponse(responseStr,"")};
//		}                
//    }

}
