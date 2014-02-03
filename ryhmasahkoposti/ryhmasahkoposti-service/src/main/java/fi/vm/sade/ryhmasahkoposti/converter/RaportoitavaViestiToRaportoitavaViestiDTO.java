package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachmentDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaLiiteDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaVastaanottajaDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaVastaanottajaService;
import fi.vm.sade.ryhmasahkoposti.util.MessageUtil;

@Component
public class RaportoitavaViestiToRaportoitavaViestiDTO {
	private static RaportoitavaVastaanottajaService raportoitavaVastaanottajaService;

	@Autowired
	public RaportoitavaViestiToRaportoitavaViestiDTO(RaportoitavaVastaanottajaService raportoitavaVastaanottajaService) {
		RaportoitavaViestiToRaportoitavaViestiDTO.raportoitavaVastaanottajaService = raportoitavaVastaanottajaService;
	}

	public static List<RaportoitavaViestiDTO> convert(List<RaportoitavaViesti> raportoitavatViestit) {
		List<RaportoitavaViestiDTO> raportoitavatViestitDTO = new ArrayList<RaportoitavaViestiDTO>();
		
		for (RaportoitavaViesti raportoitavaViesti : raportoitavatViestit) {			
			raportoitavatViestitDTO.add(convertRaportoivaViesti(raportoitavaViesti, true));
		}
		
		return raportoitavatViestitDTO;
	}
	
	public static RaportoitavaViestiDTO convert(RaportoitavaViesti raportoitavaViesti, 
		List<RaportoitavaLiite> raportoitavatLiitteet, boolean lahetysRaportti) {
		RaportoitavaViestiDTO raportoitavaViestiDTO = convertRaportoivaViesti(raportoitavaViesti, lahetysRaportti);
		raportoitavaViestiDTO.setLiitetiedostot(convertRaportoitavatLiitteet(raportoitavatLiitteet));
		raportoitavaViestiDTO.setVastaanottajat(convertRaportoitavatVastaanottajat(raportoitavaViesti));

		return raportoitavaViestiDTO;
	}

	public static EmailMessageDTO convertToEmailMessageDTO(RaportoitavaViesti raportoitavaViesti, 
			List<RaportoitavaLiite> raportoitavatLiitteet) {
		EmailMessageDTO emailMessageDTO = convertToEmailMessageDTO(raportoitavaViesti);
		emailMessageDTO.setAttachments(convertEmailAttachmentDTO(raportoitavatLiitteet));
		return emailMessageDTO;
	}
	
	private static List<RaportoitavaVastaanottajaDTO> convertRaportoitavatVastaanottajat(
		RaportoitavaViesti raportoitavaViesti) {
		List<RaportoitavaVastaanottajaDTO> vastaanottajat = new ArrayList<RaportoitavaVastaanottajaDTO>();
		
		for (RaportoitavaVastaanottaja vastaanottaja : raportoitavaViesti.getRaportoitavatVastaanottajat()) {
			RaportoitavaVastaanottajaDTO raportoitavaVastaanottajaDTO = new RaportoitavaVastaanottajaDTO();
			
			raportoitavaVastaanottajaDTO.setVastaanottajaID(vastaanottaja.getId());
			raportoitavaVastaanottajaDTO.setEtunimi("");
			raportoitavaVastaanottajaDTO.setSukunimi(""); 
			raportoitavaVastaanottajaDTO.setLahetysOnnistui(vastaanottaja.getLahetysOnnistui());
			raportoitavaVastaanottajaDTO.setOrganisaationNimi("");
			raportoitavaVastaanottajaDTO.setVastaanottajanOid(vastaanottaja.getVastaanottajaOid());
			raportoitavaVastaanottajaDTO.setVastaanottajanSahkopostiosoite(vastaanottaja.getVastaanottajanSahkoposti());
			
			vastaanottajat.add(raportoitavaVastaanottajaDTO);
		}
		
		return vastaanottajat;
	}

	private static List<EmailAttachment> convertEmailAttachmentDTO(List<RaportoitavaLiite> raportoitavatLiitteet) {
		List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
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

	private static List<RaportoitavaLiiteDTO> convertRaportoitavatLiitteet(List<RaportoitavaLiite> raportoitavatLiitteet) {
		List<RaportoitavaLiiteDTO> liitteidenNimet = new ArrayList<RaportoitavaLiiteDTO>();
		
		for (RaportoitavaLiite liite : raportoitavatLiitteet) {
			RaportoitavaLiiteDTO liiteDTO = new RaportoitavaLiiteDTO();
			
			liiteDTO.setLiiteID(liite.getId());
			liiteDTO.setLiitetiedostonNimi(liite.getLiitetiedostonNimi());
			liiteDTO.setLiitetiedosto(liite.getLiitetiedosto());
			liiteDTO.setSisaltotyyppi(liite.getSisaltotyyppi());
			
			liitteidenNimet.add(liiteDTO);
		}
			
		return liitteidenNimet;
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
	
	private static RaportoitavaViestiDTO convertRaportoivaViesti(RaportoitavaViesti raportoitavaViesti, 
		boolean lahetysRaportti) {
		RaportoitavaViestiDTO raportoitavaViestiDTO = new RaportoitavaViestiDTO();
		
		raportoitavaViestiDTO.setViestiID(raportoitavaViesti.getId());
		raportoitavaViestiDTO.setAihe(raportoitavaViesti.getAihe());
		raportoitavaViestiDTO.setLahettajanSahkopostiosoite(raportoitavaViesti.getLahettajanSahkopostiosoite());
		raportoitavaViestiDTO.setLahetysAlkoi(raportoitavaViesti.getLahetysAlkoi());
		raportoitavaViestiDTO.setLahetysPaattyi(raportoitavaViesti.getLahetysPaattyi());
		
		if (lahetysRaportti) {
			raportoitavaViestiDTO.setLahetysraportti(muodostaLahetysraportti(raportoitavaViesti));
		}
		
		raportoitavaViestiDTO.setProsessi(raportoitavaViesti.getProsessi());
		raportoitavaViestiDTO.setVastauksenSaajanSahkopostiosoite(
			raportoitavaViesti.getVastauksensaajanSahkopostiosoite());
		raportoitavaViestiDTO.setViestinSisalto(raportoitavaViesti.getViesti());
		
		return raportoitavaViestiDTO;
	}

	private static String muodostaLahetysraportti(RaportoitavaViesti raportoitavaViesti) {
		Long epaonnistuneidenLkm = raportoitavaVastaanottajaService.haeRaportoitavienVastaanottajienLukumaara(
			raportoitavaViesti.getId(), false);
		
		if (epaonnistuneidenLkm != null && epaonnistuneidenLkm.compareTo(new Long(0)) > 0) {
			Object[] parametrit = {epaonnistuneidenLkm};
			return MessageUtil.getMessage("ryhmasahkoposti.lahetys_epaonnistui", parametrit);
		}
		
		if (raportoitavaViesti.getLahetysAlkoi() != null && raportoitavaViesti.getLahetysPaattyi() == null) {
			return MessageUtil.getMessage("ryhmasahkoposti.lahetys_kesken");
		}
			
		if (raportoitavaViesti.getLahetysAlkoi() != null && raportoitavaViesti.getLahetysPaattyi() != null) {
			return MessageUtil.getMessage("ryhmasahkoposti.lahetys_onnistui");
		}
  			
		return "";
	}
	
	
}
