package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.QLetterReceiverLetter;

@Repository
public class LetterReceiverLetterDAOImpl extends AbstractJpaDAOImpl<LetterReceiverLetter, Long>
            implements LetterReceiverLetterDAO {

    @Override
    public List<LetterReceiverLetter> getLetterReceiverLettersByLetterReceiverID(List<Long> letterReceiverIDs) {
        QLetterReceiverLetter letterReceiverLetter = QLetterReceiverLetter.letterReceiverLetter;
        
        BooleanExpression whereExpression = letterReceiverLetter.letterReceivers.id.in(letterReceiverIDs);
        JPAQuery findLetterReceiverLetter = from(letterReceiverLetter).where(whereExpression);
        
        return findLetterReceiverLetter.list(letterReceiverLetter);
    }

    @Override
    public List<Long> findLetterReceiverLetterIdsByLetterReceiverIds(List<Long> letterReceiverIds) {
        return getEntityManager().createQuery(
                "select lrl.id from LetterReceivers lr" +
                "       inner join lr.letterReceiverLetter lrl " +
                "where lr.id in (:ids)", Long.class)
            .setParameter("ids", letterReceiverIds).getResultList();
    }

    @Override
    public List<LetterReceiverLetter> findByIds(List<Long> letterRreceiverLetterIds) {
        return getEntityManager().createQuery(
                "select lrl from LetterReceiverLetter lrl" +
                        "       inner join fetch lrl.letterReceivers lr" +
                        "       inner join fetch lr.letterBatch batch " +
                        "where lrl.id in (:ids)", LetterReceiverLetter.class)
                .setParameter("ids", letterRreceiverLetterIds).getResultList();
    }

    protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }
    
}
