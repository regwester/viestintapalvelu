package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenLopetusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenTilanneDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.RaportoitavaViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.converter.FileItemToRaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.converter.HakukenttaToRaportoitavaViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.converter.LahetettyVastaanottajalleDTOToRaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.converter.LahetyksenAloitusDTOToRaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.converter.RaportoitavaViestiToRaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaLiiteService;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaVastaanottajaService;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaViestiService;
import fi.vm.sade.ryhmasahkoposti.service.RyhmasahkopostinRaportointiService;

@Service
@Transactional(readOnly=true)
public class RyhmasahkopostinRaportointiServiceImpl implements RyhmasahkopostinRaportointiService {
	private RaportoitavaViestiService raportoitavaViestiService;
	private RaportoitavaVastaanottajaService raportoitavaVastaanottajaService;
	private RaportoitavaLiiteService raportoitavaLiiteService;

	@Autowired
	public RyhmasahkopostinRaportointiServiceImpl(RaportoitavaViestiService raportoitavaViestiService,
		RaportoitavaVastaanottajaService raportoitavaVastaanottajaService, 
		RaportoitavaLiiteService raportoitavaLiiteService) {
		this.raportoitavaViestiService = raportoitavaViestiService;
		this.raportoitavaVastaanottajaService = raportoitavaVastaanottajaService;
		this.raportoitavaLiiteService = raportoitavaLiiteService;
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
	public List<RaportoitavaVastaanottaja> haeRaportoitavatVastaanottajatViestiLahettamatta(int vastaanottajienLukumaara) {
		return raportoitavaVastaanottajaService.haeRaportoitavatVastaanottajatViestiLahettamatta(vastaanottajienLukumaara);
	}

	@Override
	public RaportoitavaViestiDTO haeRaportoitavaViesti(Long viestiID) {
		RaportoitavaViesti viesti = raportoitavaViestiService.haeRaportoitavaViesti(viestiID);
		return RaportoitavaViestiToRaportoitavaViestiDTO.convert(viesti);
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
		
		List<RaportoitavaLiite> raportoitavatLiitteet = 
			raportoitavaLiiteService.haeRaportoitavatLiitteet(lahetyksenAloitus.getLahetetynviestinliitteet());
		raportoitavaViesti.setRaportoitavatLiitteet(raportoitavatLiitteet);
		
		RaportoitavaViesti tallennettuRaportoitavaViesti = 
			raportoitavaViestiService.tallennaRaportoitavaViesti(raportoitavaViesti);
		
		List<RaportoitavaVastaanottaja> raportoitavatVastaanottajat = 
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
	public boolean raportoiLahetysVastaanottajalle(LahetettyVastaanottajalleDTO lahetettyVastaanottajalle) {
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
	public Long tallennaLiite(FileItem fileItem) throws IOException {
		RaportoitavaLiite liite = FileItemToRaportoitavaLiite.convert(fileItem);
		return raportoitavaLiiteService.tallennaRaportoitavaLiite(liite);
	}
}
