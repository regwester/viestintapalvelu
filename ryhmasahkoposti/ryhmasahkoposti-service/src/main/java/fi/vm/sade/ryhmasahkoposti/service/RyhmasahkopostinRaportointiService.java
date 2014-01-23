package fi.vm.sade.ryhmasahkoposti.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenLopetusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenTilanneDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;

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
	 * @return Lähetyksen tulostiedot, missä näkyy esim. lähetettyjen viestin lukumäärä {@link LahetyksenTilanneDTO}
	 */
	public LahetyksenTilanneDTO haeLahetyksenTulos(Long viestiID);

	/**
	 * Hakee raportoitavan viestin vastaanottajien tiedot, joille viesti on lähettämättä
	 * 
	 * @param vastaanottajienLukumaara Palautettavien vasttanottajien lukumaara
	 * @return Lista raportoitavan viestin vastaanottajien tietoja {@link RaportoitavaVastaanottaja}
	 */
	public List<RaportoitavaVastaanottaja> haeRaportoitavatVastaanottajatViestiLahettamatta(int vastaanottajienLukumaara);

	/**
	 * Hakee raportoitavan viestin viestintunnuksella 
	 * 
	 * @param viestiID Viestintunnus
	 * @return Lista raportoitavia viestejä
	 */
	public RaportoitavaViestiDTO haeRaportoitavaViesti(Long viestiID);

	/**
	 * Hakee käyttäjän ja hänen käyttäjäryhmänsä raportoitavat viestit 
	 * 
	 * @param query Hakuparametri
	 * @return Lista raportoitavia viestejä
	 */
	public List<RaportoitavaViestiDTO> haeRaportoitavatViestit();

	/**
	 * Hakee hakuparametrin mukaiset käyttäjän ja hänen käyttäjäryhmänsä raportoitavat viestit 
	 * 
	 * @param hakuKentta Hakuparametri
	 * @return Lista raportoitavia viestejä
	 */
	public List<RaportoitavaViestiDTO> haeRaportoitavatViestit(String hakuKentta);
	
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
	 * @return true, jos raportointi onnistui. false, jos raportointi on jo tehty.
	 */
	public boolean raportoiLahetysVastaanottajalle(LahetettyVastaanottajalleDTO lahetettyVastaanottajalle);
	
	/**
	 * Raportoi ryhmäsähköpostin lähetetyksen lopetus
	 * 
	 * @param lahetyksenLopetus Ryhmäsähköpostin lähetyksen lopetustiedot
	 * @return true, jos raportointi onnistui. false, jos raportointi on jo tehty.
	 */
	public boolean raportoiLahetyksenLopetus(LahetyksenLopetusDTO lahetyksenLopetus);
	
	/**
	 * Tallentaa ryhmäsähköpostin liitteen tietokantaan raportointia varten
	 * 
	 * @param fileItem Liitetiedoston tiedot
	 * @return Liitteelle generoitu avain
	 */
	public Long tallennaLiite(FileItem fileItem) throws IOException;
}
