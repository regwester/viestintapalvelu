package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.PathBuilder;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiversDAO;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverAddress;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.QLetterReceiverAddress;
import fi.vm.sade.viestintapalvelu.model.QLetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.QLetterReceivers;

@Repository
public class LetterReceiversDAOImpl extends AbstractJpaDAOImpl<LetterReceivers, Long> implements LetterReceiversDAO {

    @Override
    public List<LetterReceivers> findLetterReceiversByLetterBatchID(Long letterBatchID, 
        PagingAndSortingDTO pagingAndSorting) {
        QLetterReceivers letterReceivers = QLetterReceivers.letterReceivers;
        QLetterReceiverAddress letterReceiverAddress = QLetterReceiverAddress.letterReceiverAddress;
        QLetterReceiverLetter letterReceiverLetter = QLetterReceiverLetter.letterReceiverLetter;
        
        BooleanExpression whereExpression = letterReceivers.letterBatch.id.eq(letterBatchID);
        OrderSpecifier<?> orderBy = orderBy(pagingAndSorting);
        
        JPAQuery findLetterReceivers = from(letterReceivers).
            leftJoin(letterReceivers.letterReceiverAddress, letterReceiverAddress).
            leftJoin(letterReceivers.letterReceiverLetter, letterReceiverLetter).
            where(whereExpression).orderBy(orderBy).
            limit(pagingAndSorting.getNumberOfRows()).offset(pagingAndSorting.getFromIndex());

        return findLetterReceivers.list(letterReceivers);
    }
    
    @Override
    public Long findNumberOfReciversByLetterBatchID(Long letterBatchID) {
        EntityManager em = getEntityManager();

        String findNumberOfRecivers = "SELECT COUNT(*) FROM LetterReceivers a "
            + "JOIN a.letterBatch WHERE a.letterBatch.id = :letterBatchID";
        TypedQuery<Long> query = em.createQuery(findNumberOfRecivers, Long.class);
        query.setParameter("letterBatchID", letterBatchID);

        return query.getSingleResult();    
    }

    @Override
    public List<Long> findLetterRecieverIdsByLetterBatchId(long letterBatchId) {
        return getEntityManager().createQuery(
                "select receiver.id from LetterReceivers receiver " +
                "       inner join receiver.letterBatch lb with lb.id = :letterBatchId " +
                "order by receiver.id", Long.class)
            .setParameter("letterBatchId", letterBatchId)
            .getResultList();
    }

    protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }
    
    protected OrderSpecifier<?> orderBy(PagingAndSortingDTO pagingAndSorting) {
        PathBuilder<LetterReceiverAddress> pb = 
            new PathBuilder<LetterReceiverAddress>(LetterReceiverAddress.class, "letterReceiverAddress");
        
        if (pagingAndSorting.getSortedBy() != null && !pagingAndSorting.getSortedBy().isEmpty()) {
            if (pagingAndSorting.getSortOrder() == null || pagingAndSorting.getSortOrder().isEmpty()) {
                return pb.getString(pagingAndSorting.getSortedBy()).asc();
            }
            
            if (pagingAndSorting.getSortOrder().equalsIgnoreCase("asc")) {
                return pb.getString(pagingAndSorting.getSortedBy()).asc();
            }
            
            return pb.getString(pagingAndSorting.getSortedBy()).desc();
        }
        
        return pb.getString("lastName").asc();
    }    
}
