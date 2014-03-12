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
     * Hakee halutun määrän raportoitavia viestejä halutussa järjestyksessä
     *  
     * @param pagingAndSorting Palautettavien tietojen sivutus ja järjestystiedot
     * @return Lista raportoitavia viestejä
     */
    public List<ReportedMessage> findAll(PagingAndSortingDTO pagingAndSorting);
	
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
	 * Hakee raportoitavat viestit lahettajien oid-tunnuksen perusteella
	 * 
	 * @param senderOids Lista lähettäjien oid-tunnuksia
	 * @return Lista ryhmäsähköpostin raportoitavia viestejä
	 */
	public List<ReportedMessage> findReportedMessagesBySendersOids(List<String> senderOids);
	
	/**
	 * Hakee raportoitujen ryhmäsähköpostien lukumäärän
	 * 
	 * @return Raportoitujen ryhmäsähköpostien lukumäärä
	 */
	public Long findNumberOfReportedMessage();
}
