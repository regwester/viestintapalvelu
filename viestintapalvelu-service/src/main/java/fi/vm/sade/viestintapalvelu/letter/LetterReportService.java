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
package fi.vm.sade.viestintapalvelu.letter;

import java.util.List;

import fi.vm.sade.viestintapalvelu.dto.OrganizationDTO;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterBatchReportDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterBatchesReportDTO;
import fi.vm.sade.viestintapalvelu.dto.query.LetterReportQueryDTO;

/**
 * Palvelukerroksen rajapinta kirjelähetysten raportoinnin
 * liiketoimintalogiikkaa varten
 * 
 * @author vehei1
 *
 */
public interface LetterReportService {
    /**
     * Hakee halutun kirjelähetyksen raporttitiedot
     * 
     * @param id
     *            Kirjelähetyksen avain
     * @param pagingAndSorting
     *            Sivutus- ja lajittelutiedot
     * @return Kirjelähetyksen tiedot
     */
    LetterBatchReportDTO getLetterBatchReport(Long id, PagingAndSortingDTO pagingAndSorting, String query);

    /**
     * Hakee käyttäjän organisaation raportoitavat kirjelähetykset lajiteltuna
     * ja sivutettuna. Jos organizationOID on yhtä kuin rekisterinpitäjän OID,
     * haetaan kaikki kirjelähetykset organisaatiosta riippumatta.
     * 
     * @param organizationOID
     *            Organisaation OID
     * @param pagingAndSorting
     *            Sivutus ja lajittelutiedot
     * @return Näytettävät kirjelähetyksen raporttitiedot
     */
    LetterBatchesReportDTO getLetterBatchesReport(String organizationOID, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee hakuparametrien mukaiset raportoitavat kirjelähetykset lajiteltuna
     * ja sivutettuna
     * 
     * @param query
     *            Hakuparametrit
     * @param pagingAndSorting
     *            Sivutus ja lajittelutiedot
     * @return Näytettävät kirjelähetyksen raporttitiedot
     */
    LetterBatchesReportDTO getLetterBatchesReport(LetterReportQueryDTO query, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee käyttäjän organisaatiotiedot
     * 
     * @return Lista organisaation tietoja
     */
    List<OrganizationDTO> getUserOrganizations();
}
