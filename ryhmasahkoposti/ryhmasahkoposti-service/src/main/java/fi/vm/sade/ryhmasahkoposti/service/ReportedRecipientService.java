package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;
import java.util.Set;

import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

/**
 * Rajapinta raportoittavien viestin vastaanottajien liiketoimintalogiikkaa varten
 * 
 * @author vehei1
 *
 */
public interface ReportedRecipientService {
	/**
	 * Hakee kaikkien raportoittavien viestin vastaanottajien tiedot
	 * 
	 * @return Lista raportoittavien viestin vastaanottajien tietoja {@link ReportedRecipient}
	 */
	public List<ReportedRecipient> getReportedRecipients();

	/**
	 * Hakee raportoitavan viestin vastaanottajien tiedot, joille viesti on lähettämättä
	 * 
	 * @param vastaanottajienLukumaara Palautettavien vasttanottajien lukumaara
	 * @return Lista raportoitavan viestin vastaanottajien tietoja {@link ReportedRecipient}
	 */
	public List<ReportedRecipient> getUnhandledReportedRecipients(int listSize);

	/**
	 * Hakee raportoitavan viestin vastaanottajan tiedot
	 * 
	 * @param id Raportoitavan viestin vastaanottajan tunnus
	 * @return Raportoitavan viestin vastaanottajan tiedot {@link ReportedRecipient}
	 */
	public ReportedRecipient getReportedRecipient(Long id);
	
	/**
	 * Hakee raportoitavan viestin vastaanottajan tiedot viestin tunnuksella ja vastaanottajan sähköpostiosoitteella
	 * 
	 * @param messageID Raportoitavan viestin tunnus
	 * @param recipientEmail Viestin vastaanottajan sähköpostiosoite
	 * @return Raportoitavan viestin vastaanottajan tiedot {@link ReportedRecipient}
	 */
	public ReportedRecipient getReportedRecipient(Long messageID, String recipientEmail);
	
	/**
	 * Hakee viestin vastaanottajien kokonaislukumäärän
	 * 
	 * @param messageID Raportoitavan viestin tunnus
	 * @return Viestin vastaanottajien kokonaislukumäärä
	 */
	public Long getNumberOfRecipients(Long messageID);
	
	/**
	 * Hakee epäonnistuneiden tai onnistuneiden viestin vastaanottajien lukumäärän
	 *   
	 * @param messageID Raportoitavan viestin tunnus
	 * @param sendingSuccesful true, jos halutaan hakea onnistuneet viestin lähetykset, false haetaan epäonnistuneet
	 * @return Epäonnistuneiden tai onnistuneiden viestien vastaanottajien lukumäärä
	 */
	public Long getNumerOfReportedRecipients(Long messageID, boolean sendingSuccesful);	
		
	/**
	 * Päivittää raportoitavan viestin vastaanottajan tiedot 
	 * 
	 * @param raportoitavaVastaanottaja Raportoitavan viestin vastaanottajan tiedot
	 */
	public void updateReportedRecipient(ReportedRecipient reportedRecipient);

	/**
	 * Tallentaa raportoitavan viestin vastaanottajien tiedot 
	 * 
	 * @param raportoitavatVastaanottajat Lista raportoitavan viestin vastaanottajien tietoja
	 */
	public void saveReportedRecipients(Set<ReportedRecipient> reportedRecipients);
}
