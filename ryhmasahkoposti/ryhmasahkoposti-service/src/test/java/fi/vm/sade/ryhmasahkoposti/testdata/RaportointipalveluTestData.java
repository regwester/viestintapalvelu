package fi.vm.sade.ryhmasahkoposti.testdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessagesDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

public class RaportointipalveluTestData {

	public static AttachmentResponse getAttachmentResponse(Long id, FileItem fileItem) {
    	AttachmentResponse attachmentResponse = new AttachmentResponse();
    	
    	attachmentResponse.setUuid(id.toString());
    	attachmentResponse.setFileName(fileItem.getName());
    	attachmentResponse.setContentType(fileItem.getContentType());
    	
    	return attachmentResponse;
    }

    public static EmailData getEmailData() {
		EmailData emailData = new EmailData();		
		return emailData;
	}
	
	public static EmailMessage getEmailMessage() {
		EmailMessage emailMessage = new EmailMessage();
		
		emailMessage.setBody("Tämä on koekutsu");
		emailMessage.setCallingProcess("Hakuprosessi");
		emailMessage.setCharset("utf-8");
		emailMessage.setFooter("");
		emailMessage.setHtml(false);
		emailMessage.setReplyTo("vastaus.oppilaitos@sposti.fi");
		emailMessage.setFrom("lahettaja.oppilaitos@sposti.fi");
		emailMessage.setSenderOid("1.2.246.562.24.42645159413");
		emailMessage.setSubject("Koekutsu");
		
		return emailMessage;
	}
	
	public static EmailMessageDTO getEmailMessageDTO() {
	    EmailMessageDTO emailMessageDTO = new EmailMessageDTO();
	    
	    emailMessageDTO.setMessageID(new Long(1));
        emailMessageDTO.setBody("Tämä on koekutsu");
        emailMessageDTO.setCallingProcess("Hakuprosessi");
        emailMessageDTO.setCharset("utf-8");
        emailMessageDTO.setFooter("");
        emailMessageDTO.setHtml(false);
        emailMessageDTO.setReplyTo("vastaus.oppilaitos@sposti.fi");
        emailMessageDTO.setFrom("lahettaja.oppilaitos@sposti.fi");
        emailMessageDTO.setSenderOid("1.2.246.562.24.42645159413");
        emailMessageDTO.setSubject("Koekutsu");
	    
	    return emailMessageDTO;
	}
	
	public static EmailAttachment getEmailAttachment() {
		EmailAttachment emailAttachment = new EmailAttachment();
		
		byte[] attachment = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		emailAttachment.setData(attachment);
		emailAttachment.setName("koekutsu.doc");
		emailAttachment.setContentType("application/pdf");		
		
		return emailAttachment;
	}
	
	public static EmailRecipient getEmailRecipient() {
		EmailRecipient emailRecipient = new EmailRecipient();
		
		emailRecipient.setOid("1.2.246.562.24.34397748041");
		emailRecipient.setOidType("oppilas");
		emailRecipient.setEmail("vastaan.ottaja@sposti.fi");
		emailRecipient.setLanguageCode("FI");
		
		return emailRecipient;
	}

	public static EmailRecipientDTO getEmailRecipientDTO() {
		EmailRecipientDTO emailRecipientDTO = new EmailRecipientDTO();
		
		emailRecipientDTO.setOid("1.2.246.562.24.34397748041");
		emailRecipientDTO.setOidType("oppilas");
		emailRecipientDTO.setEmail("vastaan.ottaja@sposti.fi");
		emailRecipientDTO.setLanguageCode("FI");
		
		return emailRecipientDTO;
	}

	public static Henkilo getHenkilo() {
	    Henkilo henkilo = new Henkilo();
	    
	    henkilo.setOidHenkilo("1.2.246.562.24.34397748041");
	    henkilo.setHetu("081181-9984");
	    henkilo.setEtunimet("Etunimi");
	    henkilo.setSukunimi("Sukunimi");
	    henkilo.setKutsumanimi("Kutsumanimi");
	    
	    return henkilo;
	}

	public static OrganisaatioRDTO getOrganisaatioRDTO() {
	    OrganisaatioRDTO organisaatio = new OrganisaatioRDTO();
	    
	    organisaatio.setOid("1.2.246.562.10.00000000001");
	    Map<String, String> nimet = new HashMap<String, String>();
	    nimet.put("fi", "Oppilaitos");
	    organisaatio.setNimi(nimet);
	    
	    return organisaatio;
	}

	public static PagingAndSortingDTO getPagingAndSortingDTO() {
	    PagingAndSortingDTO pagingAndSorting = new PagingAndSortingDTO();
	    
	    pagingAndSorting.setFromIndex(0);
	    pagingAndSorting.setNumberOfRows(10);
	    pagingAndSorting.setSortedBy("");
	    pagingAndSorting.setSortOrder("asc");
	    
	    return pagingAndSorting;
	}
	
	public static ReportedMessage getReportedMessage() {
		ReportedMessage reportedMessage = new ReportedMessage();
		
		reportedMessage.setSubject("Koekutsu");
		reportedMessage.setProcess("Hakuprosessi");
		reportedMessage.setSenderOid("1.2.246.562.24.42645159413");
		reportedMessage.setSenderOrganizationOid("1.2.246.562.10.00000000001");
		reportedMessage.setSenderEmail("testi.virkailija@sposti.fi");
		reportedMessage.setReplyToEmail("testi.virkailija@sposti.fi");
		reportedMessage.setMessage("Tämä on koekutsu");
		reportedMessage.setHtmlMessage("");
		reportedMessage.setCharacterSet("utf-8");
		reportedMessage.setSendingStarted(new Date());
		reportedMessage.setTimestamp(new Date());

		return reportedMessage;
	}

    public static ReportedMessageDTO getReportedMessageDTO() {
        ReportedMessageDTO reportedMessage = new ReportedMessageDTO();
        
        reportedMessage.setMessageID(new Long(1));
        reportedMessage.setSubject("Koekutsu");
        reportedMessage.setSenderOid("1.2.246.562.24.42645159413");
        reportedMessage.setOrganizationOid("1.2.246.562.10.00000000001");
        reportedMessage.setSendingStatus(getSendingStatusDTO());
        reportedMessage.setSendingReport("");
        reportedMessage.setStatusReport("Lahetyksiä epäonnistui");

        return reportedMessage;
    }
	
	public static ReportedMessagesDTO getReportedMessagesDTO() {
	    ReportedMessagesDTO reportedMessagesDTO = new ReportedMessagesDTO();
	    
	    reportedMessagesDTO.setNumberOfReportedMessages(new Long(1));
	    List<ReportedMessageDTO> reportedMessageDTOs = new ArrayList<ReportedMessageDTO>();
	    reportedMessageDTOs.add(getReportedMessageDTO());
	    reportedMessagesDTO.setReportedMessages(reportedMessageDTOs);
	    
	    return reportedMessagesDTO;
	}

	public static ReportedMessageQueryDTO getReportedMessageQueryDTO() {
	    ReportedMessageQueryDTO query = new ReportedMessageQueryDTO();
	    
	    query.setOrganizationOid("1.2.246.562.10.00000000001");
	    query.setSearchArgument("testi.vastaanottaja@sposti.fi");
	    
	    return query;
	}
	
	public static ReportedMessageAttachment getReportedMessageAttachment() {		
		ReportedMessageAttachment reportedMessageAttachment = new ReportedMessageAttachment();
		
		reportedMessageAttachment.setId(new Long(400));
		reportedMessageAttachment.setReportedAttachmentID(new Long(100));
		reportedMessageAttachment.setVersion(new Long(0));
		reportedMessageAttachment.setTimestamp(new Date());
		
		return reportedMessageAttachment;
	}

	public static ReportedRecipient getReportedRecipient(ReportedMessage reportedMessage) {
		ReportedRecipient raportoitavaVastaanottaja = new ReportedRecipient();
		
		raportoitavaVastaanottaja.setReportedMessage(reportedMessage);
		raportoitavaVastaanottaja.setRecipientOid("1.2.246.562.24.34397748041");
		raportoitavaVastaanottaja.setRecipientOidType("oppilas");
		raportoitavaVastaanottaja.setSocialSecurityID("");
		raportoitavaVastaanottaja.setRecipientEmail("testi.vastaanottaja@sposti.fi");
		raportoitavaVastaanottaja.setLanguageCode("FI");
		raportoitavaVastaanottaja.setSearchName("Testi Oppilas");
		raportoitavaVastaanottaja.setSendingStarted(new Date());
		raportoitavaVastaanottaja.setSendingEnded(new Date());
		raportoitavaVastaanottaja.setFailureReason("");
		raportoitavaVastaanottaja.setTimestamp(new Date());
		
		return raportoitavaVastaanottaja;
	}

	public static ReportedRecipient getReportedRecipient() {
		ReportedRecipient reportedRecipient = new ReportedRecipient();
		
		reportedRecipient.setRecipientOid("1.2.246.562.24.34397748041");
		reportedRecipient.setRecipientOidType("oppilas");
		reportedRecipient.setSocialSecurityID("");
		reportedRecipient.setRecipientEmail("testi.vastaanottaja@sposti.fi");
		reportedRecipient.setLanguageCode("FI");
		reportedRecipient.setSearchName("Testi,Oppilas");
		reportedRecipient.setSendingStarted(null);
		reportedRecipient.setSendingEnded(null);
		reportedRecipient.setFailureReason("");
		reportedRecipient.setTimestamp(new Date());
		
		return reportedRecipient;
	}
	
	public static ReportedAttachment getReportedAttachment() {
		ReportedAttachment raportoitavaLiite = new ReportedAttachment();
		
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		raportoitavaLiite.setAttachmentName("koekutsu.doc");
		raportoitavaLiite.setContentType("application/pdf");
		raportoitavaLiite.setAttachment(sisalto);
		raportoitavaLiite.setTimkestamp(new Date());
		
		return raportoitavaLiite; 		
	}
	
	public static SendingStatusDTO getSendingStatusDTO() {
		SendingStatusDTO sendingStatusDTO = new SendingStatusDTO();
		sendingStatusDTO.setMessageID(new Long(1));
		sendingStatusDTO.setNumberOfReciepients(new Long(10));
		sendingStatusDTO.setNumberOfFailedSendings(new Long(2));
		sendingStatusDTO.setNumberOfSuccesfulSendings(new Long(5));
		sendingStatusDTO.setSendingStarted(new Date());
		
		return sendingStatusDTO;
	}

    public static List<OrganisaatioHenkilo> getHenkilonOrganisaatiot() {
        List<OrganisaatioHenkilo> henkilonOrganisaatiot = new ArrayList<OrganisaatioHenkilo>();
        
        OrganisaatioHenkilo organisaatioHenkilo = new OrganisaatioHenkilo();
        organisaatioHenkilo.setOrganisaatioOid("1.2.246.562.10.00000000001");
        henkilonOrganisaatiot.add(organisaatioHenkilo);
        
        return henkilonOrganisaatiot;
    }
}
