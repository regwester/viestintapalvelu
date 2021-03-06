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
package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;
import java.util.Optional;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;

/**
 * Rajapinta raportoitavien ryhmäsähköpostiviestien tietokantakäsittelyyn
 * 
 * @author vehei1
 *
 */
public interface ReportedMessageDAO extends JpaDAO<ReportedMessage, Long> {
    /**
     * Hakee halutun määrän käyttäjän organisaation raportoitavia viestejä halutussa järjestyksessä
     *  
     * @param organizationOids Organisaatioiden oid-tunnukset, jos null ei rajata
     * @param pagingAndSorting Palautettavien tietojen sivutus ja järjestystiedot
     * @return Lista raportoitavia viestejä
     */
	List<ReportedMessage> findByOrganizationOids(List<String> organizationOids, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee halutun käyttäjän lähettämiä raportoitavia viestejä halutussa järjestyksessä
     *
     * @param senderOid Lähettäjän oid-tunnus
     * @param pagingAndSorting Palautettavien tietojen sivutus ja järjestystiedot
     * @return Lista raportoitavia viestejä
     */
	List<ReportedMessage> findBySenderOid(String senderOid, PagingAndSortingDTO pagingAndSorting);

	Optional<ReportedMessage> findByLetter(Long letterID);

	/**
     * Hakee halutun käyttäjän lähettämiä raportoitavia viestejä halutussa järjestyksessä
     *  
     * @param senderOid Lähettäjän oid-tunnus
     * @param process Prosessi, jonka kautta viesti on lähetetty
     * @param pagingAndSorting Palautettavien tietojen sivutus ja järjestystiedot
     * @return Lista raportoitavia viestejä
     */
	List<ReportedMessage> findBySenderOidAndProcess(String senderOid, String process,
													PagingAndSortingDTO pagingAndSorting);
	
    /**
	 * Hakee hakuparametrien mukaiset raportoitavat viestit
	 *  
	 * @param query Hakuparametrit
	 * @param pagingAndSorting Lajittelutekijät
	 * @return Lista raportoituja ryhmäsähköpostiviesteja
	 */
	List<ReportedMessage> findBySearchCriteria(ReportedMessageQueryDTO query,
											   PagingAndSortingDTO pagingAndSorting);

	/**
	 * Hakee raportoitujen ryhmäsähköpostien lukumäärän
	 * 
	 * @param  organizationOids Organisaatioiden oid-tunnukset, jos null, ei rajata
	 * @return Raportoitujen ryhmäsähköpostien lukumäärä
	 */
	Long findNumberOfReportedMessages(List<String> organizationOids);

    /**
     * Hakee raportoitujen ryhmäsähköpostien lukumäärän tietyllä käyttäjällä
     *
     * @param  userOid Käyttäjän oid-tunnus
     * @return Kyseisen käyttäjän lähettämien ryhmäsähköpostien lukumäärä
     */
	Long findNumberOfUserMessages(String userOid);

	   /**
     * Hakee hakuparametrien mukaiset raportoitujen ryhmäsähköpostien lukumäärän
     * 
     * @param query Hakuparametrit
     * @return Raportoitujen ryhmäsähköpostien lukumäärä
     */
	   Long findNumberOfReportedMessage(ReportedMessageQueryDTO query);
}
