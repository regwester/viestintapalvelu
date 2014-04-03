package fi.vm.sade.viestintapalvelu.letter;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;;

/**
 * @author migar1
 *
 */
@Service
@Transactional
public class LetterService {

    @Autowired
    private LetterBatchDAO letterBatchDAO;

	public LetterBatch storeLetterDTO(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch) {
		LetterBatch letterB = new LetterBatch();
		letterB.setTemplateId(letterBatch.getTemplateId());
		letterB.setTimestamp(new Date());
		letterB.setLanguage(letterBatch.getLanguageCode());
		letterB.setStoringOid(letterBatch.getStoringOid());
		letterB.setOrganizationOid(letterBatch.getOrganizationOid());
		return storeLetterBatch(letterB);
	}


	
	
	
	
	
	private LetterBatch storeLetterBatch(LetterBatch letterB) {
		return letterBatchDAO.insert(letterB);
	}
}
