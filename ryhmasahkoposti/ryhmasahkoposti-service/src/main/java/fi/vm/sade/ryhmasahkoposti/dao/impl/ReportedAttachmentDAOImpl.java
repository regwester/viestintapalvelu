package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedAttachmentDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;

@Repository
public class ReportedAttachmentDAOImpl extends AbstractJpaDAOImpl<ReportedAttachment, Long> implements ReportedAttachmentDAO {

	@Override
	public List<Long> saveReportedAttachments(List<ReportedAttachment> attachments) {
		// TODO Auto-generated method stub
		return null;
	}
}
