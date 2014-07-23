package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.PathBuilder;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverAddressDAO;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverAddress;
import fi.vm.sade.viestintapalvelu.model.QLetterReceiverAddress;

@Repository
public class LetterReceiverAddressDAOImpl extends AbstractJpaDAOImpl<LetterReceiverAddress, Long> implements 
    LetterReceiverAddressDAO {

    @Override
    public List<LetterReceiverAddress> getLetterReceiverAddressesByLetterReceiverID(List<Long> letterReceiverIDs,
        PagingAndSortingDTO pagingAndSorting) {
        QLetterReceiverAddress letterReceiverAddress = QLetterReceiverAddress.letterReceiverAddress;

        OrderSpecifier<?> orderBy = orderBy(pagingAndSorting);        
        BooleanExpression whereExpression = letterReceiverAddress.letterReceivers.id.in(letterReceiverIDs);
        JPAQuery findLetterReceiverAddress = from(letterReceiverAddress).where(whereExpression).orderBy(orderBy);
        
        return findLetterReceiverAddress.list(letterReceiverAddress);
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