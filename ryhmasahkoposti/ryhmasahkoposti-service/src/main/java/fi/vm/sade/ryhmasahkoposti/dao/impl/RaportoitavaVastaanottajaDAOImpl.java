package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.RyhmasahkopostiVastaanottajaQueryDTO;
import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavaVastaanottajaDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.QRaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.QRaportoitavaViesti;

@Repository
public class RaportoitavaVastaanottajaDAOImpl extends AbstractJpaDAOImpl<RaportoitavaVastaanottaja, Long> 
	implements RaportoitavaVastaanottajaDAO {

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
	public List<RaportoitavaVastaanottaja> findBySearchCriterias(RyhmasahkopostiVastaanottajaQueryDTO query) {
		QRaportoitavaVastaanottaja raportoitavaVastaanottaja = QRaportoitavaVastaanottaja.raportoitavaVastaanottaja;
		QRaportoitavaViesti raportoitavaViesti = QRaportoitavaViesti.raportoitavaViesti;
		
		BooleanExpression whereExpression = null;
		
		if (query.getVastaanottajanOid() != null) {
			whereExpression = raportoitavaVastaanottaja.vastaanottajaOid.eq(query.getVastaanottajanOid());
		}
		
		if (query.getVastaanottajanSahkopostiosoite() != null) {
			whereExpression = 
				raportoitavaVastaanottaja.vastaanottajanSahkoposti.eq(query.getVastaanottajanSahkopostiosoite());
		}
		
		if (query.getLahetysajankohta() != null) {
			whereExpression = 
				whereExpression.and(raportoitavaVastaanottaja.lahetysalkoi.loe(query.getLahetysajankohta()));
			whereExpression = 
				whereExpression.and(raportoitavaVastaanottaja.lahetyspaattyi.goe(query.getLahetysajankohta()));
		}
		
		return from(raportoitavaVastaanottaja).where(whereExpression).join(
			raportoitavaVastaanottaja.raportoitavaviesti, raportoitavaViesti).list(raportoitavaVastaanottaja);
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
