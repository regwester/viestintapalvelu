package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
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
    public Long getNumberOfReportedMessages(String organizationOid) {
        return reportedMessageDAO.findNumberOfReportedMessages(organizationOid);
    }

    @Override
    public Long getNumberOfReportedMessages(ReportedMessageQueryDTO query) {
        return reportedMessageDAO.findNumberOfReportedMessage(query);
    }
	
	@Override
	public List<ReportedMessage> getReportedMessages(String organizationOid, PagingAndSortingDTO pagingAndSorting) {
	    return reportedMessageDAO.findByOrganizationOid(organizationOid, pagingAndSorting);
	}
	
    @Override
	public List<ReportedMessage> getReportedMessages(ReportedMessageQueryDTO query, 
	    PagingAndSortingDTO pagingAndSorting) {
        return reportedMessageDAO.findBySearchCriteria(query, pagingAndSorting);
	}
    
    @Override
	public List<ReportedMessage> getUserMessages(String senderOid, PagingAndSortingDTO pagingAndSorting) {
		return reportedMessageDAO.findBySenderOid(senderOid, pagingAndSorting);
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
