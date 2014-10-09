package fi.vm.sade.viestintapalvelu.letter;

import java.util.List;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.dao.dto.LetterBatchStatusDto;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchDto;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterBatchSplitedIpostDto;
import fi.vm.sade.viestintapalvelu.letter.processing.IPostiProcessable;
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
        EMAIL,
        LETTER,
        IPOSTI
    }

    /**
     * Luo kirjelähetyksen
     *
     * @param letterBatch
     *            Annetun kirjelähetyksen tiedot
     * @return Luodun kirjelähetyksen tiedot
     */
    LetterBatch createLetter(AsyncLetterBatchDto letterBatch);

    /**
     * Luo kirjelähetyksen
     * 
     * @param letterBatch
     *            Annetun kirjelähetyksen tiedot
     * @return Luodun kirjelähetyksen tiedot
     */
    LetterBatch createLetter(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch);

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

    void updateBatchProcessingStarted(long id, LetterBatchProcess process);

    void processLetterReceiver(long receiverId) throws Exception;

    /**
     * @param id of the batch job to mark finished
     * @param process to mark finished
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
     * @param letterBatchId id of the LetterBatch to set to error-status
     * @param e the exception
     */
    void errorProcessingBatch(long letterBatchId, Exception e);
}