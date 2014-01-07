package fi.vm.sade.ryhmasahkoposti.raportointi.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenLopetusDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenTulosDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.query.RyhmasahkopostiViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.raportointi.service.RaportoitavaVastaanottajaService;
import fi.vm.sade.ryhmasahkoposti.raportointi.service.RaportoitavaViestiService;
import fi.vm.sade.ryhmasahkoposti.raportointi.service.RyhmasahkopostinRaportointiService;

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
	public LahetyksenTulosDTO haeLahetyksenTulos(Long viestiID) {
		LahetyksenTulosDTO lahetyksenTulos = new LahetyksenTulosDTO();
		
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
	public List<RaportoitavaViesti> haeRaportoitavatViestit(RyhmasahkopostiViestiQueryDTO query) {
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
	public void raportoiLahetyksenLopetus(LahetyksenLopetusDTO lahetyksenLopetus) {
		RaportoitavaViesti raportoitavaViesti = 
			raportoitavaViestiService.haeRaportoitavaViesti(lahetyksenLopetus.getViestiID());
		
		raportoitavaViesti.setLahetysPaattyi(lahetyksenLopetus.getLahetysPaattyi());
		raportoitavaViestiService.paivitaRaportoitavaViesti(raportoitavaViesti);	
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void raportoiLahetysVastaanottajalle(LahetettyVastaanottajalleDTO lahetettyVastaanottajalle) {
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
			raportoitavaVastaanottajaService.haeRaportoitavaVastaanottaja(lahetettyVastaanottajalle.getViestiID(), 
			lahetettyVastaanottajalle.getVastaanottajanSahkoposti());
				 
		raportoitavaVastaanottaja = raportoitavaVastaanottajaService.taydennaRaportoitavaaVastaanottajaa(
			raportoitavaVastaanottaja, lahetettyVastaanottajalle);
		
		raportoitavaVastaanottajaService.paivitaRaportoitavaVastaanottaja(raportoitavaVastaanottaja);		
	}
}
