package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavanViestinLiiteDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavanViestinLiite;

@Repository
public class RaportoitavanViestinLiiteDAOImpl extends AbstractJpaDAOImpl<RaportoitavanViestinLiite, Long> 
	implements RaportoitavanViestinLiiteDAO {

	@Override
	public List<RaportoitavanViestinLiite> haeRaportoitavanViestinLiitteet(Long viestinID) {
		EntityManager em = getEntityManager();
		
		String findViestinLiitteet = "SELECT a FROM RaportoitavanViestinLiite a JOIN a.raportoitavaviesti " + 
			"WHERE a.lahetysalkoi = null";
		TypedQuery<RaportoitavanViestinLiite> query = 
			em.createQuery(findViestinLiitteet, RaportoitavanViestinLiite.class);
		
		return query.getResultList();
	}
	
	protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }
}
