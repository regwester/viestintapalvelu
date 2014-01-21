package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavaVastaanottajaDAO;
import fi.vm.sade.ryhmasahkoposti.model.QRaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.QRaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;

@Repository
public class RaportoitavaVastaanottajaDAOImpl extends AbstractJpaDAOImpl<RaportoitavaVastaanottaja, Long> 
	implements RaportoitavaVastaanottajaDAO {
	@Override
	public List<RaportoitavaVastaanottaja> findLahettamattomat() {
		EntityManager em = getEntityManager();
		
		String findLahettamattomat = "SELECT a FROM RaportoitavaVastaanottaja a JOIN a.raportoitavaviesti " + 
			"WHERE a.lahetysalkoi = null";
		TypedQuery<RaportoitavaVastaanottaja> query = 
			em.createQuery(findLahettamattomat, RaportoitavaVastaanottaja.class);
		
		return query.getResultList();
	}

	@Override
	public RaportoitavaVastaanottaja findByLahetettyviestiIdAndVastaanottajanSahkopostiosoite(Long viestiID,
		String vastaanottajanSahkopostiosoite) {
		QRaportoitavaVastaanottaja raportoitavaVastaanottaja = QRaportoitavaVastaanottaja.raportoitavaVastaanottaja;
		QRaportoitavaViesti raportoitavaViesti = QRaportoitavaViesti.raportoitavaViesti;
		
		BooleanExpression whereExpression = raportoitavaVastaanottaja.raportoitavaviesti.id.eq(viestiID);
		whereExpression = raportoitavaVastaanottaja.vastaanottajanSahkoposti.eq(vastaanottajanSahkopostiosoite);
		
		return from(raportoitavaVastaanottaja).join(
			raportoitavaVastaanottaja.raportoitavaviesti, raportoitavaViesti).where(
			whereExpression).singleResult(raportoitavaVastaanottaja);
	}

	@Override
	public Long findVastaanottajienLukumaaraByViestiID(Long viestiID) {
		EntityManager em = getEntityManager();
		
		String findVastaanottajienLukumaara = "SELECT COUNT(*) FROM RaportoitavaVastaanottaja a " + 
			"JOIN a.raportoitavaviesti WHERE a.raportoitavaviesti.id = :viestiID";
		TypedQuery<Long> query = em.createQuery(findVastaanottajienLukumaara, Long.class);
		query.setParameter("viestiID", viestiID);
		
		return query.getSingleResult();
	}
	
	@Override
	public Long findVastaanottajienLukumaaraByViestiIdJaLahetysonnistui(Long viestiID, boolean lahetysonnistui) {
		EntityManager em = getEntityManager();
		
		String findVastaanottajienLukumaara = "SELECT COUNT(*) FROM RaportoitavaVastaanottaja a "	+ 
			"JOIN a.raportoitavaviesti WHERE a.raportoitavaviesti.id = :viestiID AND a.lahetysOnnistui = :lahetysonnistui";
		TypedQuery<Long> query = em.createQuery(findVastaanottajienLukumaara, Long.class);
		query.setParameter("viestiID", viestiID);
		
		if (lahetysonnistui) {
			query.setParameter("lahetysonnistui", "1");
		} else {
			query.setParameter("lahetysonnistui", "0");
		}
		
		return query.getSingleResult();
	}

	protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }
}
