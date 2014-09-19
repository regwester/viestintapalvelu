package fi.vm.sade.ryhmasahkoposti.converter;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;

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
        PagingAndSortingDTO pagingAndSorting = PagingAndSortingDTO.getDefault();

        pagingAndSorting.setSortedBy(sortedBy);
        pagingAndSorting.setSortOrder(order);

        return pagingAndSorting;
    }
}
