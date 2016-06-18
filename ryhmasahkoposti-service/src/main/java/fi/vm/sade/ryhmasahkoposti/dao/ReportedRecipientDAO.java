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

import java.util.Date;
import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

/**
 * Rajapinta raportoitavien ryhmäsähköpostiviestin vastaanottajien
 * tietokantakäsittelyyn
 * 
 * @author vehei1
 *
 */
public interface ReportedRecipientDAO extends JpaDAO<ReportedRecipient, Long> {
    /**
     * Hakee vastaanottajien tiedot viestintunnuksella. Palauttaa halutun määrän
     * lajiteltuna.
     * 
     * @param messageID
     *            Viestintunnus
     * @param pagingAndSorting
     *            Sivutus- ja lajittelutiedot
     * @return Lista raportoitavien vastaanottajien tietoja
     */
    List<ReportedRecipient> findByMessageId(Long messageID, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee vastaanottajien tiedot viestintunnuksella ja tila lähetys
     * epäonnistui. Palauttaa halutun määrän lajiteltuna.
     * 
     * @param messageID
     *            Viestintunnus
     * @param pagingAndSorting
     *            Sivutus- ja lajittelutiedot
     * @return Lista raportoitavien vastaanottajien tietoja, joille lähetys on
     *         epäonnistunut
     */
    List<ReportedRecipient> findByMessageIdAndSendingUnsuccessful(Long messageID, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee vastaanottajien tiedot viestintunnuksella ja tila lähetys
     * palautui. Palauttaa halutun määrän lajiteltuna.
     *
     * @param messageID
     *            Viestintunnus
     * @param pagingAndSorting
     *            Sivutus- ja lajittelutiedot
     * @return Lista raportoitavien vastaanottajien tietoja, joille lähetys on
     *         epäonnistunut
     */
    List<ReportedRecipient> findByMessageIdAndSendingBounced(Long messageID, PagingAndSortingDTO pagingAndSorting);

    /**
     * Hakee vastaanottajan tiedot vastaanottajantunnisteella
     * 
     * @param recipientID
     *            Vastaanottajantunnus
     * @return Raportoitavan vastaanottajan tiedot
     */
    ReportedRecipient findByRecipientID(Long recipientID);

    /**
     * Hakee vastaanottajan tiedot viestin tiivisteen perusteella
     *
     * @param letterHash
     *            Viestin tiiviste
     * @return Raportoitavan vastaanottajan tiedot
     */
    List<ReportedRecipient> findByLetterHash(String letterHash);

    /**
     * Hakee lähettävästä ryhmäsähköpostista viimeiseksi lähetetyn
     * 
     * @param messageID
     *            Sanoman tunnus
     * @return Viimeiseksi lähetetyn ajanhetki
     */
    Date findMaxValueOfSendingEndedByMessageID(Long messageID);

    /**
     * Hakee vastaanottajien lukumäärän viestintunnuksella
     * 
     * @param messageID
     *            Viestintunnus
     * @return Viestin vastaanottajien lukumäärän
     */
    Long findNumberOfRecipientsByMessageID(Long messageID);

    /**
     * Hakee vastaanottajien lukumäärän viestintunnuksella ja viestin tilalla
     *
     * @param messageID
     *            Viestintunnus
     * @param sendingStatus
     *            0,1,2 halutun statuksen mukaan
     * @return Viestin vastaanottajien lukumäärän
     */
    Long findNumberOfRecipientsByMessageIDAndSendingStatus(Long messageID, String sendingStatus);

    /**
     * Hakee raportoitavat vastaanottajat, joille viestiä ei ole lähetetty
     * 
     * @return Lista raportoitavan vastaanottajien tietoja
     */
    List<ReportedRecipient> findUnhandled();

    /**
     * Hakee raportoitavien vastaanottajien id:t, joilta ei ole vielä haettu
     * henkilö- tai organisaatiotietoja.
     *
     * @return Listan vastaanottajien id:istä
     */
    List<Long> findRecipientIdsWithIncompleteInformation();

}
