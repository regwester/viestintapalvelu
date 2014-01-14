package fi.vm.sade.ryhmasahkoposti.service;

import java.io.IOException;
import java.util.List;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenLopetusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenTulosDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;

/**
 * Rajapinta lähetettävän ryhmäsähköpostiviestin raportointia varten
 * 
 * @author vehei1
 *
 */
public interface RyhmasahkopostinRaportointiService {
	/**
	 * Hakee lähetyksen tuloksen
	 * 
	 * @param viestiID Halutun viestin tunnus
	 * @return Lähetyksen tulostiedot, missä näkyy esim. lähetettyjen viestin lukumäärä {@link LahetyksenTulosDTO}
	 */
	public LahetyksenTulosDTO haeLahetyksenTulos(Long viestiID);

	/**
	 * Hakee käyttäjän ja hänen käyttäjäryhmänsä raportoitavat viestit 
	 * 
	 * @param query Hakuparametri
	 * @return Lista raportoitavia viestejä
	 */
	public List<RaportoitavaViesti> haeRaportoitavatViestit();

	/**
	 * Hakee hakuparametrin mukaiset käyttäjän ja hänen käyttäjäryhmänsä raportoitavat viestit 
	 * 
	 * @param query Hakuparametri
	 * @return Lista raportoitavia viestejä
	 */
	public List<RaportoitavaViesti> haeRaportoitavatViestit(String query);
	
	/**
	 * Raportoi ryhmäsähköpostin lähetyksen aloittamisen 
	 *  
	 * @param lahetyksenAloitus Ryhmäsahköpostin lähetyksen aloitustiedot
	 * @return Raportoitavan ryhmäsähköpostiviestin yksilöllinen tunnus 
	 * @throws IOException
	 */
	public Long raportoiLahetyksenAloitus(LahetyksenAloitusDTO lahetyksenAloitus) throws IOException;
	
	/**
	 * Raportoi ryhmäsähköpostinlähetys vastaanottajalle
	 * 
	 * @param lahetettyVastaanottajalle Vastaanottajalle lähetyksen tiedot
	 */
	public void raportoiLahetysVastaanottajalle(LahetettyVastaanottajalleDTO lahetettyVastaanottajalle);
	
	/**
	 * Raportoi ryhmäsähköpostin lähetetyksen lopetus
	 * 
	 * @param lahetyksenLopetus Ryhmäsähköpostin lähetyksen lopetustiedot
	 */
	public void raportoiLahetyksenLopetus(LahetyksenLopetusDTO lahetyksenLopetus);
	
	public String testaa();
}
