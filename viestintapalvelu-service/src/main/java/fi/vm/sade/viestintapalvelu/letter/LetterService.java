package fi.vm.sade.viestintapalvelu.letter;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchStatusDto;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchDto;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;

/**
 * Rajapinta kirjeiden liiketoimtakäsittelyä varten
 * 
 * @author vehei1
 *
 */
public interface LetterService {

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
    Optional<LetterBatchProcess> updateBatchProcessingFinished(long id, LetterBatchProcess process);

    LetterBatch fetchById(long id);

    List<LetterReceiverLetter> getReceiverLetters(Set<LetterReceivers> letterReceivers);

    LetterBatchStatusDto getBatchStatus(long batchId);

    void updateLetter(LetterReceiverLetter letter);

    List<Long> findUnprocessedLetterReceiverIdsByBatch(long batchId);

    List<Long> findAllReceiversIdsByBatch(long batchId);

    void saveBatchErrorForReceiver(Long letterReceiverId, String message);

    List<Long> findUnfinishedLetterBatches();
}