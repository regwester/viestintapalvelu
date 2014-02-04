package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;

/**
 * Rajapinta raportoitavien ryhmäsähköpostiviestien tietokantakäsittelyyn
 * 
 * @author vehei1
 *
 */
public interface ReportedMessageDAO extends JpaDAO<ReportedMessage, Long> {
	/**
	 * Hakee hakuparametrien mukaiset raportoitavat viestit
	 *  
	 * @param query Hakuparametrit
	 * @return Lista raportoituja ryhmäsähköpostiviesteja
	 */
	public List<ReportedMessage> findBySearchCriteria(EmailMessageQueryDTO query);

	/**
	 * Hakee raportoitavat viestit lahettajien oid-tunnuksen perusteella
	 * 
	 * @param senderOids Lista lähettäjien oid-tunnuksia
	 * @return Lista ryhmäsähköpostin raportoitavia viestejä
	 */
	public List<ReportedMessage> findSendersReportedMessages(List<String> senderOids);
	
	/**
	 * Hakee raportoitujen ryhmäsähköpostien lukumäärän
	 * 
	 * @return Raportoitujen ryhmäsähköpostien lukumäärä
	 */
	public Long findNumberOfReportedMessage();
}
