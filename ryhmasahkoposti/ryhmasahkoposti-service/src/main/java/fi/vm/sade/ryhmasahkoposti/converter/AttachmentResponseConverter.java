package fi.vm.sade.ryhmasahkoposti.converter;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;

@Component
public class AttachmentResponseConverter {

    public AttachmentResponse convert(Long id, ReportedAttachment reportedAttachment) {
        AttachmentResponse attachmentResponse = new AttachmentResponse();
        
        attachmentResponse.setUuid(id.toString());
        attachmentResponse.setFileName(reportedAttachment.getAttachmentName());
        attachmentResponse.setContentType(reportedAttachment.getContentType());
        attachmentResponse.setFileSize(reportedAttachment.getAttachment().length);
        
        return attachmentResponse;
    }
}
