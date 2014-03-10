package fi.vm.sade.ryhmasahkoposti.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.stereotype.Component;

@Component
public class ZipUtil {
	public static byte[] zip(String attachmentName, byte[] attachment) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(attachment.length);
		
		Deflater deflater = new Deflater();  
		deflater.setInput(attachment);  
		deflater.finish(); 

		byte[] buffer = new byte[1024];   
		
		while (!deflater.finished()) {  
			int count = deflater.deflate(buffer);  
			outputStream.write(buffer, 0, count);   
		}  
		
		outputStream.close();    
		return outputStream.toByteArray();  
    }
	
	public static byte[] unzip(String attachmentName, byte[] attachment) throws IOException, DataFormatException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(attachment.length);
		
		Inflater inflater = new Inflater();   
		inflater.setInput(attachment);  
		   
		byte[] buffer = new byte[1024];  
		   
		while (!inflater.finished()) {  
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		
		outputStream.close();  
		return outputStream.toByteArray();  	
	}
}
