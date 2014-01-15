package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenLopetusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenTilanneDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaVastaanottajaService;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaViestiService;
import fi.vm.sade.ryhmasahkoposti.service.RyhmasahkopostinRaportointiService;

@Service
@Transactional(readOnly=true)
public class RyhmasahkopostinRaportointiServiceImpl implements RyhmasahkopostinRaportointiService {
	private RaportoitavaViestiService raportoitavaViestiService;
	private RaportoitavaVastaanottajaService raportoitavaVastaanottajaService;

	@Autowired
	public RyhmasahkopostinRaportointiServiceImpl(RaportoitavaViestiService raportoitavaViestiService,
		RaportoitavaVastaanottajaService raportoitavaVastaanottajaService) {
		this.raportoitavaViestiService = raportoitavaViestiService;
		this.raportoitavaVastaanottajaService = raportoitavaVastaanottajaService;
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
	public RaportoitavaViesti haeRaportoitavatViesti(Long viestiID) {
		return raportoitavaViestiService.haeRaportoitavaViesti(viestiID);
	}

	@Override
	public List<RaportoitavaViesti> haeRaportoitavatViestit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RaportoitavaViesti> haeRaportoitavatViestit(String query) {
		return raportoitavaViestiService.haeRaportoitavatViestit(query);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Long raportoiLahetyksenAloitus(LahetyksenAloitusDTO lahetyksenAloitus) throws IOException {
		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.muodostaRaportoitavaViesti(lahetyksenAloitus);
		RaportoitavaViesti tallennettuRaportoitavaViesti = 
			raportoitavaViestiService.tallennaRaportoitavaViesti(raportoitavaViesti);
		
		List<RaportoitavaVastaanottaja> raportoitavatVastaanottajat = 
			raportoitavaVastaanottajaService.muodostaRaportoitavatVastaanottajat(tallennettuRaportoitavaViesti, 
			lahetyksenAloitus.getVastaanottajat());
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
	public String testaa() {
		return "RyhmasahkopostinRaportointiServiceImpl vastasi";
	}
}
