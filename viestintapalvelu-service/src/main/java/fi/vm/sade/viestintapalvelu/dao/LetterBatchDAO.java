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

import com.google.common.base.Optional;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.dao.dto.LetterBatchStatusDto;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterBatchReportDTO;
import fi.vm.sade.viestintapalvelu.dto.query.LetterReportQueryDTO;
import fi.vm.sade.viestintapalvelu.letter.LetterListItem;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;

;

/**
 * Rajapinta kirjelähetysten tietokantakäsittelyä varten
 * 
 * @author vehei1
 *
 */
public interface LetterBatchDAO extends JpaDAO<LetterBatch, Long> {
    /**
     * Haetaan kirjelähetyksen tiedot hakuparametreilla
     * 
     * @param templateName
     *            Kirjepohjan nimi
     * @param language
     *            Kielikoodi
     * @param organizationOid
     *            Organisaation OID
     * @param tag
     *            Tunniste
     * @param applicationPeriod
     * @return Kirjelähetyksen tiedot
     */
    LetterBatch findLetterBatchByNameOrgTag(String templateName, String language, String organizationOid, Optional<String> tag,
            Optional<String> applicationPeriod);

    /**
     * Haetaan kirjelähetyksen tiedot hakuparametreilla
     * 
     * @param templateName
     *            Kirjepohjan nimi
     * @param language
     *            Kielikoodi
     * @param organizationOid
     *            Organisaation OID
     * @return Kirjelähetyksen tiedot
     */
    LetterBatch findLetterBatchByNameOrg(String templateName, String language, String organizationOid);

    /**
     * Hakee listan kirjelähetysten tietoja sivutettuna ja lajiteltuna
     * 
     * @param organizationOIDs
     *            organisaatio OIDs
     * @param pagingAndSorting
     *            Sivutus- ja lajittelutiedot
     * @return Lista kirjelähetysten tietoja
     */
    List<LetterBatch> findLetterBatchesByOrganizationOid(List<String> organizationOIDs, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee listan kirjelähetyksiä annetujen hakuparametrien mukaisesti
     * 
     * @param letterReportQuery
     *            Kirjelähetysten raportoinnin hakuparametrit
     * @param pagingAndSorting
     *            Sivutus- ja lajittelutiedot
     * @return Lista kirjelähetysten tietoja
     */
    List<LetterBatchReportDTO> findLetterBatchesBySearchArgument(LetterReportQueryDTO letterReportQuery, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee kirjelähetysten lukumäärän
     *
     * @return Kirjelähetysten lukumäärä
     */
    Long findNumberOfLetterBatches();

    /**
     * Hakee kirjelähetysten lukumäärän
     * 
     * @param organizationOids
     *            Organisaatioiden oid-tunnukset
     * @return Kirjelähetysten lukumäärä
     */
    Long findNumberOfLetterBatches(List<String> organizationOids);

    /**
     * Hakee kirjelähetysten lukumäärän annettujen hakuparametrien mukaisesti
     * 
     * @param letterReportQuery
     *            Kirjelähetysten raportoinnin hakuparametrit
     * @param maxCount
     * @return Hakuparametreja vastaavien kirjelähetysten lukumäärä
     */
    Long findNumberOfLetterBatchesBySearchArgument(LetterReportQueryDTO letterReportQuery, Long maxCount);

    /**
     * @param letterBatchId
     *            id of a LetterBatch
     * @return the status of given LetterBatch
     */
    LetterBatchStatusDto getLetterBatchStatus(long letterBatchId);

    /**
     * 
     * @param batchId
     *            id of LetterBatch
     * @return list of ids for LetterReceivers that have yet to be processed
     */
    List<Long> findUnprocessedLetterReceiverIdsByBatch(long batchId);

    /**
     * 
     * @param batchId
     *            id of LetterBatch
     * @return list of ids for all LetterReceivers
     */
    List<Long> findAllLetterReceiverIdsByBatch(long batchId);

    /**
     * Fetches id's of unfinished LetterBatches that haven't been terminated
     * because of error
     * 
     * @return list of id's of unfinished LetterBatches
     */
    List<Long> findUnfinishedLetterBatches();

    /**
     * Hakee listan kirjelähetysten tietoja sivutettuna ja lajiteltuna
     *
     * @param pagingAndSorting
     *            Sivutus- ja lajittelutiedot
     * @return Lista kirjelähetysten tietoja
     */
    List<LetterBatch> findAll(PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee kirjelähetyksen kirjepohjan nimen kirjelähetystunnuksen mukaan
     * 
     * @param batchId
     * @return
     */

    String findTemplateNameForLetterBatch(long batchId);

    List<LetterListItem> findLettersReadyForPublishByPersonOid(String personOid);

    int publishLetterBatch(long batchId);
}
