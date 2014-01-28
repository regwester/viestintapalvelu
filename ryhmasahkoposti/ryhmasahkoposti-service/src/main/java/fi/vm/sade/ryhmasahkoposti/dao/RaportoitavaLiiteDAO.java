package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;

/**
 * Rajapinta ryhmäsähköpostin raportoitavien liitteiden tietokantakäsittelyä varten
 * 
 * @author vehei1
 *
 */
public interface RaportoitavaLiiteDAO extends JpaDAO<RaportoitavaLiite, Long> {
	/**
	 * Tallentaa raportoitavat liitteet
	 * 
	 * @param liitteet Lista raportoitavia liitteitä
	 * @return Lista raportoitavien liitteiden avaimia
	 */
	public List<Long> tallennaRaportoitavatLiitteet(List<RaportoitavaLiite> liitteet);
}
