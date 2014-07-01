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
     * @param organizationOid Organisaation oid-tunnus
     * @param pagingAndSorting Palautettavien tietojen sivutus ja järjestystiedot
     * @return Lista raportoitavia viestejä
     */
    public List<ReportedMessage> findByOrganizationOid(String organizationOid, PagingAndSortingDTO pagingAndSorting);
    
    /**
     * Hakee halutun käyttäjän lähettämiä raportoitavia viestejä halutussa järjestyksessä
     *  
     * @param senderOid Lähettäjän oid-tunnus
     * @param processConstraint Prosessi, jonka kautta viesti on lähetetty
     * @param pagingAndSorting Palautettavien tietojen sivutus ja järjestystiedot
     * @return Lista raportoitavia viestejä
     */
    public List<ReportedMessage> findBySenderOid(String senderOid, String processConstraint, 
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
	 * @param  organizationOid Organisaation oid-tunnus
	 * @return Raportoitujen ryhmäsähköpostien lukumäärä
	 */
	public Long findNumberOfReportedMessage(String organizationOid);

	   /**
     * Hakee hakuparametrien mukaiset raportoitujen ryhmäsähköpostien lukumäärän
     * 
     * @param query Hakuparametrit
     * @return Raportoitujen ryhmäsähköpostien lukumäärä
     */
    public Long findNumberOfReportedMessage(ReportedMessageQueryDTO query);
}
