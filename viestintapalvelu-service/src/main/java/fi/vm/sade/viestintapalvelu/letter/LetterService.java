package fi.vm.sade.viestintapalvelu.letter;

import java.util.List;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.model.LetterBatch;

/**
 * Rajapinta kirjeiden liiketoimtakäsittelyä varten
 *  
 * @author vehei1
 *
 */
public interface LetterService {
    /**
     * Luo kirjelähetyksen
     * 
     * @param letterBatch Annetun kirjelähetyksen tiedot
     * @return Luodun kirjelähetyksen tiedot
     */
    public LetterBatch createLetter(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch);

    /**
     * Hakee kirjelähetyksen tiedot annetun avaimen perusteella
     * 
     * @param id Kirjelähetyksen avain
     * @return Kirjelähetyksen tiedot
     */
    public fi.vm.sade.viestintapalvelu.letter.LetterBatch findById(long id);

    /**
     * Hakee annettujen hakuparametrien mukaiset kirjelähetyksen tiedot
     * 
     * @param templateName Kirjepohjan nimi
     * @param languageCode Kielikoodi
     * @param organizationOid Organisaation OID
     * @param tag Tunniste
     * @param applicationPeriod
     * @return Kirjelähetyksen tiedot
     */
    public fi.vm.sade.viestintapalvelu.letter.LetterBatch findLetterBatchByNameOrgTag(String templateName,
                      String languageCode, String organizationOid,
                      Optional<String> tag, Optional<String> applicationPeriod);

    /**
     * Hakee annettujen hakuparametrien mukaiset korvauskentien tiedot
     * 
     * @param templateName Kirjepohjan nimi
     * @param languageCode Kielikoodi
     * @param organizationOid organisaation OID
     * @param tag Tunniste
     * @param applicationPeriod
     * @return Lista korvauskenttien tietoja
     */
    public List<fi.vm.sade.viestintapalvelu.template.Replacement> findReplacementByNameOrgTag(String templateName,
                  String languageCode, String organizationOid, Optional<String> tag, Optional<String> applicationPeriod);

    /**
     * Hakee vastaanottajan kirjeen sisällön
     * 
     * @param id Vastaanottajan kirjeen avain
     * @return Kirjeen sisällön tiedot
     */
    public fi.vm.sade.viestintapalvelu.letter.LetterContent getLetter(long id);
    
    /**
     * Hakee kirjelähetyksen kirjeiden sisällöt ja yhdistää ne yhdeksi PDF-dokumentiksi
     * 
     * @param letterBatchID Kirjelähetyksen avain
     * @return Kirjelähetyksen kirjeiden sisällöt
     * @throws Exception
     */
    public byte[] getLetterContentsByLetterBatchID(Long letterBatchID) throws Exception;
}