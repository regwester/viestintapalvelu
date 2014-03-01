package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageAttachmentDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageAttachment;
import fi.vm.sade.ryhmasahkoposti.service.ReportedMessageAttachmentService;

@Service
public class ReportedMessageAttachmentServiceImpl implements ReportedMessageAttachmentService {
	private ReportedMessageAttachmentDAO reportedMessageAttachmentDAO;

	@Autowired
	public ReportedMessageAttachmentServiceImpl(ReportedMessageAttachmentDAO reportedMessageAttachmentDAO) {
		this.reportedMessageAttachmentDAO = reportedMessageAttachmentDAO;
	}
	
	@Override
	public List<ReportedMessageAttachment> getReportedMessageAttachments(Long messageID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveReportedMessageAttachments(ReportedMessage reportedMessage,	
		List<ReportedAttachment> reportedAttachments) {		
		for (ReportedAttachment reportedAttachment : reportedAttachments) {
			ReportedMessageAttachment reportedMessageAttachment = new ReportedMessageAttachment();
			reportedMessageAttachment.setReportedMessage(reportedMessage);
			reportedMessageAttachment.setReportedAttachmentID(reportedAttachment.getId());
			reportedMessageAttachment.setTimestamp(new Date());
			
			reportedMessageAttachmentDAO.insert(reportedMessageAttachment);
		}
	}

}
