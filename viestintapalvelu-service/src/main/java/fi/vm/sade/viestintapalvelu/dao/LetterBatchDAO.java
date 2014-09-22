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
	public LetterBatch findLetterBatchByNameOrgTag(String templateName, String language, String organizationOid,
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
	public LetterBatch findLetterBatchByNameOrg(String templateName, String language, String organizationOid);

	/**
	 * Hakee listan kirjelähetysten tietoja sivutettuna ja lajiteltuna
	 * 
	 * @param organizationOID Käyttäjän organisaatiotieto
	 * @param pagingAndSorting Sivutus- ja lajittelutiedot
	 * @return Lista kirjelähetysten tietoja
	 */
    public List<LetterBatch> findLetterBatchesByOrganizationOid(String organizationOID, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee listan kirjelähetyksiä annetujen hakuparametrien mukaisesti
     * 
     * @param letterReportQuery Kirjelähetysten raportoinnin hakuparametrit
     * @param pagingAndSorting Sivutus- ja lajittelutiedot
     * @return Lista kirjelähetysten tietoja
     */
    public List<LetterBatch> findLetterBatchesBySearchArgument(LetterReportQueryDTO letterReportQuery, PagingAndSortingDTO pagingAndSorting);
    
    /**
	 * Hakee kirjelähetysten lukumäärän
	 * 
	 * @param  organizationOid Organisaation oid-tunnus
	 * @return Kirjelähetysten lukumäärä
	 */
	public Long findNumberOfLetterBatches(String organizationOid);
	
	/**
	 * Hakee kirjelähetysten lukumäärän annettujen hakuparametrien mukaisesti
	 * 
	 * @param  letterReportQuery Kirjelähetysten raportoinnin hakuparametrit
	 * @return Hakuparametreja vastaavien kirjelähetysten lukumäärä
	 */
	public Long findNumberOfLetterBatchesBySearchArgument(LetterReportQueryDTO letterReportQuery);
}
