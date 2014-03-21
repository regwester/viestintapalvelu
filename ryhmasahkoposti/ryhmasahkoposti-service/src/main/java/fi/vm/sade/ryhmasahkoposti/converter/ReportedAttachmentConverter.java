package fi.vm.sade.ryhmasahkoposti.converter;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;

@Component
public class ReportedAttachmentConverter {

	public static ReportedAttachment convert(FileItem fileItem) throws IOException {
		ReportedAttachment liite = new ReportedAttachment();
		
		String filename = fileItem.getName();
		liite.setAttachmentName(FilenameUtils.getName(filename));
		liite.setContentType(fileItem.getContentType());
		
//		byte[] zippedAttachment = ZipUtil.zip(fileItem.getName(), fileItem.get());
//		liite.setAttachment(zippedAttachment);
		
		liite.setAttachment(fileItem.get());
		liite.setTimkestamp(new Date());
		
		return liite;
	}
}
