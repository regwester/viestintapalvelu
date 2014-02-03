package fi.vm.sade.ryhmasahkoposti.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenTilanneDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;

/**
 * Rajapinta lähetettävän ryhmäsähköpostiviestin raportointia varten
 * 
 * @author vehei1
 *
 */
public interface GroupEmailReportingService {
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

	/**
	 * Hakee käyttäjän ja hänen käyttäjäryhmänsä raportoitavat viestit 
	 * 
	 * @return Lista raportoitavia viestejä
	 */
	public List<EmailMessageDTO> getMessages();

	/**
	 * Hakee hakuparametrin mukaiset käyttäjän ja hänen käyttäjäryhmänsä raportoitavat viestit 
	 * 
	 * @param searchArgument Hakuparametri
	 * @return Lista raportoitavia ryhmäsähköpostiviestejä
	 */
	public List<EmailMessageDTO> getMessages(String searchArgument);

	/**
	 * Hakee viestintunnuksella raportoitavan viestin ja haluttaessa viestille lähetysraportin  
	 * 
	 * @param viestiID Viestintunnus
	 * @param lahetysRaportti true muodostetaan viestin lähetysraportti, false ei muodosteta 
	 * @return Lista raportoitavia viestejä
	 */
	public EmailMessageDTO getMessage(Long messageID);
	
	/**
	 * Merkitsee sähköpostin lähetyksen vastaanottajalle alkaneeksi
	 * 
	 * @param recipient Ryhmäsähköpostin vastaanottajan tiedot
	 * @return true, jos merkintä lähettäjälle onnistui. false, jos lähetys on jo aloitettu toisen säikeen toimesta
	 */
	public boolean startSending(EmailRecipientDTO recipient);
	
	/**
	 * Päivittää tiedon, että sähköpostin lähetys vastaanottajalle on onnistunut
	 *   
	 * @param recipient Ryhmäsähköpostin vastaanottajan tiedot
	 * @param result Tieto, että lähetys onnistui
	 * @return true, päivitys on tehty.
	 */
	public boolean recipientHandledSuccess(EmailRecipientDTO recipient, String result);
	
	/**
	 * Päivittää tiedon, että sähköpostin lähetys vastaanottajalle ei onnistunut
	 *   
	 * @param recipient Ryhmäsähköpostin vastaanottajan tiedot
	 * @param result Tieto, miksi lähetys ei onnistunut
	 * @return true, päivitys on tehty.
	 */	
	public boolean recipientHandledFailure(EmailRecipientDTO recipient, String result);
	
	/**
	 * Tallentaa ryhmäsähköpostin liitteen tietokantaan raportointia varten
	 * 
	 * @param fileItem Liitetiedoston tiedot
	 * @return Liitteelle generoitu avain
	 */
	public Long saveAttachment(FileItem fileItem) throws IOException;

	/**
	 * Tallentaa ryhmäsähköpostin tiedot
	 *  
	 * @param emailData Ryhmäshköpostin tiedot
	 * @return Tallenetun ryhmäsköpostiviestin avain
	 * @throws IOException
	 */
	public Long saveSendingEmail(EmailData emailData) throws IOException;
}
