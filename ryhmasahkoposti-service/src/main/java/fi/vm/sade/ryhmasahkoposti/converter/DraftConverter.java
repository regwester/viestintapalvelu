package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.Draft;
import fi.vm.sade.ryhmasahkoposti.model.DraftModel;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.service.ReportedAttachmentService;

@Component
public class DraftConverter {
    
    @Autowired
    private ReportedAttachmentService reportedAttachmentService;
    
    public DraftModel convert(Draft draft) {
        List<ReportedAttachment> raList = reportedAttachmentService.getReportedAttachments(draft.getAttachInfo());
        Set<ReportedAttachment> raSet = new HashSet<ReportedAttachment>(raList);
        return new DraftModel.Builder()
        .replyTo(draft.getReplyTo())
        .sender(draft.getSender())
        .from(draft.getFrom())
        .subject(draft.getSubject())
        .userOid(draft.getOrganizationOid())
        .body(draft.getBody())
        .isHtml(draft.isHtml())
        .createDate(draft.getCreateDate().toDate())
        .setAttachments(raSet)
        .build();
    }
    
    public Draft convert(DraftModel draftModel) {
        Set<ReportedAttachment> raSet = draftModel.getAttachments();
        List<AttachmentResponse> eaList = new ArrayList<AttachmentResponse>();
        for(ReportedAttachment a : raSet) {
            AttachmentResponse result = new AttachmentResponse();
            result.setUuid(a.getId().toString());
            result.setContentType(a.getContentType());
            result.setFileName(a.getAttachmentName());
            result.setFileSize(a.getAttachment().length);
            eaList.add(result);
        }
        
        Date d = draftModel.getCreateDate();
        // Need to check that d is not null, so DateTime doesn't initialize into current time
        // DateTime(null) is the same as DateTime()
        DateTime dt = (d == null) ? null : new DateTime(d);
        
        String body = draftModel.getBody();
        if(draftModel.isHtml()) {
            body = StringEscapeUtils.unescapeHtml(body);
        }
        
        return new Draft.Builder()
        .replyTo(draftModel.getReplyTo())
        .subject(draftModel.getSubject())
        .organizationOid(draftModel.getUserOid())
        .body(body)
        .from(draftModel.getFrom())
        .sender(draftModel.getSender())
        .isHtml(draftModel.isHtml())
        .createDate(dt)
        .setAttachments(eaList)
        .build();
    }
    
}
