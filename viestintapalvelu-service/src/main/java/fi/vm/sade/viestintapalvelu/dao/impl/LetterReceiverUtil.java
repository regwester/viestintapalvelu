package fi.vm.sade.viestintapalvelu.dao.impl;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.PathBuilder;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverAddress;

public class LetterReceiverUtil {

    public static OrderSpecifier<?> orderBy(PagingAndSortingDTO pagingAndSorting) {
        PathBuilder<LetterReceiverAddress> pb = new PathBuilder<>(LetterReceiverAddress.class, "letterReceiverAddress");

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
