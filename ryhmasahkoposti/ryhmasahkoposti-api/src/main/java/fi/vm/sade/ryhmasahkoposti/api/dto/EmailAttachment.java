package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.File;
import java.io.IOException;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

public class EmailAttachment {
	String name;
	String contentType;
	String attachment;

	@SuppressWarnings("unused")
	private EmailAttachment() {

	}

	public EmailAttachment(String attachmentFile) {		
		name = attachmentFile;
		
        DataSource ds = new FileDataSource(attachmentFile);     	    
	    contentType = ds.getContentType();	    

		attachment = fileToEncodedStr(attachmentFile);        
	}
		
	public String getName() {
		return name;
	}

	public String getAttachment() {
		return attachment;
	}

	public String getContentType() {
		return contentType;
	}
	
	private String fileToEncodedStr(String attachmentFile) {
	    File file = new File(attachmentFile);
	    byte[] fileBytes;
		try {			
			fileBytes = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			fileBytes = "".getBytes();
			e.printStackTrace();
		}
	    
	    byte[] encoded = Base64.encodeBase64(fileBytes);	    		
	    return new String(encoded);
		
	}

	@Override
	public String toString() {
		return "EmailAttachment ["
				+ "name=" + name
				+ ", contentType=" + contentType 
				+ ", attachment=" + attachment.substring(0, 30) + "... ]";
	}
}


