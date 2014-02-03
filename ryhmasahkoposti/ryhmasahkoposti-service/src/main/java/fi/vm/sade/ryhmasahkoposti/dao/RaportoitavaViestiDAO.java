package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;

/**
 * Rajapinta raportoitavien ryhmäsähköpostiviestien tietokantakäsittelyyn
 * 
 * @author vehei1
 *
 */
public interface RaportoitavaViestiDAO extends JpaDAO<RaportoitavaViesti, Long> {
	/**
	 * Hakee hakuparametrien mukaiset raportoitavat viestit
	 *  
	 * @param query Hakuparametrit
	 * @return Lista raportoituja ryhmäsähköpostiviesteja
	 */
	public List<RaportoitavaViesti> findBySearchCriteria(EmailMessageQueryDTO query);

	/**
	 * Hakee raportoitavat viestit lahettajien oid-tunnuksen perusteella
	 * 
	 * @param lahettajanOidList Lista lähettäjien oid-tunnuksia
	 * @return Lista ryhmäsähköpostin raportoitavia viestejä
	 */
	public List<RaportoitavaViesti> findLahettajanRaportoitavatViestit(List<String> lahettajanOidList);
	
	/**
	 * Hakee raportoitujen ryhmäsähköpostien lukumäärän
	 * 
	 * @return Raportoitujen ryhmäsähköpostien lukumäärä
	 */
	public Long findRaportoitavienViestienLukumaara();
}
