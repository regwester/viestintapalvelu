package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageRecipientAttachment;

/**
 * Rajapinta ryhmäsähköpostin raportoitavien liitteiden tietokantakäsittelyä varten
 * 
 * @author vehei1
 *
 */
public interface ReportedAttachmentDAO extends JpaDAO<ReportedAttachment, Long> {
	/**
	 * Tallentaa raportoitavat liitteet
	 * 
	 * @param liitteet Lista raportoitavia liitteitä
	 * @return Lista raportoitavien liitteiden avaimia
	 */
	List<Long> saveReportedAttachments(List<ReportedAttachment> liitteet);

    void insert(ReportedMessageRecipientAttachment recipientAttachment);
}
