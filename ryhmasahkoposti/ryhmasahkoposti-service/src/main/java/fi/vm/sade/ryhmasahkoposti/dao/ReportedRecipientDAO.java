package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

/**
 * Rajapinta raportoitavien ryhmäsähköpostiviestin vastaanottajien tietokantakäsittelyyn
 * 
 * @author vehei1
 *
 */
public interface ReportedRecipientDAO extends JpaDAO<ReportedRecipient, Long> {
	/**
	 * Hakee vastaanottajan tiedot viestintunnuksella ja sähköpostiosoitteella
	 * 
	 * @param messageID Viestintunnus
	 * @param recipientEmail Vastaanottajansähköpostiosoite
	 * @return Raportoitavan vastaanottajan tiedot
	 */
	public ReportedRecipient findByMessageIdAndRecipientEmail(Long messageID, String recipientEmail);

	/**
	 * Hakee vastaanottajan tiedot vastaanottajantunnisteella
	 * 
	 * @param recipientID Vastaanottajantunnus
	 * @return Raportoitavan vastaanottajan tiedot
	 */
	public ReportedRecipient findByRecipientID(Long recipientID);
	
	/**
	 * Hakee raportoitavat vastaanottajat, joille viestiä ei ole lähetetty 
	 *  
	 * @return Lista raportoitavan vastaanottajien tietoja
	 */	
	public List<ReportedRecipient> findUnhandled();
	
	/**
	 * Hakee vastaanottajien lukumäärän viestintunnuksella
	 * 
	 * @param messageID Viestintunnus
	 * @return Viestin vastaanottajien lukumäärän
	 */
	public Long findNumberOfRecipientsByMessageID(Long messageID);
	
	/**
	 * Hakee vastaanottajien lukumäärän viestintunnuksella, joille viestin lähetys on onnistunut tai epäonnistunut 
	 * 
	 * @param messageID Viestintunnus
	 * @param sendingSuccesful true, jos halutaan hakea onnistuneet lähetykset, muuten false
	 * @return Viestin vastaanottajien lukumäärän
	 */
	public Long findNumberOfRecipientsByMessageIDAndSendingSuccesful(Long messageID, boolean sendingSuccesful);
}
