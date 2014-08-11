package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.Date;
import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

/**
 * Rajapinta raportoitavien ryhmäsähköpostiviestin vastaanottajien tietokantakäsittelyyn
 * 
 * @author vehei1
 *
 */
public interface ReportedRecipientDAO extends JpaDAO<ReportedRecipient, Long> {
    /**
     * Hakee vastaanottajien tiedot viestintunnuksella. Palauttaa halutun määrän lajiteltuna.
     * 
     * @param messageID Viestintunnus
     * @param pagingAndSorting Sivutus- ja lajittelutiedot
     * @return Lista raportoitavien vastaanottajien tietoja
     */
    public List<ReportedRecipient> findByMessageId(Long messageID, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee vastaanottajien tiedot viestintunnuksella ja tila lähetys epäonnistui. Palauttaa halutun määrän lajiteltuna.
     * 
     * @param messageID Viestintunnus
     * @param pagingAndSorting Sivutus- ja lajittelutiedot
     * @return Lista raportoitavien vastaanottajien tietoja, joille lähetys on epäonnistunut
     */	
    public List<ReportedRecipient> findByMessageIdAndSendingUnsuccesful(Long messageID, 
	    PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee vastaanottajan tiedot vastaanottajantunnisteella
     * 
     * @param recipientID Vastaanottajantunnus
     * @return Raportoitavan vastaanottajan tiedot
     */
    public ReportedRecipient findByRecipientID(Long recipientID);

    /**
     * Hakee lähettävästä ryhmäsähköpostista viimeiseksi lähetetyn
     * 
     * @param messageID Sanoman tunnus
     * @return Viimeiseksi lähetetyn ajanhetki
     */
    public Date findMaxValueOfSendingEndedByMessageID(Long messageID);

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

    /**
     * Hakee raportoitavat vastaanottajat, joille viestiä ei ole lähetetty 
     *  
     * @return Lista raportoitavan vastaanottajien tietoja
     */	
    public List<ReportedRecipient> findUnhandled();


}
