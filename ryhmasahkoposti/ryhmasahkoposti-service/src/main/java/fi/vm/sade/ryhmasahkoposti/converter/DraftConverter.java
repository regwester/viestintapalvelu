package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.Draft;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.model.DraftModel;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;

@Component
public class DraftConverter {

    @Autowired
    private ReportedAttachmentConverter reportedAttachmentConverter;
    
    public DraftModel convert(Draft draft) {
        Set<EmailAttachment> eaSet = draft.getAttachments();
        Set<ReportedAttachment> raSet = new HashSet<ReportedAttachment>();
        for(EmailAttachment a : eaSet) {
            raSet.add(reportedAttachmentConverter.convert(a));
        }
        
        return new DraftModel.Builder()
        .replyTo(draft.getReplyTo())
        .subject(draft.getSubject())
        .userOid(draft.getUserOid())
        .body(draft.getBody())
        .isHtml(draft.isHtml())
        .createDate(draft.getCreateDate())
        .setAttachments(raSet)
        .build();
    }
    
    public Draft convert(DraftModel draftModel) {
        Set<ReportedAttachment> raSet = draftModel.getAttachments();
        Set<EmailAttachment> eaSet = new HashSet<EmailAttachment>();
        for(ReportedAttachment a : raSet) {
            eaSet.add(reportedAttachmentConverter.convert(a));
        }
        
        return new Draft.Builder()
        .replyTo(draftModel.getReplyTo())
        .subject(draftModel.getSubject())
        .userOid(draftModel.getUserOid())
        .body(draftModel.getBody())
        .isHtml(draftModel.isHtml())
        .createDate(draftModel.getCreateDate())
        .setAttachments(eaSet)
        .build();
    }
    
}
