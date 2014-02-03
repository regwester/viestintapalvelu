package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaVastaanottajaDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;

@Component
public class RaportoitavaVastaanottajaToRaportoitavaVastaanottajaDTO {

	public static EmailRecipientDTO convertToEmailRecipientDTO(RaportoitavaVastaanottaja vastaanottaja) {
		EmailRecipientDTO emailRecipientDTO = new EmailRecipientDTO();
		
		emailRecipientDTO.setRecipientID(vastaanottaja.getId());
		emailRecipientDTO.setFirstName("");
		emailRecipientDTO.setLastName("");
		emailRecipientDTO.setOrganizationName("");
		emailRecipientDTO.setOid(vastaanottaja.getVastaanottajaOid());
		emailRecipientDTO.setEmail(vastaanottaja.getVastaanottajanSahkoposti());
		emailRecipientDTO.setEmailMessageID(vastaanottaja.getRaportoitavaviesti().getId());
		
		return emailRecipientDTO;
	}

	public static RaportoitavaVastaanottajaDTO convert(RaportoitavaVastaanottaja vastaanottaja) {
		RaportoitavaVastaanottajaDTO vastaanottajaDTO = new RaportoitavaVastaanottajaDTO();

		vastaanottajaDTO.setVastaanottajaID(vastaanottaja.getId());
		vastaanottajaDTO.setEtunimi("");
		vastaanottajaDTO.setSukunimi("");
		vastaanottajaDTO.setOrganisaationNimi("");
		vastaanottajaDTO.setVastaanottajanOid(vastaanottaja.getVastaanottajaOid());
		vastaanottajaDTO.setVastaanottajanSahkopostiosoite(vastaanottaja.getVastaanottajanSahkoposti());
		vastaanottajaDTO.setViestiID(vastaanottaja.getRaportoitavaviesti().getId());

		return vastaanottajaDTO;
	}

	public static List<RaportoitavaVastaanottajaDTO> convert(
			List<RaportoitavaVastaanottaja> vastaanottajat) {
		List<RaportoitavaVastaanottajaDTO> vastaanottajatDTO = new ArrayList<RaportoitavaVastaanottajaDTO>();

		for (RaportoitavaVastaanottaja vastaanottaja : vastaanottajat) {
			vastaanottajatDTO.add(convert(vastaanottaja));
		}
		return vastaanottajatDTO;
	}

	public static List<EmailRecipientDTO> convertToEmailRecipientDTO(
			List<RaportoitavaVastaanottaja> vastaanottajat) {
		List<EmailRecipientDTO> emailRecipientDTOs = new ArrayList<EmailRecipientDTO>();

		for (RaportoitavaVastaanottaja vastaanottaja : vastaanottajat) {
			emailRecipientDTOs.add(convertToEmailRecipientDTO(vastaanottaja));
		}
		return emailRecipientDTOs;
	}

}
