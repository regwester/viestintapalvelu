/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.ryhmasahkoposti.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.OrganizationDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessagesDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedRecipientReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
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
    Long addSendingGroupEmail(EmailData emailData) throws IOException;

    /**
     * Hakee viestintunnuksella lähetettävän ryhmäsähköpostiviestin  
     * 
     * @param messageID Viestintunnus 
     * @return Lähetettävän ryhmäsähköpostiviestin tiedot
     */
    EmailMessageDTO getMessage(Long messageID);

    /**
     * Get recipient replacements
     * 
     * @param recipientId
     * @return
     */
    List<ReportedRecipientReplacementDTO> getRecipientReplacements(long recipientId) throws IOException;

    /**
     * Hakee viestintunnuksella raportoitavan ryhmäsähköpostiviestin  
     * 
     * @param messageID Viestintunnus 
     * @return Raporetoitavan ryhmäsähköpostiviestin tiedot
     */
    ReportedMessageDTO getReportedMessage(Long messageID);

    /**
     * Hakee viestintunnuksella raportoitavan ryhmäsähköpostiviestin ja sen lähetysraportin   
     * 
     * @param messageID Viestintunnus 
     * @param pagingAndSorting Sivutus ja lajittelutiedot
     * @return Raportoitavan ryhmäsähköpostiviestin tiedot ja sen lähetysraportin
     */
    ReportedMessageDTO getReportedMessageAndRecipients(Long messageID, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee viestintunnuksella raportoitavan ryhmäsähköpostiviestin ja vastaanottajat, joille lähetys epäonnistui   
     * 
     * @param messageID Viestintunnus 
     * @param pagingAndSorting Sivutus ja lajittelutiedot
     * @return Raportoitavan ryhmäsähköpostiviestin tiedot, vastaanottajat ja lähetysraportin
     */
    ReportedMessageDTO getReportedMessageAndRecipientsSendingUnsuccessful(Long messageID,
	    PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee käyttäjän ja hänen käyttäjäryhmänsä raportoitavat viestit 
     * 
     * @param organizationOid Organisaation oid-tunnus
     * @param pagingAndSorting Mahdolliset sivutus- ja lajittelutiedot
     * @return Haluttu määrä raportoitavia ryhmäsähköpostiviestejä
     */
    ReportedMessagesDTO getReportedMessagesByOrganizationOid(String organizationOid, 
	    PagingAndSortingDTO pagingAndSorting);
    
    /**
     * Hakee käyttäjän määritellyn prosessin sisällä lähettämät 
     * 
     * @param senderOid Organisaation oid-tunnus
     * @param pagingAndSorting Mahdolliset sivutus- ja lajittelutiedot
     * @return Haluttu määrä raportoitavia ryhmäsähköpostiviestejä
     */
    ReportedMessagesDTO getReportedMessagesBySenderOid(String senderOid, String process, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee hakuparametrien mukaiset käyttäjän ja hänen käyttäjäryhmänsä raportoitavat viestit 
     * 
     * @param reportedMessageQueryDTO Hakuparametrit
     * @param pagingAndSorting Mahdolliset sivutus- ja lajittelutiedot
     * @return Haluttu määrä raportoitavia ryhmäsähköpostiviestejä
     */
    ReportedMessagesDTO getReportedMessages(ReportedMessageQueryDTO reportedMessageQueryDTO, 
	    PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee lähetyksen tuloksen
     * 
     * @param messageID Halutun viestin tunnus
     * @return Lähetyksen tulostiedot, missä näkyy esim. lähetettyjen viestin lukumäärä {@link SendingStatusDTO}
     */
    SendingStatusDTO getSendingStatus(Long messageID);

    /**
     * Hakee raportoitavan viestin vastaanottajien tiedot, joille viesti on lähettämättä
     * 
     * @param listSize Palautettavien vastaanottajien lukumaara
     * @return Lista raportoitavan viestin vastaanottajien tietoja {@link ReportedRecipient}
     */
    List<EmailRecipientDTO> getUnhandledMessageRecipients(int listSize);
    
    /**
     * Palauttaa käyttäjän oid:n
     * 
     * @return Käyttäjän oid
     */
    String getCurrentUserOid();

    /**
     * Palauttaa käyttäjän organisaatiotiedot
     * 
     * @return Lista käyttäjän organisaatioita
     */
    List<OrganizationDTO> getUserOrganizations();

    /**
     * Päivittää tiedon, että sähköpostin lähetys vastaanottajalle ei onnistunut
     *   
     * @param recipient Ryhmäsähköpostin vastaanottajan tiedot
     * @param result Tieto, miksi lähetys ei onnistunut
     * @return true, päivitys on tehty.
     */	
    boolean recipientHandledFailure(EmailRecipientDTO recipient, String result);

    /**
     * Päivittää tiedon, että sähköpostin lähetys vastaanottajalle on onnistunut
     *   
     * @param recipient Ryhmäsähköpostin vastaanottajan tiedot
     * @param result Tieto, että lähetys onnistui
     * @return true, päivitys on tehty.
     */
    boolean recipientHandledSuccess(EmailRecipientDTO recipient, String result);
    
    /**
     * Hakee ryhmäsähköpostin liitteen tietokannasta
     * 
     * @param attachmentID Liitetiedoston tunniste
     * @return Liitteen tiedot
     */
    ReportedAttachment getAttachment(Long attachmentID) throws IOException;

    /**
     * Tallentaa ryhmäsähköpostin liitteen tietokantaan raportointia varten
     * 
     * @param fileItem Liitetiedoston tiedot
     * @return Liitteelle generoitu avain
     */
    Long saveAttachment(FileItem fileItem) throws IOException;

    /**
     * Tallentaa ryhmäsähköpostin liitteen tietokantaan raportointia varten
     * 
     * @param emailAttachment Sähköpostiin liitetty tiedosto
     * @return Tallennetun liitteen tiedot
     */
    AttachmentResponse saveAttachment(EmailAttachment emailAttachment);

    /**
     * Merkitsee sähköpostin lähetyksen vastaanottajalle alkaneeksi
     * 
     * @param recipient Ryhmäsähköpostin vastaanottajan tiedot
     * @return true, jos merkintä lähettäjälle onnistui. false, jos lähetys on jo aloitettu toisen säikeen toimesta
     */
    boolean startSending(EmailRecipientDTO recipient);
}
