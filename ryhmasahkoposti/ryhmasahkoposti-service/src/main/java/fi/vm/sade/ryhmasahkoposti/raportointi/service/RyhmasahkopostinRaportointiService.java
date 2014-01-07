package fi.vm.sade.ryhmasahkoposti.raportointi.service;

import java.io.IOException;
import java.util.List;

import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenLopetusDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenTulosDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.query.RyhmasahkopostiViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaViesti;

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
	
	public List<RaportoitavaViesti> haeRaportoitavatViestit(RyhmasahkopostiViestiQueryDTO query);
	
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
}
