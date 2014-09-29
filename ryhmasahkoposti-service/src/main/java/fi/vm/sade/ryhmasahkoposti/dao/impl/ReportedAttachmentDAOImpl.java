package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedAttachmentDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageRecipientAttachment;

@Repository
public class ReportedAttachmentDAOImpl extends AbstractJpaDAOImpl<ReportedAttachment, Long> implements ReportedAttachmentDAO {

	@Override
	public List<Long> saveReportedAttachments(List<ReportedAttachment> attachments) {
        List<Long> ids = new ArrayList<Long>();
        for (ReportedAttachment attachment : attachments) {
            getEntityManager().persist(attachment);
            ids.add(attachment.getId());
        }
        getEntityManager().flush();
		return ids;
	}

    @Override
    public void insert(ReportedMessageRecipientAttachment recipientAttachment) {
        getEntityManager().persist(recipientAttachment);
        getEntityManager().flush();
    }
}
