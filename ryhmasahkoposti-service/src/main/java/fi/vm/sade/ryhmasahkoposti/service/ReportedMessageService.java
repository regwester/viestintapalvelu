package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;

public interface ReportedMessageService {
    /**
     * Hakee hakuparameterejä vastaavien raportoitavien ryhmäsähköpostien lukumäärän
     * 
     * @param query Hakuparametrit
     * @return Raportoitavien ryhmäsähköpostien lukumäärän
     */
    Long getNumberOfReportedMessages(ReportedMessageQueryDTO query);

    /**
     * Hakee raportoitavien ryhmäsähköpostien lukumäärän
     * 
     * @param organizationOid Organisaation oid-tunnus
     * @return Raportoitavien ryhmäsähköpostien lukumäärän
     */
    Long getNumberOfReportedMessages(String organizationOid);

    /**
     * Hakee raportoitavan viestin viestin avaimella
     * 
     * @param id Raportoitavan viestin avain
     * @return Raportoitavan viestin tiedot {@link ReportedMessage}
     */
    ReportedMessage getReportedMessage(Long id);

    /**
     * Hakee annettujen parametrien mukaiset raportoitavat viestit
     * 
     * @param query Hakuparametrit
     * @param pagingAndSorting Sivutus ja lajittelutiedot
     * @return Lists raportoitavia viestejä {@link ReportedMessage}
     */
    List<ReportedMessage> getReportedMessages(ReportedMessageQueryDTO query, 
	    PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee käyttäjän organisaation lähettämät raportoitavat viestit
     *  
     * @param organizationOid Organisaation oid-tunnus
     * @param pagingAndSorting Sivutus ja lajittelutiedot
     * @return
     */
    List<ReportedMessage> getReportedMessages(String organizationOid, PagingAndSortingDTO pagingAndSorting);
    
    /**
     * Hakee määritellyn lähettäjän tietyn prosessin sisällä lähettämät raportoitavat viestit
     *  
     * @param senderOid Organisaation oid-tunnus
     * @param pagingAndSorting Sivutus ja lajittelutiedot
     * @return
     */
    List<ReportedMessage> getUserMessages(String senderOid, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee määritellyn lähettäjän tietyn prosessin sisällä lähettämät raportoitavat viestit
     *
     * @param senderOid Organisaation oid-tunnus
     * @param process Kutsuva prosessi
     * @param pagingAndSorting Sivutus ja lajittelutiedot
     * @return
     */
    List<ReportedMessage> getUserMessages(String senderOid, String process, PagingAndSortingDTO pagingAndSorting);

    /**
     * Tallentaa raportoitvan ryhmäsähköpostin viestin
     * 
     * @param reportedMessage Ryhmäsähköpostin raportoitavaviesti
     * @return Tallennetu raportoitava viesti
     */
    ReportedMessage saveReportedMessage(ReportedMessage reportedMessage);

    /**
     * Päivittää raportoitavan viestin tietoja tietokantaan
     * @param reportedMessage Päivittetävä raportoitava viesti
     */
    void updateReportedMessage(ReportedMessage reportedMessage);
}
