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
package fi.vm.sade.dto;

import java.io.Serializable;

public class PagingAndSortingDTO implements Serializable {
    private static final long serialVersionUID = -7108947232742937768L;
    private int fromIndex;
    private int numberOfRows;
    private String sortedBy;
    private String sortOrder;

    public PagingAndSortingDTO() {
    }

    public PagingAndSortingDTO(Builder builder) {
        fromIndex = builder.fromIndex;
        numberOfRows = builder.numberOfRows;
        sortedBy = builder.sortedBy;
        sortOrder = builder.sortOrder;
    }

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

    public static class Builder {
        private int fromIndex;
        private int numberOfRows;
        private String sortedBy;
        private String sortOrder;

        public Builder from(int index) {
            this.fromIndex = index;
            return this;
        }

        public Builder numberOfRows(int rows) {
            this.numberOfRows = rows;
            return this;
        }

        public Builder sortedBy(String value) {
            this.sortedBy = value;
            return this;
        }

        public Builder sortOrder(String value) {
            this.sortOrder = value;
            return this;
        }

        public PagingAndSortingDTO build() {
            return new PagingAndSortingDTO(this);
        }
    }

    public static PagingAndSortingDTO getDefault() {
        return new Builder().from(0).numberOfRows(0).build();
    }

    @Override
    public String toString() {
        return "PagingAndSortingDTO{" + "fromIndex=" + fromIndex + ", numberOfRows=" + numberOfRows + ", sortedBy='" + sortedBy + '\'' + ", sortOrder='"
                + sortOrder + '\'' + '}';
    }
}
