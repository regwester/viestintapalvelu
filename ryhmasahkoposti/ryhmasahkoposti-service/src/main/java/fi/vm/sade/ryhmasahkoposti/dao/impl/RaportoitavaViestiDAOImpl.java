package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.RaportoitavaVastaanottajaQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.RaportoitavaViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavaViestiDAO;
import fi.vm.sade.ryhmasahkoposti.model.QRaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.QRaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;

@Repository
public class RaportoitavaViestiDAOImpl extends AbstractJpaDAOImpl<RaportoitavaViesti, Long> 
	implements RaportoitavaViestiDAO {
	
	@Override
	public List<RaportoitavaViesti> findBySearchCriteria(RaportoitavaViestiQueryDTO query) {
		RaportoitavaVastaanottajaQueryDTO vastaanottajaQuery = query.getVastaanottajaQuery();
		
		QRaportoitavaVastaanottaja raportoitavaVastaanottaja = QRaportoitavaVastaanottaja.raportoitavaVastaanottaja;
		QRaportoitavaViesti raportoitavaViesti = QRaportoitavaViesti.raportoitavaViesti;
		
		BooleanExpression whereExpression = null;
		
		if (vastaanottajaQuery.getVastaanottajanOid() != null) {
			whereExpression = raportoitavaVastaanottaja.vastaanottajaOid.eq(vastaanottajaQuery.getVastaanottajanOid());
		}
		
		if (vastaanottajaQuery.getVastaanottajanSahkopostiosoite() != null) {
			whereExpression = raportoitavaVastaanottaja.vastaanottajanSahkoposti.eq(
				vastaanottajaQuery.getVastaanottajanSahkopostiosoite());
		}
		
		if (vastaanottajaQuery.getVastaanottajanHenkilotunnus() != null) {
		}

		if (vastaanottajaQuery.getVastaanottajanNimi() != null) {
			whereExpression = raportoitavaVastaanottaja.hakuNimi.containsIgnoreCase(
				vastaanottajaQuery.getVastaanottajanNimi());
		}
		
		return from(raportoitavaViesti).leftJoin(
			raportoitavaViesti.raportoitavatVastaanottajat, raportoitavaVastaanottaja).fetch().where(
			whereExpression).list(raportoitavaViesti);
	}

	@Override
	public List<RaportoitavaViesti> findLahettajanRaportoitavatViestit(List<String> lahettajanOidList) {
		QRaportoitavaViesti raportoitavaViesti = QRaportoitavaViesti.raportoitavaViesti;
		
		BooleanExpression whereExpression = raportoitavaViesti.lahettajanOid.in(lahettajanOidList);
		
		List<RaportoitavaViesti> raportoitavatViestit = 
			from(raportoitavaViesti).where(whereExpression).list(raportoitavaViesti);
			
		return raportoitavatViestit;
	}

	@Override
	public Long findRaportoitavienViestienLukumaara() {
		EntityManager em = getEntityManager();
		
		String findRaportoitavienViestienLukumaara = "SELECT COUNT(*) FROM RaportoitavaViesti a";
		TypedQuery<Long> query = em.createQuery(findRaportoitavienViestienLukumaara, Long.class);
		
		return query.getSingleResult();
	}
	
	protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }
}
