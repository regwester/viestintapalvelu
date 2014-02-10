package fi.vm.sade.ryhmasahkoposti.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

/**
 * Rajapinta raportoittavien viestin vastaanottajien liiketoimintalogiikkaa varten
 * 
 * @author vehei1
 *
 */
public interface ReportedRecipientService {
	/**
	 * Hakee lähetyksen tilannetietojen vastaanottajien lukumäärät
	 * 
	 * @param messageID Sanoman tunnus
	 * @return Lähetyksen tilannetiedot täydennettynä vastaanottajien lukumäärillä
	 */
	public SendingStatusDTO getSendingStatusOfRecipients(Long messageID);
	
	/**
	 * Hakee kaikkien raportoittavien viestin vastaanottajien tiedot
	 * 
	 * @return Lista raportoittavien viestin vastaanottajien tietoja {@link ReportedRecipient}
	 */
	public List<ReportedRecipient> getReportedRecipients();

	/**
	 * Hakee ryhmäsähköpostin viimeisimmän lähetysajankohdan vastaanottajien tiedoista  
	 * @param messageID vistin tunnus
	 * @return Viimeisin lähetysajankohta tai null, jos mitään ei ole vielä lähetetty
	 */
	public Date getLatestReportedRecipientsSendingEnded(Long messageID);

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
	 * Hakee raportoitavan viestin vastaanottajien tiedot, joille viesti on lähettämättä
	 * 
	 * @param vastaanottajienLukumaara Palautettavien vasttanottajien lukumaara
	 * @return Lista raportoitavan viestin vastaanottajien tietoja {@link ReportedRecipient}
	 */
	public List<ReportedRecipient> getUnhandledReportedRecipients(int listSize);

	/**
	 * Tallentaa raportoitavan viestin vastaanottajien tiedot 
	 * 
	 * @param raportoitavatVastaanottajat Lista raportoitavan viestin vastaanottajien tietoja
	 */
	public void saveReportedRecipients(Set<ReportedRecipient> reportedRecipients);

	/**
	 * Päivittää raportoitavan viestin vastaanottajan tiedot 
	 * 
	 * @param raportoitavaVastaanottaja Raportoitavan viestin vastaanottajan tiedot
	 */
	public void updateReportedRecipient(ReportedRecipient reportedRecipient);
}
