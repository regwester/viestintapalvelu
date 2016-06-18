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
package fi.vm.sade.viestintapalvelu.dao;

import java.util.List;

import fi.vm.sade.dto.PagingAndSortingDTO;
import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;

/**
 * Rajapinta kirjelähetysten vastaanottajien tietokantakäsittelyä varten
 * 
 * @author vehei1
 *
 */
public interface LetterReceiversDAO extends JpaDAO<LetterReceivers, Long> {
    /**
     * Hakee kirjelähetysten vastaanottajat ja vastaanottajien kirjeet
     * 
     * @param letterBatchID Kirjelähetyksen tunnus
     * @param pagingAndSorting Sivutus- ja lajittelutiedot
     * @return Lista kirjelähetyksen vastaanottajatietoja
     */
    List<LetterReceivers> findLetterReceiversByLetterBatchID(Long letterBatchID, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee kirjelähetysten vastaanottajat ja vastaanottajien kirjeet
     * 
     * @param letterBatchID Kirjelähetyksen tunnus
     * @param pagingAndSorting Sivutus- ja lajittelutiedot
     * @param query Hakuehdot 
     * @return Lista kirjelähetyksen vastaanottajatietoja
     */
    List<LetterReceivers> findLetterReceiversByLetterBatchID(Long letterBatchID, PagingAndSortingDTO pagingAndSorting, String query);

    /**
     * Hakee kirjelähetysten vastaanottajien lukumäärän 
     *  
     * @param letterBatchID Kirjelähetyksen tunnus
     * @return Kirjelähetysten vastaanottajien lukumäärä
     */
    Long findNumberOfReciversByLetterBatchID(Long letterBatchID);

    /**
     * Hakee kirjelähetysten vastaanottajien lukumäärän 
     *  
     * @param letterBatchID Kirjelähetyksen tunnus
     * @return Kirjelähetysten vastaanottajien lukumäärä
     */
    Long findNumberOfReciversByLetterBatchID(Long letterBatchID, String query);

    
    List<Long> findLetterRecieverIdsByLetterBatchId(long letterBatchId);
}
