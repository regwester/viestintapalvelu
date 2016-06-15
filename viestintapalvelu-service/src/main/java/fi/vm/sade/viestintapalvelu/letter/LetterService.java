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

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.dao.dto.LetterBatchCountDto;
import fi.vm.sade.viestintapalvelu.dao.dto.LetterBatchStatusDto;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterReceiverLetterDTO;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchDto;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterBatchSplitedIpostDto;
import fi.vm.sade.viestintapalvelu.letter.processing.IPostiProcessable;
import fi.vm.sade.viestintapalvelu.letter.LetterListResponse;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;

/**
 * Rajapinta kirjeiden liiketoimtakäsittelyä varten
 * 
 * @author vehei1
 *
 */
public interface LetterService {
    public static final String DOKUMENTTI_ID_PREFIX_PDF = "VIES-1-";
    public static final String DOKUMENTTI_ID_PREFIX_ZIP = "VIES-2-";

    public enum LetterBatchProcess {
        EMAIL, LETTER, IPOSTI
    }

    /**
     * Luo kirjelähetyksen
     *
     * @param letterBatch
     *            Annetun kirjelähetyksen tiedot
     * @return Luodun kirjelähetyksen tiedot
     */
    LetterBatch createLetter(AsyncLetterBatchDto letterBatch, boolean anonymous);

    /**
     * Luo kirjelähetyksen
     * 
     * @param letterBatch
     *            Annetun kirjelähetyksen tiedot
     * @return Luodun kirjelähetyksen tiedot
     */
    LetterBatch createLetter(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch, boolean anonymous);

    /**
     * Hakee kirjelähetyksen tiedot annetun avaimen perusteella
     * 
     * @param id
     *            Kirjelähetyksen avain
     * @return Kirjelähetyksen tiedot
     */
    fi.vm.sade.viestintapalvelu.letter.LetterBatch findById(long id);

    /**
     * Hakee annettujen hakuparametrien mukaiset kirjelähetyksen tiedot
     * 
     * @param templateName
     *            Kirjepohjan nimi
     * @param languageCode
     *            Kielikoodi
     * @param organizationOid
     *            Organisaation OID
     * @param tag
     *            Tunniste
     * @param applicationPeriod
     * @return Kirjelähetyksen tiedot
     */
    fi.vm.sade.viestintapalvelu.letter.LetterBatch findLetterBatchByNameOrgTag(String templateName, String languageCode, String organizationOid,
            Optional<String> tag, Optional<String> applicationPeriod);

    /**
     * Hakee annettujen hakuparametrien mukaiset korvauskentien tiedot
     * 
     * @param templateName
     *            Kirjepohjan nimi
     * @param languageCode
     *            Kielikoodi
     * @param organizationOid
     *            organisaation OID
     * @param tag
     *            Tunniste
     * @param applicationPeriod
     * @return Lista korvauskenttien tietoja
     */
    List<fi.vm.sade.viestintapalvelu.template.Replacement> findReplacementByNameOrgTag(String templateName, String languageCode, String organizationOid,
            Optional<String> tag, Optional<String> applicationPeriod);

    /**
     * Hakee vastaanottajan kirjeen sisällön
     * 
     * @param id
     *            Vastaanottajan kirjeen avain
     * @return Kirjeen sisällön tiedot
     */
    fi.vm.sade.viestintapalvelu.letter.LetterContent getLetter(long id);

    /**
     * Hakee kirjelähetyksen kirjeiden sisällöt ja yhdistää ne yhdeksi
     * PDF-dokumentiksi
     * 
     * @param letterBatchID
     *            Kirjelähetyksen avain
     * @return Kirjelähetyksen kirjeiden sisällöt
     * @throws Exception
     */
    byte[] getLetterContentsByLetterBatchID(Long letterBatchID) throws Exception;

    String getLetterTypeByLetterBatchID(Long letterBatchID) throws Exception;

    void updateBatchProcessingStarted(long id, LetterBatchProcess process);

    void processLetterReceiver(long receiverId) throws Exception;

    /**
     * @param id
     *            of the batch job to mark finished
     * @param process
     *            to mark finished
     * @return the next process
     */
    Optional<LetterBatchProcess> updateBatchProcessingFinished(long id, LetterBatchProcess process) throws Exception;

    LetterBatch fetchById(long id);

    LetterBatchStatusDto getBatchStatus(long batchId);

    void updateLetter(LetterReceiverLetter letter);

    List<Long> findUnprocessedLetterReceiverIdsByBatch(long batchId);

    List<Long> findAllReceiversIdsByBatch(long batchId);

    void saveBatchErrorForReceiver(Long letterReceiverId, String message);

    List<Long> findUnfinishedLetterBatches();

    LetterBatchSplitedIpostDto splitBatchForIpostProcessing(long letterBatchId);

    void processIposti(IPostiProcessable processable) throws Exception;

    void handleIpostError(IPostiProcessable letterBatchId, Exception e);

    /**
     * @param letterBatchId
     *            id of the LetterBatch to set to error-status
     * @param e
     *            the exception
     */
    void errorProcessingBatch(long letterBatchId, Exception e);

    LetterListResponse listLettersByUser(String persoinOid);

    /**
     * Hakee vastaanottajan kirjeen sisällön
     *
     * @param id
     *            Vastaanottajan kirjeen sisällön ID
     * @return Vastaanottajan kirjeen tiedot
     * @throws DataFormatException
     * @throws IOException
     */
    LetterReceiverLetterDTO getLetterReceiverLetter(Long id) throws IOException, DataFormatException;

    int publishLetterBatch(long letterBatchId);

    Optional<Long> getLatestLetterBatchId(String hakuOid, String type, String language, boolean published);

    LetterBatchCountDto countLetterStatuses(String hakuOid, String type, String language);

    List<String> getEPostiEmailAddresses(long letterBatchId);
}