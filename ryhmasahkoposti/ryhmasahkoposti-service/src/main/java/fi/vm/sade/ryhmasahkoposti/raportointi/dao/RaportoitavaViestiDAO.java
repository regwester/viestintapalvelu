package fi.vm.sade.ryhmasahkoposti.raportointi.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.query.RyhmasahkopostiViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaViesti;

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
	public List<RaportoitavaViesti> findBySearchCriteria(RyhmasahkopostiViestiQueryDTO query);

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
