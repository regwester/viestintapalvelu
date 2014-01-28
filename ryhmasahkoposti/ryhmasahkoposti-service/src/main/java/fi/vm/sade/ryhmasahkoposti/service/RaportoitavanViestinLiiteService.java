package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavanViestinLiite;

/**
 * Rajapinta raportoitavan ryhmäsähköpostiviestin liittyvien liitteiden käsittelyä varten
 *  
 * @author vehei1
 *
 */
public interface RaportoitavanViestinLiiteService {

	/**
	 * Hakee raportoitavan ryhmäsähköpostiviestiin liittyvät liitteet
	 * 
	 * @param viestinID Raportoitavan ryhmäsähköpostin avain
	 * @return Lista ryhmäsähköpostiin liitettyjen liitteiden avaimia
	 */
	public List<RaportoitavanViestinLiite> haeRaportoitavanViestinLiitteet(Long viestinID);
	
	/**
	 * Tallentaa raportoitavan ryhmäsähköpostiviestiin liittyvät liitteet
	 * 
	 * @param raportoitavaViesti Raportoitavan ryhmäsähköpostiviestin tiedot
	 * @param raportoitavatLiitteet Lista ryhmäsähköpostiin liitettyjen liitteiden tietoja
	 */
	public void tallennaRaportoitavanViestinLiitteet(RaportoitavaViesti raportoitavaViesti, 
		List<RaportoitavaLiite> raportoitavatLiitteet);
}
