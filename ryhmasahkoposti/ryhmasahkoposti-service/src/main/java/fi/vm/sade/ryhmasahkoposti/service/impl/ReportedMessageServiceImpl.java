package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.service.ReportedMessageService;

@Service
public class ReportedMessageServiceImpl implements ReportedMessageService {
	private ReportedMessageDAO reportedMessageDAO;
	
	@Autowired
	public ReportedMessageServiceImpl(ReportedMessageDAO reportedMessageDAO) {
		this.reportedMessageDAO = reportedMessageDAO;
	}

	@Override
	public List<ReportedMessage> getReportedMessages() {
		return reportedMessageDAO.findAll();
	}

	@Override
	public List<ReportedMessage> getReportedMessages(EmailMessageQueryDTO query) {
		return reportedMessageDAO.findBySearchCriteria(query);
	}

	@Override
	public ReportedMessage getReportedMessage(Long id) {
		return reportedMessageDAO.read(id);
	}

	@Override
	public void updateReportedMessage(ReportedMessage reportedMessage) {
		reportedMessageDAO.update(reportedMessage);
	}

	@Override
	public ReportedMessage saveReportedMessage(ReportedMessage reportedMessage) {
		return reportedMessageDAO.insert(reportedMessage);
	}
	
}
