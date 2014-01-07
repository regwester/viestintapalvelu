package fi.vm.sade.ryhmasahkoposti.raportointi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.raportointi.dao.RaportoitavaViestiDAO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.query.RyhmasahkopostiViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.raportointi.service.RaportoitavaViestiService;

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
	public List<RaportoitavaViesti> haeRaportoitavatViestit(RyhmasahkopostiViestiQueryDTO query) {
		List<RaportoitavaViesti> raportoitavatViestit = new ArrayList<RaportoitavaViesti>();
		
		if (query.getLahettajanOid() != null && !query.getLahettajanOid().isEmpty()) {
			List<String> lahettajanOidList = new ArrayList<String>();
			lahettajanOidList.add(query.getLahettajanOid());
			
			raportoitavatViestit = raportoitavaViestiDAO.findLahettajanRaportoitavatViestit(lahettajanOidList);
		}
		
		return raportoitavatViestit;
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
		raportoitavaViesti.setLahetysAlkoi(lahetyksenAloitus.getLahetysAlkoi());
		
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
