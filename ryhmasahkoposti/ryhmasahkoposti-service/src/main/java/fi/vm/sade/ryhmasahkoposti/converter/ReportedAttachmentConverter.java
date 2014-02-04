package fi.vm.sade.ryhmasahkoposti.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;

@Component
public class ReportedAttachmentConverter {

	public static ReportedAttachment convert(FileItem fileItem) throws IOException {
		ReportedAttachment liite = new ReportedAttachment();
		
		liite.setAttachmentName(fileItem.getName());
		liite.setContentType(fileItem.getContentType());
		
		byte[] zippedAttachment = zipAttachment(fileItem.getName(), fileItem.get());

		liite.setAttachment(zippedAttachment);
		liite.setTimkestamp(new Date());
		
		return liite;
	}
	
	private static byte[] zipAttachment(String attachmentName, byte[] attachment) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipStream = new ZipOutputStream(out);
        
        zipStream.putNextEntry(new ZipEntry(attachmentName));
        zipStream.write(attachment);
        zipStream.closeEntry();

        zipStream.close();
        return out.toByteArray();
    }
}
