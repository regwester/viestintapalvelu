package fi.vm.sade.viestintapalvelu.letter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.LetterReplacement;

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
		
		// kirjeet.kirjelahetys
		LetterBatch letterB = new LetterBatch();
		letterB.setTemplateId(letterBatch.getTemplateId());
		letterB.setTimestamp(new Date());
		letterB.setLanguage(letterBatch.getLanguageCode());
		letterB.setStoringOid(letterBatch.getStoringOid());
		letterB.setOrganizationOid(letterBatch.getOrganizationOid());
		
		// kirjeet.lahetyskorvauskentat
		letterB.setLetterReplacements(parseLetterReplacements(letterBatch, letterB));
		
		// kirjeet.vastaanottaja
        letterB.setLetterReceivers(parseLetterReceivers(letterBatch, letterB));
		
		return storeLetterBatch(letterB);
	}


	/*
	 *  kirjeet.lahetyskorvauskentat
	 */
	private Set<LetterReplacement> parseLetterReplacements(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch, LetterBatch letterB) {
		Set<LetterReplacement> replacements = new HashSet<LetterReplacement>();

		Object replKeys[] = letterBatch.getTemplateReplacements().keySet().toArray();
		Object replVals[] = letterBatch.getTemplateReplacements().values().toArray();
		
		for (int i = 0; i < replVals.length; i++) {
			fi.vm.sade.viestintapalvelu.model.LetterReplacement repl = new fi.vm.sade.viestintapalvelu.model.LetterReplacement();
			String key = replKeys[i].toString();
			repl.setName(key);
			repl.setDefaultValue(replVals[i].toString());
//			repl.isMandatory(letterBatch.getTemplateReplacements().get(key));
			repl.setTimestamp(new Date());
			repl.setLetterBatch(letterB);	
			replacements.add(repl);
		}
		return replacements;
	}

	/*
	 * kirjeet.vastaanottaja
	 */
	private Set<LetterReceivers> parseLetterReceivers(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch, LetterBatch letterB) {
		Set<LetterReceivers> receivers = new HashSet<LetterReceivers>();
        for (fi.vm.sade.viestintapalvelu.letter.Letter letter : letterBatch.getLetters()) {
			fi.vm.sade.viestintapalvelu.model.LetterReceivers rec = new fi.vm.sade.viestintapalvelu.model.LetterReceivers();
        	rec.setTimestamp(new Date());
        	rec.setLetterBatch(letterB);
        	
    		// kirjeet.vastaanottajakorvauskentat
        	Set<LetterReceiverReplacement> letterRepl = new HashSet<LetterReceiverReplacement>();
        	
        	Object letReplKeys[] = letter.getCustomLetterContents().keySet().toArray();
        	Object letReplVals[] = letter.getCustomLetterContents().values().toArray();
        	
    		for (int i = 0; i < letReplVals.length; i++) {
    			fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement repl = new fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement();
    			String key = letReplKeys[i].toString();
    			repl.setName(key);
    			repl.setDefaultValue(letReplVals[i].toString());
//    			repl.isMandatory(letter.getCustomLetterContents().get(key));
    			repl.setTimestamp(new Date());
    			repl.setLetterReceivers(rec);
    			letterRepl.add(repl);
    		}
    		rec.setLetterReceiverReplacement(letterRepl);
        	
        	receivers.add(rec);
        }
		return receivers;
	}

	
	
	
	
	
	private LetterBatch storeLetterBatch(LetterBatch letterB) {
		return letterBatchDAO.insert(letterB);
	}
}
