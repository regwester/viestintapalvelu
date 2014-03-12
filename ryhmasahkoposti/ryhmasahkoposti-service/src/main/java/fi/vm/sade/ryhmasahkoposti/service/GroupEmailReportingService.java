package fi.vm.sade.ryhmasahkoposti.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessagesDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

/**
 * Rajapinta lähetettävän ryhmäsähköpostiviestin raportointia varten
 * 
 * @author vehei1
 *
 */
public interface GroupEmailReportingService {
	/**
	 * Lisää lähetettävän ryhmäsähköpostin tiedot odottamaan lähetystä
	 *  
	 * @param emailData Ryhmäshköpostin tiedot
	 * @return Tallenetun ryhmäsköpostiviestin avain
	 * @throws IOException
	 */
	public Long addSendingGroupEmail(EmailData emailData) throws IOException;

	/**
	 * Hakee viestintunnuksella lähetettävän ryhmäsähköpostiviestin  
	 * 
	 * @param messageID Viestintunnus 
	 * @return Lähetettävän ryhmäsähköpostiviestin tiedot
	 */
	public EmailMessageDTO getMessage(Long messageID);

	/**
	 * Hakee viestintunnuksella raportoitavan ryhmäsähköpostiviestin  
	 * 
	 * @param messageID Viestintunnus 
	 * @return Raporetoitavan ryhmäsähköpostiviestin tiedot
	 */
	public ReportedMessageDTO getReportedMessage(Long messageID);

	/**
	 * Hakee käyttäjän ja hänen käyttäjäryhmänsä raportoitavat viestit 
	 * 
	 * @param pagingAndSorting Mahdolliset sivutus- ja lajittelutiedot
	 * @return Haluttu määrä raportoitavia ryhmäsähköpostiviestejä
	 */
	public ReportedMessagesDTO getReportedMessages(PagingAndSortingDTO pagingAndSorting);

	/**
	 * Hakee hakuparametrin mukaiset käyttäjän ja hänen käyttäjäryhmänsä raportoitavat viestit 
	 * 
	 * @param searchArgument Hakuparametri
	 * @param pagingAndSorting Mahdolliset sivutus- ja lajittelutiedot
	 * @return Haluttu määrä raportoitavia ryhmäsähköpostiviestejä
	 */
	public ReportedMessagesDTO getReportedMessages(String searchArgument, PagingAndSortingDTO pagingAndSorting);

	/**
	 * Hakee viestintunnuksella raportoitavan ryhmäsähköpostiviestin ja sen lähetysraportin   
	 * 
	 * @param messageID Viestintunnus 
	 * @param pagingAndSorting Sivutus ja lajittelutiedot
	 * @return Raportoitavan ryhmäsähköpostiviestin tiedot ja sen lähetysraportin
	 */
    public ReportedMessageDTO getReportedMessage(Long messageID, PagingAndSortingDTO pagingAndSorting);

	/**
	 * Hakee lähetyksen tuloksen
	 * 
	 * @param messageID Halutun viestin tunnus
	 * @return Lähetyksen tulostiedot, missä näkyy esim. lähetettyjen viestin lukumäärä {@link SendingStatusDTO}
	 */
	public SendingStatusDTO getSendingStatus(Long messageID);
	
	/**
	 * Hakee raportoitavan viestin vastaanottajien tiedot, joille viesti on lähettämättä
	 * 
	 * @param vastaanottajienLukumaara Palautettavien vastaanottajien lukumaara
	 * @return Lista raportoitavan viestin vastaanottajien tietoja {@link ReportedRecipient}
	 */
	public List<EmailRecipientDTO> getUnhandledMessageRecipients(int listSize);
	
	/**
	 * Päivittää tiedon, että sähköpostin lähetys vastaanottajalle ei onnistunut
	 *   
	 * @param recipient Ryhmäsähköpostin vastaanottajan tiedot
	 * @param result Tieto, miksi lähetys ei onnistunut
	 * @return true, päivitys on tehty.
	 */	
	public boolean recipientHandledFailure(EmailRecipientDTO recipient, String result);
	
	/**
	 * Päivittää tiedon, että sähköpostin lähetys vastaanottajalle on onnistunut
	 *   
	 * @param recipient Ryhmäsähköpostin vastaanottajan tiedot
	 * @param result Tieto, että lähetys onnistui
	 * @return true, päivitys on tehty.
	 */
	public boolean recipientHandledSuccess(EmailRecipientDTO recipient, String result);
	
	/**
	 * Tallentaa ryhmäsähköpostin liitteen tietokantaan raportointia varten
	 * 
	 * @param fileItem Liitetiedoston tiedot
	 * @return Liitteelle generoitu avain
	 */
	public Long saveAttachment(FileItem fileItem) throws IOException;

	/**
	 * Merkitsee sähköpostin lähetyksen vastaanottajalle alkaneeksi
	 * 
	 * @param recipient Ryhmäsähköpostin vastaanottajan tiedot
	 * @return true, jos merkintä lähettäjälle onnistui. false, jos lähetys on jo aloitettu toisen säikeen toimesta
	 */
	public boolean startSending(EmailRecipientDTO recipient);
}
