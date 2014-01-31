package fi.vm.sade.ryhmasahkoposti.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenLopetusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenTilanneDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaVastaanottajaDTO;
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
	 * @param vastaanottajienLukumaara Palautettavien vastaanottajien lukumaara
	 * @return Lista raportoitavan viestin vastaanottajien tietoja {@link RaportoitavaVastaanottaja}
	 */
	public List<EmailRecipientDTO> getUnhandledMessageRecipients(int listSize);
	
	public List<RaportoitavaVastaanottajaDTO> haeRaportoitavatVastaanottajatViestiLahettamatta(int vastaanottajienLukumaara);

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
	 * Hakee raportoitavan viestin viestintunnuksella 
	 * 
	 * @param viestiID Viestintunnus
	 * @param lahetysRaportti true muodostetaan viestin lähetysraportti, false ei muodosteta 
	 * @return Lista raportoitavia viestejä
	 */
	public RaportoitavaViestiDTO haeRaportoitavaViesti(Long viestiID, boolean lahetysRaportti);
	
	public EmailMessageDTO getMessage(Long messageID);
	
	/**
	 * Raportoi ryhmäsähköpostin lähetyksen aloittamisen 
	 *  
	 * @param lahetyksenAloitus Ryhmäsahköpostin lähetyksen aloitustiedot
	 * @return Raportoitavan ryhmäsähköpostiviestin yksilöllinen tunnus 
	 * @throws IOException
	 */
	public Long raportoiLahetyksenAloitus(LahetyksenAloitusDTO lahetyksenAloitus) throws IOException;
	
	/**
	 * Raportoi ryhmäsähköpostin lähetetyksen lopetus
	 * 
	 * @param lahetyksenLopetus Ryhmäsähköpostin lähetyksen lopetustiedot
	 * @return true, jos raportointi onnistui. false, jos raportointi on jo tehty.
	 */
	public boolean raportoiLahetyksenLopetus(LahetyksenLopetusDTO lahetyksenLopetus);
	
	/**
	 * Raportoi ryhmäsähköpostinlähetyksen tilanteen. Kun lähetys halutaan merkitä aloitetuksi vastaanottajalle, 
	 * niin välitetään tiedoissa lähetyksen aloitushetki. Kun lähetys halutaan merkitä päättyneeksi vastaanottajalle,
	 * niin välitetään tiedoissa päättymishetki ja mahdollinen lähetyksen epäonnistumisen syy.
	 * 
	 * @param lahetettyVastaanottajalle Vastaanottajalle lähetyksen tiedot
	 * @return true, jos raportointi onnistui. false, jos raportoinin on jo tehty esim. toinen lähetyssäie.
	 */
	public boolean raportoiLahetyksenTilanne(LahetettyVastaanottajalleDTO lahetettyVastaanottajalle);
	
	public boolean startSending(EmailRecipientDTO recipient);
	public boolean recipientHandledSuccess(EmailRecipientDTO recipient, String result);
	public boolean recipientHandledFailure(EmailRecipientDTO recipient, String result);
	
	/**
	 * Tallentaa ryhmäsähköpostin liitteen tietokantaan raportointia varten
	 * 
	 * @param fileItem Liitetiedoston tiedot
	 * @return Liitteelle generoitu avain
	 */
	public Long tallennaLiite(FileItem fileItem) throws IOException;
}
