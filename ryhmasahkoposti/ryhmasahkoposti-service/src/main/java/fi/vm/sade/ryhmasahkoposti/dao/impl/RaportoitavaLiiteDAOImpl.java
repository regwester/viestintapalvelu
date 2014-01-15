package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavaLiiteDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;

@Repository
public class RaportoitavaLiiteDAOImpl extends AbstractJpaDAOImpl<RaportoitavaLiite, Long> implements RaportoitavaLiiteDAO {

	@Override
	public List<Long> tallennaRaportoitavatLiitteet(List<RaportoitavaLiite> liitteet) {
		// TODO Auto-generated method stub
		return null;
	}
}
