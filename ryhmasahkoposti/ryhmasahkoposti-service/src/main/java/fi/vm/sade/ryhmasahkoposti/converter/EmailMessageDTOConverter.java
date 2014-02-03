package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachmentDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;

@Component
public class EmailMessageDTOConverter {
	public static List<EmailMessageDTO> convert(List<RaportoitavaViesti> raportoitavatViestit) {
		List<EmailMessageDTO> emailMessageDTO = new ArrayList<EmailMessageDTO>();
		
		for (RaportoitavaViesti raportoitavaViesti : raportoitavatViestit) {			
			emailMessageDTO.add(convertToEmailMessageDTO(raportoitavaViesti));
		}
		
		return emailMessageDTO;
	}

	public static EmailMessageDTO convertToEmailMessageDTO(RaportoitavaViesti raportoitavaViesti, 
		List<RaportoitavaLiite> raportoitavatLiitteet) {
		
		EmailMessageDTO emailMessageDTO = convertToEmailMessageDTO(raportoitavaViesti);
		emailMessageDTO.setAttachmentDTOs(convertEmailAttachmentDTO(raportoitavatLiitteet));
		emailMessageDTO.setRecipients(convertEmailRecipientDTO(raportoitavaViesti));
		
		return emailMessageDTO;
	}
	
	private static List<EmailRecipientDTO> convertEmailRecipientDTO(
		RaportoitavaViesti raportoitavaViesti) {
		List<EmailRecipientDTO> vastaanottajat = new ArrayList<EmailRecipientDTO>();
		
		for (RaportoitavaVastaanottaja vastaanottaja : raportoitavaViesti.getRaportoitavatVastaanottajat()) {
			EmailRecipientDTO emailRecipientDTO = new EmailRecipientDTO();
			
			emailRecipientDTO.setRecipientID(vastaanottaja.getId());
			emailRecipientDTO.setSendSuccessfull(vastaanottaja.getLahetysOnnistui());
			emailRecipientDTO.setOid(vastaanottaja.getVastaanottajaOid());
			emailRecipientDTO.setEmail(vastaanottaja.getVastaanottajanSahkoposti());
			
			vastaanottajat.add(emailRecipientDTO);
		}
		
		return vastaanottajat;
	}

	private static List<EmailAttachmentDTO> convertEmailAttachmentDTO(List<RaportoitavaLiite> raportoitavatLiitteet) {
		List<EmailAttachmentDTO> attachments = new ArrayList<EmailAttachmentDTO>();
		
		for (RaportoitavaLiite liite : raportoitavatLiitteet) {
			EmailAttachmentDTO attachmentDTO = new EmailAttachmentDTO();
			attachmentDTO.setAttachmentID(liite.getId());
			attachmentDTO.setName(liite.getLiitetiedostonNimi());
			attachmentDTO.setData(liite.getLiitetiedosto());
			attachmentDTO.setContentType(liite.getSisaltotyyppi());
			attachments.add(attachmentDTO);
		}
			
		return attachments;
	}

	private static EmailMessageDTO convertToEmailMessageDTO(RaportoitavaViesti raportoitavaViesti) {
		EmailMessageDTO emailMessageDTO = new EmailMessageDTO();
		
		emailMessageDTO.setMessageID(raportoitavaViesti.getId());
		emailMessageDTO.setSubject(raportoitavaViesti.getAihe());
		emailMessageDTO.setSenderEmail(raportoitavaViesti.getLahettajanSahkopostiosoite());
		emailMessageDTO.setStartTime(raportoitavaViesti.getLahetysAlkoi());
		emailMessageDTO.setEndTime(raportoitavaViesti.getLahetysPaattyi());
		emailMessageDTO.setCallingProcess(raportoitavaViesti.getProsessi());
		emailMessageDTO.setReplyToAddress(raportoitavaViesti.getVastauksensaajanSahkopostiosoite());
		emailMessageDTO.setBody(raportoitavaViesti.getViesti());
		
		return emailMessageDTO;
	}
}
