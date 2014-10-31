package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;

/**
 * Rajapinta raportoitavien ryhmäsähköpostiviestien tietokantakäsittelyyn
 * 
 * @author vehei1
 *
 */
public interface ReportedMessageDAO extends JpaDAO<ReportedMessage, Long> {
    /**
     * Hakee halutun määrän käyttäjän organisaation raportoitavia viestejä halutussa järjestyksessä
     *  
     * @param organizationOids Organisaatioiden oid-tunnukset, jos null ei rajata
     * @param pagingAndSorting Palautettavien tietojen sivutus ja järjestystiedot
     * @return Lista raportoitavia viestejä
     */
    public List<ReportedMessage> findByOrganizationOids(List<String> organizationOids, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee halutun käyttäjän lähettämiä raportoitavia viestejä halutussa järjestyksessä
     *
     * @param senderOid Lähettäjän oid-tunnus
     * @param pagingAndSorting Palautettavien tietojen sivutus ja järjestystiedot
     * @return Lista raportoitavia viestejä
     */
    public List<ReportedMessage> findBySenderOid(String senderOid, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee halutun käyttäjän lähettämiä raportoitavia viestejä halutussa järjestyksessä
     *  
     * @param senderOid Lähettäjän oid-tunnus
     * @param process Prosessi, jonka kautta viesti on lähetetty
     * @param pagingAndSorting Palautettavien tietojen sivutus ja järjestystiedot
     * @return Lista raportoitavia viestejä
     */
    public List<ReportedMessage> findBySenderOidAndProcess(String senderOid, String process, 
    		PagingAndSortingDTO pagingAndSorting);
	
    /**
	 * Hakee hakuparametrien mukaiset raportoitavat viestit
	 *  
	 * @param query Hakuparametrit
	 * @param pagingAndSorting Lajittelutekijät
	 * @return Lista raportoituja ryhmäsähköpostiviesteja
	 */
	public List<ReportedMessage> findBySearchCriteria(ReportedMessageQueryDTO query, 
	    PagingAndSortingDTO pagingAndSorting);

	/**
	 * Hakee raportoitujen ryhmäsähköpostien lukumäärän
	 * 
	 * @param  organizationOids Organisaatioiden oid-tunnukset, jos null, ei rajata
	 * @return Raportoitujen ryhmäsähköpostien lukumäärä
	 */
	public Long findNumberOfReportedMessages(List<String> organizationOids);

    /**
     * Hakee raportoitujen ryhmäsähköpostien lukumäärän tietyllä käyttäjällä
     *
     * @param  userOid Käyttäjän oid-tunnus
     * @return Kyseisen käyttäjän lähettämien ryhmäsähköpostien lukumäärä
     */
    public Long findNumberOfUserMessages(String userOid);

	   /**
     * Hakee hakuparametrien mukaiset raportoitujen ryhmäsähköpostien lukumäärän
     * 
     * @param query Hakuparametrit
     * @return Raportoitujen ryhmäsähköpostien lukumäärä
     */
    public Long findNumberOfReportedMessage(ReportedMessageQueryDTO query);
}
