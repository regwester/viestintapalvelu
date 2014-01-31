package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenLopetusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenTilanneDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaVastaanottajaDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.RaportoitavaViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.converter.FileItemToRaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.converter.HakukenttaToRaportoitavaViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.converter.LahetettyVastaanottajalleDTOToRaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.converter.LahetyksenAloitusDTOToRaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.converter.RaportoitavaVastaanottajaToRaportoitavaVastaanottajaDTO;
import fi.vm.sade.ryhmasahkoposti.converter.RaportoitavaViestiToRaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaLiiteService;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaVastaanottajaService;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaViestiService;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavanViestinLiiteService;
import fi.vm.sade.ryhmasahkoposti.service.RyhmasahkopostinRaportointiService;

@Service
@Transactional(readOnly=true)
public class RyhmasahkopostinRaportointiServiceImpl implements RyhmasahkopostinRaportointiService {
	private RaportoitavaViestiService raportoitavaViestiService;
	private RaportoitavaVastaanottajaService raportoitavaVastaanottajaService;
	private RaportoitavaLiiteService raportoitavaLiiteService;
	private RaportoitavanViestinLiiteService raportoitavanViestinLiiteService;

	@Autowired
	public RyhmasahkopostinRaportointiServiceImpl(RaportoitavaViestiService raportoitavaViestiService,
		RaportoitavaVastaanottajaService raportoitavaVastaanottajaService, 
		RaportoitavaLiiteService raportoitavaLiiteService, 
		RaportoitavanViestinLiiteService raportoitavanViestinLiiteService) {
		this.raportoitavaViestiService = raportoitavaViestiService;
		this.raportoitavaVastaanottajaService = raportoitavaVastaanottajaService;
		this.raportoitavaLiiteService = raportoitavaLiiteService;
		this.raportoitavanViestinLiiteService = raportoitavanViestinLiiteService;
	}
	
	@Override
	public LahetyksenTilanneDTO haeLahetyksenTulos(Long viestiID) {
		LahetyksenTilanneDTO lahetyksenTulos = new LahetyksenTilanneDTO();
		
		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.haeRaportoitavaViesti(viestiID);
		
		lahetyksenTulos.setViestiID(viestiID);
		lahetyksenTulos.setLahetysAlkoi(raportoitavaViesti.getLahetysAlkoi());
		lahetyksenTulos.setLahetysPaattyi(raportoitavaViesti.getLahetysPaattyi());
		lahetyksenTulos.setVastaanottajienLukumaara(
			raportoitavaVastaanottajaService.haeRaportoitavienVastaanottajienLukumaara(viestiID));
		lahetyksenTulos.setLahetysOnnistuiLukumaara(
			raportoitavaVastaanottajaService.haeRaportoitavienVastaanottajienLukumaara(viestiID, true));
		lahetyksenTulos.setLahetysEpaonnistuiLukumaara(
			raportoitavaVastaanottajaService.haeRaportoitavienVastaanottajienLukumaara(viestiID, false));
		
		return lahetyksenTulos;
	}

	@Override
	public List<EmailRecipientDTO> getUnhandledMessageRecipients(int listSize) {
		List<RaportoitavaVastaanottaja> vastaanottajat = raportoitavaVastaanottajaService.haeRaportoitavatVastaanottajatViestiLahettamatta(listSize);
		return RaportoitavaVastaanottajaToRaportoitavaVastaanottajaDTO.convertToEmailRecipientDTO(vastaanottajat);
	}
	
	@Override
	public List<RaportoitavaVastaanottajaDTO> haeRaportoitavatVastaanottajatViestiLahettamatta(int vastaanottajienLukumaara) {
		List<RaportoitavaVastaanottaja> vastaanottajat = 
			raportoitavaVastaanottajaService.haeRaportoitavatVastaanottajatViestiLahettamatta(vastaanottajienLukumaara);
		return RaportoitavaVastaanottajaToRaportoitavaVastaanottajaDTO.convert(vastaanottajat);
	}

	@Override
	public RaportoitavaViestiDTO haeRaportoitavaViesti(Long viestiID, boolean lahetysRaportti) {
		RaportoitavaViesti viesti = raportoitavaViestiService.haeRaportoitavaViesti(viestiID);
		List<RaportoitavaLiite> liitteet = 
			raportoitavaLiiteService.haeRaportoitavanViestinLiitteet(viesti.getRaportoitavanViestinLiitteet());
		return RaportoitavaViestiToRaportoitavaViestiDTO.convert(viesti, liitteet, lahetysRaportti);
	}

	public EmailMessageDTO getMessage(Long viestiID) {
		RaportoitavaViesti viesti = raportoitavaViestiService.haeRaportoitavaViesti(viestiID);
		List<RaportoitavaLiite> liitteet = 
			raportoitavaLiiteService.haeRaportoitavanViestinLiitteet(viesti.getRaportoitavanViestinLiitteet());
		return RaportoitavaViestiToRaportoitavaViestiDTO.convertToEmailMessageDTO(viesti, liitteet);
	}

	
	@Override
	public List<RaportoitavaViestiDTO> haeRaportoitavatViestit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RaportoitavaViestiDTO> haeRaportoitavatViestit(String hakuKentta) {
		RaportoitavaViestiQueryDTO query = HakukenttaToRaportoitavaViestiQueryDTO.convert(hakuKentta);
		List<RaportoitavaViesti> raportoitavatViestit = raportoitavaViestiService.haeRaportoitavatViestit(query);
		
		return RaportoitavaViestiToRaportoitavaViestiDTO.convert(raportoitavatViestit);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Long raportoiLahetyksenAloitus(LahetyksenAloitusDTO lahetyksenAloitus) throws IOException {
		RaportoitavaViesti raportoitavaViesti = 
			LahetyksenAloitusDTOToRaportoitavaViesti.convert(lahetyksenAloitus);
			
		RaportoitavaViesti tallennettuRaportoitavaViesti = 
			raportoitavaViestiService.tallennaRaportoitavaViesti(raportoitavaViesti);
	
		List<RaportoitavaLiite> raportoitavatLiitteet = 
			raportoitavaLiiteService.haeRaportoitavatLiitteet(lahetyksenAloitus.getLahetetynviestinliitteet());		
		raportoitavanViestinLiiteService.tallennaRaportoitavanViestinLiitteet(
			tallennettuRaportoitavaViesti, raportoitavatLiitteet);

		Set<RaportoitavaVastaanottaja> raportoitavatVastaanottajat = 
			LahetettyVastaanottajalleDTOToRaportoitavaVastaanottaja.convert(
			tallennettuRaportoitavaViesti, lahetyksenAloitus.getVastaanottajat());
		raportoitavaVastaanottajaService.tallennaRaportoitavatVastaanottajat(raportoitavatVastaanottajat);		
		
		return tallennettuRaportoitavaViesti.getId();
	}

	@Override
	public boolean raportoiLahetyksenLopetus(LahetyksenLopetusDTO lahetyksenLopetus) {
		RaportoitavaViesti raportoitavaViesti = 
			raportoitavaViestiService.haeRaportoitavaViesti(lahetyksenLopetus.getViestiID());
		
		if (raportoitavaViesti.getLahetysPaattyi() != null) {
			return false;
		}
		
		raportoitavaViesti.setLahetysPaattyi(lahetyksenLopetus.getLahetysPaattyi());
		raportoitavaViestiService.paivitaRaportoitavaViesti(raportoitavaViesti);
		
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean raportoiLahetyksenTilanne(LahetettyVastaanottajalleDTO lahetettyVastaanottajalle) {
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
			raportoitavaVastaanottajaService.haeRaportoitavaVastaanottaja(lahetettyVastaanottajalle.getViestiID(), 
			lahetettyVastaanottajalle.getVastaanottajanSahkoposti());
		
		if (lahetettyVastaanottajalle.getLahetysalkoi() != null && lahetettyVastaanottajalle.getLahetyspaattyi() == null) {
			if (raportoitavaVastaanottaja.getLahetysalkoi() != null) {
				return false;
			}
		}

		if (lahetettyVastaanottajalle.getLahetyspaattyi() != null && raportoitavaVastaanottaja.getLahetyspaattyi() != null) {
			return false;
		}
		
		raportoitavaVastaanottaja = raportoitavaVastaanottajaService.taydennaRaportoitavaaVastaanottajaa(
			raportoitavaVastaanottaja, lahetettyVastaanottajalle);
		
		raportoitavaVastaanottajaService.paivitaRaportoitavaVastaanottaja(raportoitavaVastaanottaja);
		
		return true;
	}

	@Override
	public boolean startSending(EmailRecipientDTO recipient) {
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
				raportoitavaVastaanottajaService.haeRaportoitavaVastaanottaja(recipient.getEmailMessageID(), 
				recipient.getEmail());
		if (raportoitavaVastaanottaja.getLahetysalkoi() != null) {
			return false;
		}
		raportoitavaVastaanottaja.setLahetysalkoi(new Date());
		raportoitavaVastaanottajaService.paivitaRaportoitavaVastaanottaja(raportoitavaVastaanottaja);
		return true;
	}
	@Override
	public boolean recipientHandledFailure(EmailRecipientDTO recipient, String result) {

		RaportoitavaVastaanottaja raportoitavaVastaanottaja = raportoitavaVastaanottajaService
				.haeRaportoitavaVastaanottaja(recipient.getEmailMessageID(),
						recipient.getEmail());
		raportoitavaVastaanottaja.setEpaonnistumisenSyy(result);
		raportoitavaVastaanottaja.setLahetyspaattyi(new Date());
		raportoitavaVastaanottajaService
				.paivitaRaportoitavaVastaanottaja(raportoitavaVastaanottaja);
		return true;
	}
	
	@Override
	public boolean recipientHandledSuccess(EmailRecipientDTO recipient, String result) {
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
				raportoitavaVastaanottajaService.haeRaportoitavaVastaanottaja(recipient.getEmailMessageID(), 
				recipient.getEmail());;
		raportoitavaVastaanottaja.setLahetysOnnistui(result);
		raportoitavaVastaanottaja.setLahetyspaattyi(new Date());
		raportoitavaVastaanottajaService.paivitaRaportoitavaVastaanottaja(raportoitavaVastaanottaja);
		return true;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Long tallennaLiite(FileItem fileItem) throws IOException {
		RaportoitavaLiite liite = FileItemToRaportoitavaLiite.convert(fileItem);
		return raportoitavaLiiteService.tallennaRaportoitavaLiite(liite);
	}
}
