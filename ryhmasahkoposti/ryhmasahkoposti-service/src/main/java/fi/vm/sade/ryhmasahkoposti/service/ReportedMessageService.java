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
    public Long getNumberOfReportedMessages(ReportedMessageQueryDTO query);

    /**
     * Hakee raportoitavien ryhmäsähköpostien lukumäärän
     * 
     * @param organizationOid Organisaation oid-tunnus
     * @return Raportoitavien ryhmäsähköpostien lukumäärän
     */
    public Long getNumberOfReportedMessages(String organizationOid);

    /**
     * Hakee raportoitavan viestin viestin avaimella
     * 
     * @param id Raportoitavan viestin avain
     * @return Raportoitavan viestin tiedot {@link ReportedMessage}
     */
    public ReportedMessage getReportedMessage(Long id);

    /**
     * Hakee annettujen parametrien mukaiset raportoitavat viestit
     * 
     * @param query Hakuparametrit
     * @param Sivutus ja lajittelutiedot
     * @return Lists raportoitavia viestejä {@link ReportedMessage}
     */
    public List<ReportedMessage> getReportedMessages(ReportedMessageQueryDTO query, 
	    PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee käyttäjän organisaation lähettämät raportoitavat viestit
     *  
     * @param organizationOid Organisaation oid-tunnus
     * @param pagingAndSorting Sivutus ja lajittelutiedot
     * @return
     */
    public List<ReportedMessage> getReportedMessages(String organizationOid, PagingAndSortingDTO pagingAndSorting);

    /**
     * Tallentaa raportoitvan ryhmäsähköpostin viestin
     * 
     * @param reportedMessage Ryhmäsähköpostin raportoitavaviesti
     * @return Tallennetu raportoitava viesti
     */
    public ReportedMessage saveReportedMessage(ReportedMessage reportedMessage);

    /**
     * Päivittää raportoitavan viestin tietoja tietokantaan
     * @param reportedMessage Päivittetävä raportoitava viesti
     */
    public void updateReportedMessage(ReportedMessage reportedMessage);
}
