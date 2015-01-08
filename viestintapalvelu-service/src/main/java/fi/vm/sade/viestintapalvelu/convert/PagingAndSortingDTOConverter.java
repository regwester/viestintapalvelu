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
