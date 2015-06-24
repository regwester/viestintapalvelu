/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
        TypedQuery<IPosti> q = em.createQuery("SELECT new IPosti(p.id, p.version, p.createDate, p.letterBatch) from IPosti p left join p.letterBatch where p.sentDate is null", IPosti.class);
        return q.getResultList();
    }

    @Override
    public List<IPosti> findMailById(Long id) {
        EntityManager em = getEntityManager();
        TypedQuery<IPosti> query = em.createQuery("Select p from IPosti p where p.letterBatch.id = :value", IPosti.class);
        query.setParameter("value", id);
        return query.getResultList();
    }
    
    @Override
    public List<IPosti> findByLetterBatchId(Long id) {
        
        EntityManager em = getEntityManager();
        TypedQuery<IPosti> q = em.createQuery("SELECT new IPosti(p.id, p.version, p.createDate, p.letterBatch, p.sentDate) from IPosti p left join p.letterBatch where p.letterBatch.id = :value", IPosti.class);
        q.setParameter("value", id);
        return q.getResultList();
    }
    
    public int markAsSent(Long id, Long version) {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("UPDATE kirjeet.iposti set lahetetty = :lahetetty, version = :incVersion where id =:id and version = :version");
        query.setParameter("lahetetty", new Date());
        query.setParameter("incVersion", version + 1);
        query.setParameter("id", id);
        query.setParameter("version", version);
        return query.executeUpdate();
    }
}
