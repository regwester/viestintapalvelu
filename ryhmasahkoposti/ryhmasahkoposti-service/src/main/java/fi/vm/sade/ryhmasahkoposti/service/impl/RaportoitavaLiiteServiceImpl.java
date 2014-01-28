package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyLiiteDTO;
import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavaLiiteDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavanViestinLiite;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaLiiteService;

@Service
public class RaportoitavaLiiteServiceImpl implements RaportoitavaLiiteService {
	private RaportoitavaLiiteDAO raportoitavaLiiteDAO;
    
	@Autowired
    public RaportoitavaLiiteServiceImpl(RaportoitavaLiiteDAO raportoitavaLiiteDAO) {
		this.raportoitavaLiiteDAO = raportoitavaLiiteDAO;
	}   

	@Override
	public List<RaportoitavaLiite> haeRaportoitavanViestinLiitteet(Set<RaportoitavanViestinLiite> viestinLiitteet) {
		List<RaportoitavaLiite> liitteet = new ArrayList<RaportoitavaLiite>() ;
		
		for (RaportoitavanViestinLiite viestinLiite : viestinLiitteet) {
			RaportoitavaLiite liite = raportoitavaLiiteDAO.read(viestinLiite.getRaportoitavaliiteID());
			liitteet.add(liite);
		}
		
		return liitteet;	
	}

	@Override
	public List<RaportoitavaLiite> haeRaportoitavatLiitteet(List<LahetettyLiiteDTO> lahetetytLiitteet) {
		List<RaportoitavaLiite> liitteet = new ArrayList<RaportoitavaLiite>();
		
		for (LahetettyLiiteDTO lahetettyLiite : lahetetytLiitteet) {
			RaportoitavaLiite liite = raportoitavaLiiteDAO.read(lahetettyLiite.getLiitetiedostonID());
			liitteet.add(liite);
		}
		
		return liitteet;
	}

	@Override
	public Long tallennaRaportoitavaLiite(RaportoitavaLiite raportoitavatLiite) {
		raportoitavatLiite = raportoitavaLiiteDAO.insert(raportoitavatLiite);
		return raportoitavatLiite.getId();
	}
}
