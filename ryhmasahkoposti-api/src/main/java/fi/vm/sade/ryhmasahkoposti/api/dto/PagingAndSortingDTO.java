package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;

public class PagingAndSortingDTO implements Serializable {
    private static final long serialVersionUID = -7108947232742937768L;
    private int fromIndex;
    private int numberOfRows;
    private String sortedBy;
    private String sortOrder;

    public int getFromIndex() {
        return fromIndex;
    }
    
    public void setFromIndex(int index) {
        this.fromIndex = index;
    }
    
    public int getNumberOfRows() {
        return numberOfRows;
    }
    
    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public String getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(String sortedBy) {
        this.sortedBy = sortedBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
