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

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenTilanneDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.converter.EmailMessageDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.EmailMessageQueryDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.RaportoitavaLiiteConverter;
import fi.vm.sade.ryhmasahkoposti.converter.RaportoitavaVastaanottajaConverter;
import fi.vm.sade.ryhmasahkoposti.converter.RaportoitavaVastaanottajaToRaportoitavaVastaanottajaDTO;
import fi.vm.sade.ryhmasahkoposti.converter.RaportoitavaViestiConverter;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaLiiteService;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaVastaanottajaService;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaViestiService;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavanViestinLiiteService;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;

@Service
@Transactional(readOnly=true)
public class GroupEmailReportingServiceImpl implements GroupEmailReportingService {
	private RaportoitavaViestiService raportoitavaViestiService;
	private RaportoitavaVastaanottajaService raportoitavaVastaanottajaService;
	private RaportoitavaLiiteService raportoitavaLiiteService;
	private RaportoitavanViestinLiiteService raportoitavanViestinLiiteService;

	@Autowired
	public GroupEmailReportingServiceImpl(RaportoitavaViestiService raportoitavaViestiService,
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
		List<RaportoitavaVastaanottaja> vastaanottajat = 
			raportoitavaVastaanottajaService.haeRaportoitavatVastaanottajatViestiLahettamatta(listSize);
		return RaportoitavaVastaanottajaToRaportoitavaVastaanottajaDTO.convertToEmailRecipientDTO(vastaanottajat);
	}
	
	@Override
	public EmailMessageDTO getMessage(Long viestiID) {
		RaportoitavaViesti viesti = raportoitavaViestiService.haeRaportoitavaViesti(viestiID);
		List<RaportoitavaLiite> liitteet = 
			raportoitavaLiiteService.haeRaportoitavanViestinLiitteet(viesti.getRaportoitavanViestinLiitteet());
		return EmailMessageDTOConverter.convertToEmailMessageDTO(viesti, liitteet);
	}

	
	@Override
	public List<EmailMessageDTO> getMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailMessageDTO> getMessages(String searchArgument) {
		EmailMessageQueryDTO query = EmailMessageQueryDTOConverter.convert(searchArgument);
		List<RaportoitavaViesti> raportoitavatViestit = raportoitavaViestiService.haeRaportoitavatViestit(query);
		
		return EmailMessageDTOConverter.convert(raportoitavatViestit);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Long saveSendingEmail(EmailData emailData) throws IOException {
		RaportoitavaViesti raportoitavaViesti = 
			RaportoitavaViestiConverter.convert(emailData.getEmail());
			
		RaportoitavaViesti tallennettuRaportoitavaViesti = 
			raportoitavaViestiService.tallennaRaportoitavaViesti(raportoitavaViesti);
	
		List<RaportoitavaLiite> raportoitavatLiitteet = 
			raportoitavaLiiteService.haeRaportoitavatLiitteet(emailData.getEmail().getAttachInfo());		
		raportoitavanViestinLiiteService.tallennaRaportoitavanViestinLiitteet(
			tallennettuRaportoitavaViesti, raportoitavatLiitteet);

		Set<RaportoitavaVastaanottaja> raportoitavatVastaanottajat = 
			RaportoitavaVastaanottajaConverter.convert(
			tallennettuRaportoitavaViesti, emailData.getRecipient());
		raportoitavaVastaanottajaService.tallennaRaportoitavatVastaanottajat(raportoitavatVastaanottajat);		
		
		return tallennettuRaportoitavaViesti.getId();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean startSending(EmailRecipientDTO recipient) {
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
			raportoitavaVastaanottajaService.haeRaportoitavaVastaanottaja(
			recipient.getEmailMessageID(), recipient.getEmail());
		
		if (raportoitavaVastaanottaja.getLahetysalkoi() != null) {
			return false;
		}
		
		raportoitavaVastaanottaja.setLahetysalkoi(new Date());
		raportoitavaVastaanottajaService.paivitaRaportoitavaVastaanottaja(raportoitavaVastaanottaja);
		
		return true;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean recipientHandledFailure(EmailRecipientDTO recipient, String result) {
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
			raportoitavaVastaanottajaService.haeRaportoitavaVastaanottaja(
			recipient.getEmailMessageID(), recipient.getEmail());
		
		raportoitavaVastaanottaja.setEpaonnistumisenSyy(result);
		raportoitavaVastaanottaja.setLahetyspaattyi(new Date());
		
		raportoitavaVastaanottajaService.paivitaRaportoitavaVastaanottaja(raportoitavaVastaanottaja);
		
		return true;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean recipientHandledSuccess(EmailRecipientDTO recipient, String result) {
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
			raportoitavaVastaanottajaService.haeRaportoitavaVastaanottaja(
			recipient.getEmailMessageID(), recipient.getEmail());
		
		raportoitavaVastaanottaja.setLahetysOnnistui(result);
		raportoitavaVastaanottaja.setLahetyspaattyi(new Date());
		
		raportoitavaVastaanottajaService.paivitaRaportoitavaVastaanottaja(raportoitavaVastaanottaja);
		
		return true;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Long saveAttachment(FileItem fileItem) throws IOException {
		RaportoitavaLiite liite = RaportoitavaLiiteConverter.convert(fileItem);
		return raportoitavaLiiteService.tallennaRaportoitavaLiite(liite);
	}
}
