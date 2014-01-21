package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.RaportoitavaViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavaViestiDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaViestiService;

@Service
public class RaportoitavaViestiServiceImpl implements RaportoitavaViestiService {
	private RaportoitavaViestiDAO raportoitavaViestiDAO;
	
	@Autowired
	public RaportoitavaViestiServiceImpl(RaportoitavaViestiDAO raportoitavaViestiDAO) {
		this.raportoitavaViestiDAO = raportoitavaViestiDAO;
	}

	@Override
	public List<RaportoitavaViesti> haeRaportoitavatViestit() {
		return raportoitavaViestiDAO.findAll();
	}

	@Override
	public List<RaportoitavaViesti> haeRaportoitavatViestit(RaportoitavaViestiQueryDTO query) {
		return raportoitavaViestiDAO.findBySearchCriteria(query);
	}

	@Override
	public RaportoitavaViesti haeRaportoitavaViesti(Long id) {
		return raportoitavaViestiDAO.read(id);
	}

	@Override
	public RaportoitavaViesti muodostaRaportoitavaViesti(LahetyksenAloitusDTO lahetyksenAloitus) throws IOException {
		RaportoitavaViesti raportoitavaViesti = new RaportoitavaViesti();
		
		raportoitavaViesti.setAihe(lahetyksenAloitus.getAihe());
		raportoitavaViesti.setProsessi(lahetyksenAloitus.getProsessi());
		raportoitavaViesti.setLahettajanOid(lahetyksenAloitus.getLahettajanOid());
		raportoitavaViesti.setLahettajanOidTyyppi(lahetyksenAloitus.getLahettajanOidTyyppi());
		raportoitavaViesti.setLahettajanSahkopostiosoite(lahetyksenAloitus.getLahettajanSahkopostiosoite());
		raportoitavaViesti.setVastauksensaajanOid(lahetyksenAloitus.getVastauksensaajaOid());
		raportoitavaViesti.setVastauksensaajanOidTyyppi(lahetyksenAloitus.getVastauksenSaajanOidTyyppi());
		raportoitavaViesti.setVastauksensaajanSahkopostiosoite(lahetyksenAloitus.getVastauksensaajanSahkoposti());
		raportoitavaViesti.setAihe(lahetyksenAloitus.getAihe());
		raportoitavaViesti.setViesti(lahetyksenAloitus.getViesti());
		if (lahetyksenAloitus.isHtmlViesti()) {
			raportoitavaViesti.setHtmlViesti("html");
		} else {
			raportoitavaViesti.setHtmlViesti("");
		}
		raportoitavaViesti.setMerkisto(lahetyksenAloitus.getMerkisto());
		raportoitavaViesti.setLahetysAlkoi(lahetyksenAloitus.getLahetysAlkoi());
		raportoitavaViesti.setAikaleima(new Date());
		
		return raportoitavaViesti;
	}

	@Override
	public void paivitaRaportoitavaViesti(RaportoitavaViesti raportoitavaViesti) {
		raportoitavaViestiDAO.update(raportoitavaViesti);
	}

	@Override
	public RaportoitavaViesti tallennaRaportoitavaViesti(RaportoitavaViesti raportoitavaViesti) {
		return raportoitavaViestiDAO.insert(raportoitavaViesti);
	}
	
}
