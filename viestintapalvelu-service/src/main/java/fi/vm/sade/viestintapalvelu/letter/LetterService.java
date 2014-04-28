package fi.vm.sade.viestintapalvelu.letter;

import java.util.List;

import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.template.Replacement;

public interface LetterService {

    /* ---------------------- */
    /* - Create LetterBatch - */
    /* ---------------------- */
    public LetterBatch createLetter(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch);

    /* ------------ */
    /* - findById - */
    /* ------------ */
    public fi.vm.sade.viestintapalvelu.letter.LetterBatch findById(long id);

    /* ------------------------------- */
    /* - findLetterBatchByNameOrgTag - */
    /* ------------------------------- */
    public fi.vm.sade.viestintapalvelu.letter.LetterBatch findLetterBatchByNameOrgTag(String templateName,
        String languageCode, String organizationOid, String tag);

    /* ------------------------------- */
    /* - findReplacementByNameOrgTag - */
    /* ------------------------------- */
    public List<fi.vm.sade.viestintapalvelu.template.Replacement> findReplacementByNameOrgTag(String templateName,
        String languageCode, String organizationOid, String tag);

    /* ------------- */
    /* - getLetter - */
    /* ------------- */
    public fi.vm.sade.viestintapalvelu.letter.LetterContent getLetter(long id);

}