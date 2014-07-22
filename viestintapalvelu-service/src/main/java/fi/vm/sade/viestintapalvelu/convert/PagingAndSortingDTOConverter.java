package fi.vm.sade.viestintapalvelu.convert;

import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;

@Component
public class PagingAndSortingDTOConverter {

    public PagingAndSortingDTO convert(Integer nbrOfRows, Integer page, String sortedBy, String order) {
        PagingAndSortingDTO pagingAndSorting = new PagingAndSortingDTO();
        
        if (nbrOfRows != null) {
            pagingAndSorting.setFromIndex(nbrOfRows * (--page));
            pagingAndSorting.setNumberOfRows(nbrOfRows);
        }
        
        pagingAndSorting.setSortedBy(sortedBy);
        pagingAndSorting.setSortOrder(order);
        
        return pagingAndSorting;
    }
    
    public PagingAndSortingDTO convert(String sortedBy, String order) {
        PagingAndSortingDTO pagingAndSorting = new PagingAndSortingDTO();
        
        pagingAndSorting.setFromIndex(0);
        pagingAndSorting.setNumberOfRows(0);        
        pagingAndSorting.setSortedBy(sortedBy);
        pagingAndSorting.setSortOrder(order);
        
        return pagingAndSorting;
    }
}
