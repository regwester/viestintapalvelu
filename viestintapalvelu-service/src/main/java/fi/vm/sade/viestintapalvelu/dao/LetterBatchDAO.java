package fi.vm.sade.viestintapalvelu.dao;

import java.util.List;

import com.google.common.base.Optional;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.dto.query.LetterReportQueryDTO;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;;

/**
 * Rajapinta kirjelähetysten tietokantakäsittelyä varten
 * 
 * @author vehei1
 *
 */
public interface LetterBatchDAO extends JpaDAO<LetterBatch, Long> {
    /**
     * Haetaan kirjelähetyksen tiedot hakuparametreilla
     * 
     * @param templateName Kirjepohjan nimi
     * @param language Kielikoodi
     * @param organizationOid Organisaation OID
     * @param tag Tunniste
     * @param applicationPeriod
     * @return Kirjelähetyksen tiedot
     */
	LetterBatch findLetterBatchByNameOrgTag(String templateName, String language, String organizationOid,
                                                   Optional<String> tag,
                                                   Optional<String> applicationPeriod);

    /**
     * Haetaan kirjelähetyksen tiedot hakuparametreilla
     * 
     * @param templateName Kirjepohjan nimi
     * @param language Kielikoodi
     * @param organizationOid Organisaation OID
     * @return Kirjelähetyksen tiedot
     */
	LetterBatch findLetterBatchByNameOrg(String templateName, String language, String organizationOid);

	/**
	 * Hakee listan kirjelähetysten tietoja sivutettuna ja lajiteltuna
	 * 
	 * @param organizationOID Käyttäjän organisaatiotieto
	 * @param pagingAndSorting Sivutus- ja lajittelutiedot
	 * @return Lista kirjelähetysten tietoja
	 */
    List<LetterBatch> findLetterBatchesByOrganizationOid(String organizationOID, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee listan kirjelähetyksiä annetujen hakuparametrien mukaisesti
     * 
     * @param letterReportQuery Kirjelähetysten raportoinnin hakuparametrit
     * @param pagingAndSorting Sivutus- ja lajittelutiedot
     * @return Lista kirjelähetysten tietoja
     */
    List<LetterBatch> findLetterBatchesBySearchArgument(LetterReportQueryDTO letterReportQuery, PagingAndSortingDTO pagingAndSorting);
    
    /**
	 * Hakee kirjelähetysten lukumäärän
	 * 
	 * @param  organizationOid Organisaation oid-tunnus
	 * @return Kirjelähetysten lukumäärä
	 */
	Long findNumberOfLetterBatches(String organizationOid);
	
	/**
	 * Hakee kirjelähetysten lukumäärän annettujen hakuparametrien mukaisesti
	 * 
	 * @param  letterReportQuery Kirjelähetysten raportoinnin hakuparametrit
	 * @return Hakuparametreja vastaavien kirjelähetysten lukumäärä
	 */
	Long findNumberOfLetterBatchesBySearchArgument(LetterReportQueryDTO letterReportQuery);

    /**
     * @param letterBatchId id of a LetterBatch
     * @return the status of given LetterBatch
     */
    LetterBatchStatusDto getLetterBatchStatus(long letterBatchId);
    
    /**
     * 
     * @param batchId id of LetterBatch
     * @return list of ids for LetterReceivers that have yet to be processed
     */
    List<Long> findUnprocessedLetterReceiverIdsByBatch(long batchId);
    
    /**
     * Fetches id's of unfinished LetterBatches that haven't been terminated because of error
     * @return list of id's of unfinished LetterBatches 
     */
    List<Long> findUnfinishedLetterBatches();
}
