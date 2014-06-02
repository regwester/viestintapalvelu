package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.IPostiDAO;
import fi.vm.sade.viestintapalvelu.model.IPosti;

@Repository
public class IPostiDAOImpl extends AbstractJpaDAOImpl<IPosti, Long> implements IPostiDAO {

    @Override
    public List<IPosti> findUnSent() {
        EntityManager em = getEntityManager();
        TypedQuery<IPosti> q = em.createQuery("SELECT p from IPosti p where p.sentDate is null", IPosti.class);
        return q.getResultList();
    }
}
