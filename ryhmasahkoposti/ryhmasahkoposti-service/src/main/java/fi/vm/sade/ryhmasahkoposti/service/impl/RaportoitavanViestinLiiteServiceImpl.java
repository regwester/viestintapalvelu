package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavanViestinLiiteDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavanViestinLiite;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavanViestinLiiteService;

@Service
public class RaportoitavanViestinLiiteServiceImpl implements RaportoitavanViestinLiiteService {
	private RaportoitavanViestinLiiteDAO raportoitavanViestinLiiteDAO;

	@Autowired
	public RaportoitavanViestinLiiteServiceImpl(RaportoitavanViestinLiiteDAO raportoitavanViestinLiiteDAO) {
		this.raportoitavanViestinLiiteDAO = raportoitavanViestinLiiteDAO;
	}
	
	@Override
	public List<RaportoitavanViestinLiite> haeRaportoitavanViestinLiitteet(Long viestinID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tallennaRaportoitavanViestinLiitteet(RaportoitavaViesti raportoitavaViesti,
		List<RaportoitavaLiite> raportoitavatLiitteet) {		
		for (RaportoitavaLiite liite : raportoitavatLiitteet) {
			RaportoitavanViestinLiite raportoitavanViestinLiite = new RaportoitavanViestinLiite();
			raportoitavanViestinLiite.setRaportoitavaviesti(raportoitavaViesti);
			raportoitavanViestinLiite.setRaportoitavaliiteID(liite.getId());
			raportoitavanViestinLiite.setAikaleima(new Date());
			
			raportoitavanViestinLiiteDAO.insert(raportoitavanViestinLiite);
		}
	}

}
