package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedAttachmentDAO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageAttachmentDAO;
import fi.vm.sade.ryhmasahkoposti.model.*;
import fi.vm.sade.ryhmasahkoposti.service.ReportedMessageAttachmentService;

@Service
public class ReportedMessageAttachmentServiceImpl implements ReportedMessageAttachmentService {
	private ReportedMessageAttachmentDAO reportedMessageAttachmentDAO;
    private ReportedAttachmentDAO reportedAttachmentDAO;

	@Autowired
	public ReportedMessageAttachmentServiceImpl(ReportedMessageAttachmentDAO reportedMessageAttachmentDAO,
                                                ReportedAttachmentDAO reportedAttachmentDAO) {
		this.reportedMessageAttachmentDAO = reportedMessageAttachmentDAO;
        this.reportedAttachmentDAO = reportedAttachmentDAO;
	}

	@Override
    @Transactional
	public void saveReportedMessageAttachments(ReportedMessage reportedMessage,	 List<ReportedAttachment> reportedAttachments) {
		for (ReportedAttachment reportedAttachment : reportedAttachments) {
			ReportedMessageAttachment reportedMessageAttachment = new ReportedMessageAttachment();
			reportedMessageAttachment.setReportedMessage(reportedMessage);
			reportedMessageAttachment.setReportedAttachmentID(reportedAttachment.getId());
			reportedMessageAttachment.setTimestamp(new Date());
			
			reportedMessageAttachmentDAO.insert(reportedMessageAttachment);
		}
	}

    @Override
    @Transactional
    public void saveReportedRecipientAttachments(ReportedRecipient emailRecipient, List<ReportedAttachment> reportedAttachments) {
        for (ReportedAttachment reportedAttachment : reportedAttachments) {
            ReportedMessageRecipientAttachment recipientAttachment = new ReportedMessageRecipientAttachment();
            recipientAttachment.setRecipient(emailRecipient);
            recipientAttachment.setAttachment(reportedAttachment);
            recipientAttachment.setTimestamp(new Date());

            reportedAttachmentDAO.insert(recipientAttachment);
        }
    }

}
