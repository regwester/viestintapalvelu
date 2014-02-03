package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
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
	public List<RaportoitavaViesti> haeRaportoitavatViestit(EmailMessageQueryDTO query) {
		return raportoitavaViestiDAO.findBySearchCriteria(query);
	}

	@Override
	public RaportoitavaViesti haeRaportoitavaViesti(Long id) {
		return raportoitavaViestiDAO.read(id);
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
