package fi.vm.sade.ryhmasahkoposti.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
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
	 * Hakee ryhmäsähköpostin viimeisimmän lähetysajankohdan vastaanottajien tiedoista 
	 *  
	 * @param messageID vistin tunnus
	 * @return Viimeisin lähetysajankohta tai null, jos mitään ei ole vielä lähetetty
	 */
	public Date getLatestReportedRecipientsSendingEndedDate(Long messageID);

	/**
	 * Hakee ryhmäsähköpostin vastaanottajien lukumäärän, joiden lähetys on epäonnistunut 
	 * @param messageID
	 * @return
	 */
	public Long getNumberOfSendingFailed(Long messageID);
	/**
	 * Hakee raportoitavan viestin vastaanottajan tiedot
	 * 
	 * @param id Raportoitavan viestin vastaanottajan tunnus
	 * @return Raportoitavan viestin vastaanottajan tiedot {@link ReportedRecipient}
	 */
	public ReportedRecipient getReportedRecipient(Long id);

	/**
	 * Hakee raportoitavan viestin vastaanottajien tiedot viestin tunnuksella. Palauttaa halutun määrän vastaanottajia
	 * 
	 * @param messageID Raportoitavan viestin tunnus
	 * @param pagingAndSorting Sivitus ja lajittelutiedot
	 * @return Lista raportoitavan viestin vastaanottajien tietoja {@link ReportedRecipient}
	 */
	public List<ReportedRecipient> getReportedRecipients(Long messageID, PagingAndSortingDTO pagingAndSorting);

	/**
     * Hakee raportoitavan viestin vastaanottajien tiedot viestin tunnuksella, joille lähetys on epäonnistunut
     * 
     * @param messageID Raportoitavan viestin tunnus
     * @param pagingAndSorting Sivitus ja lajittelutiedot
     * @return Lista raportoitavan viestin vastaanottajien tietoja, joille lähetys on epäonnistunut {@link ReportedRecipient}
     */
    public List<ReportedRecipient> getReportedRecipientsByStatusSendingUnsuccesful(Long messageID, 
        PagingAndSortingDTO pagingAndSorting);
        
	/**
	 * Hakee kaikkien raportoittavien viestin vastaanottajien tiedot
	 * 
	 * @return Lista raportoittavien viestin vastaanottajien tietoja {@link ReportedRecipient}
	 */
	public List<ReportedRecipient> getReportedRecipients();
	
	/**
	 * Hakee lähetyksen tilannetietojen vastaanottajien lukumäärät
	 * 
	 * @param messageID Sanoman tunnus
	 * @return Lähetyksen tilannetiedot täydennettynä vastaanottajien lukumäärillä
	 */
	public SendingStatusDTO getSendingStatusOfRecipients(Long messageID);
	
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
