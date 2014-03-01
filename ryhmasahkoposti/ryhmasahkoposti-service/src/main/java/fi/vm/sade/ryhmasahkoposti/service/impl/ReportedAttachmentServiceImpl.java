package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedAttachmentDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageAttachment;
import fi.vm.sade.ryhmasahkoposti.service.ReportedAttachmentService;

@Service
public class ReportedAttachmentServiceImpl implements ReportedAttachmentService {
	private ReportedAttachmentDAO reportedAttachmentDAO;
    
	@Autowired
    public ReportedAttachmentServiceImpl(ReportedAttachmentDAO reportedAttachmentDAO) {
		this.reportedAttachmentDAO = reportedAttachmentDAO;
	}   

	@Override
	public List<ReportedAttachment> getReportedAttachments(Set<ReportedMessageAttachment> reportedMessageAttachments) {
		List<ReportedAttachment> reportedAttachments = new ArrayList<ReportedAttachment>() ;
		
		for (ReportedMessageAttachment reportedMessageAttachment : reportedMessageAttachments) {
			ReportedAttachment liite = reportedAttachmentDAO.read(reportedMessageAttachment.getReportedAttachmentID());
			reportedAttachments.add(liite);
		}
		
		return reportedAttachments;	
	}

	@Override
	public List<ReportedAttachment> getReportedAttachments(List<AttachmentResponse> attachmentResponses) {
		List<ReportedAttachment> reportedAttachments = new ArrayList<ReportedAttachment>();
		
		for (AttachmentResponse attachmentResponse : attachmentResponses) {
			Long liitteenID = new Long(attachmentResponse.getUuid());
			ReportedAttachment reportedAttachment = reportedAttachmentDAO.read(liitteenID);
			reportedAttachments.add(reportedAttachment);
		}
		
		return reportedAttachments;
	}

	@Override
	public Long saveReportedAttachment(ReportedAttachment reportedAttachment) {
		reportedAttachment = reportedAttachmentDAO.insert(reportedAttachment);
		return reportedAttachment.getId();
	}
}
