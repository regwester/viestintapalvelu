package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavanViestinLiite;

/**
 * Rajapinta raproitavan viestin liitteiden avaimien tietokantakäsittelyä varten
 *  
 * @author vehei1
 *
 */
public interface RaportoitavanViestinLiiteDAO extends JpaDAO<RaportoitavanViestinLiite, Long> {
	
	/**
	 * Hakee raportoitavan ryhmäsähköpostiviestin liitteiden avaimet
	 * 
	 * @param viestinID Ryhmäsähköpostiviestin avain
	 * @return Lista raportoitavan viestin liitteiden avaimet
	 */
	public List<RaportoitavanViestinLiite> haeRaportoitavanViestinLiitteet(Long viestinID);
}
