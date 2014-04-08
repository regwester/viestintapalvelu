package fi.vm.sade.viestintapalvelu.letter;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverAddress;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.LetterReplacement;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.TemplateContent;
import fi.vm.sade.viestintapalvelu.template.Replacement;

/**
 * @author migar1
 *
 */
@Service
@Transactional
public class LetterService {

    @Autowired
    private LetterBatchDAO letterBatchDAO;

    
    /* ---------- */
    /* - Strore - */
    /* ---------- */
	public LetterBatch storeLetterDTO(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch) {
		
		// kirjeet.kirjelahetys
		LetterBatch letterB = new LetterBatch();
		letterB.setTemplateId(letterBatch.getTemplateId());
		letterB.setTimestamp(new Date());
		letterB.setLanguage(letterBatch.getLanguageCode());
		letterB.setStoringOid(letterBatch.getStoringOid());
		letterB.setOrganizationOid(letterBatch.getOrganizationOid());
		
		// kirjeet.lahetyskorvauskentat
		letterB.setLetterReplacements(parseLetterReplacementsModels(letterBatch, letterB));
		
		// kirjeet.vastaanottaja
        letterB.setLetterReceivers(parseLetterReceiversModels(letterBatch, letterB));
		
		return storeLetterBatch(letterB);
	}


	/*
	 *  kirjeet.lahetyskorvauskentat
	 */
	private Set<LetterReplacement> parseLetterReplacementsModels(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch, LetterBatch letterB) {
		Set<LetterReplacement> replacements = new HashSet<LetterReplacement>();

		Object replKeys[] = letterBatch.getTemplateReplacements().keySet().toArray();
		Object replVals[] = letterBatch.getTemplateReplacements().values().toArray();
		
		for (int i = 0; i < replVals.length; i++) {
			fi.vm.sade.viestintapalvelu.model.LetterReplacement repl = new fi.vm.sade.viestintapalvelu.model.LetterReplacement();
			
			repl.setName(replKeys[i].toString());
			repl.setDefaultValue(replVals[i].toString());
//			repl.setMandatory();
//TODO: tähän tietyt kentät Mandatory true esim. title body ...			
			
			
			
			repl.setTimestamp(new Date());
			repl.setLetterBatch(letterB);	
			replacements.add(repl);
		}
		return replacements;
	}

	/*
	 * kirjeet.vastaanottaja
	 */
	private Set<LetterReceivers> parseLetterReceiversModels(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch, LetterBatch letterB) {
		Set<LetterReceivers> receivers = new HashSet<LetterReceivers>();
        for (fi.vm.sade.viestintapalvelu.letter.Letter letter : letterBatch.getLetters()) {
			fi.vm.sade.viestintapalvelu.model.LetterReceivers rec = new fi.vm.sade.viestintapalvelu.model.LetterReceivers();
        	rec.setTimestamp(new Date());
        	rec.setLetterBatch(letterB);
        	
    		// kirjeet.vastaanottajakorvauskentat
        	if ((letter.getCustomLetterContents() != null) || (letter.getCustomLetterContents().isEmpty())) {				
	        	Set<LetterReceiverReplacement> letterRepl = new HashSet<LetterReceiverReplacement>();
	        	
	        	Object letReplKeys[] = letter.getCustomLetterContents().keySet().toArray();
	        	Object letReplVals[] = letter.getCustomLetterContents().values().toArray();
	        	
	    		for (int i = 0; i < letReplVals.length; i++) {
	    			fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement repl = new fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement();
	    			
	    			repl.setName(letReplKeys[i].toString());
	    			repl.setDefaultValue(letReplVals[i].toString());
	//    			repl.setMandatory();
	    			repl.setTimestamp(new Date());
	    			repl.setLetterReceivers(rec);
	    			letterRepl.add(repl);
	    		}
	    		rec.setLetterReceiverReplacement(letterRepl);
			}
        	
    		// kirjeet.vastaanottajaosoite
    		if (letter.getAddressLabel() != null) {				
	    		LetterReceiverAddress lra = new LetterReceiverAddress();
	    		lra.setFirstName(letter.getAddressLabel().getFirstName());
	    		lra.setLastName(letter.getAddressLabel().getLastName());
	    		lra.setAddressline(letter.getAddressLabel().getAddressline());
	    		lra.setAddressline2(letter.getAddressLabel().getAddressline2());
	    		lra.setAddressline3(letter.getAddressLabel().getAddressline3());
	    		lra.setPostalCode(letter.getAddressLabel().getPostalCode());
	    		lra.setCity(letter.getAddressLabel().getCity());
	    		lra.setRegion(letter.getAddressLabel().getRegion());
	    		lra.setCountry(letter.getAddressLabel().getCountry());
	    		lra.setCountryCode(letter.getAddressLabel().getCountryCode());
	    		lra.setLetterReceivers(rec);    		
	    		
	    		rec.setLetterReceiverAddress(lra);
			}
    		
    		//    		
        	receivers.add(rec);
        }
		return receivers;
	}

	private LetterBatch storeLetterBatch(LetterBatch letterB) {
		return letterBatchDAO.insert(letterB);
	}

	
    /* ------------ */
    /* - findById - */
    /* ------------ */
    public fi.vm.sade.viestintapalvelu.letter.LetterBatch findById(long id) {
    	LetterBatch searchResult = null;
    	List<LetterBatch> letterBatch = letterBatchDAO.findBy("id", id);
        if (letterBatch != null && !letterBatch.isEmpty()) {
            searchResult = letterBatch.get(0);
        }
        
		// kirjeet.kirjelahetys
        fi.vm.sade.viestintapalvelu.letter.LetterBatch result = new fi.vm.sade.viestintapalvelu.letter.LetterBatch();
        result.setTemplateId(searchResult.getTemplateId());
        result.setLanguageCode(searchResult.getLanguage());
        result.setStoringOid(searchResult.getStoringOid());
        result.setOrganizationOid(searchResult.getOrganizationOid());
        
		// kirjeet.lahetyskorvauskentat        
    	result.setTemplateReplacements(parseReplDTOs(searchResult.getLetterReplacements()));
    	
		// kirjeet.vastaanottaja
//    	result.setLetters(parseLetterDTOs(searchResult.getLetterReceivers()));    	
//		Not implemented
    	
    	return result;
    }

	private Map<String, Object> parseReplDTOs(Set<LetterReplacement> letterReplacements) {	 
		Map<String, Object> replacements = new HashMap<String, Object>();
		
        for (LetterReplacement letterRepl : letterReplacements) {
        	replacements.put(letterRepl.getName(), letterRepl.getDefaultValue());        	
        }
		return replacements;
	}
	
	private List<Letter> parseLetterDTOs(Set<LetterReceivers> letterReceivers) {
		List<Letter> letters = new LinkedList<Letter>();
				
        for (LetterReceivers letterRec : letterReceivers) {
        	Letter letter = new Letter();
        
        	// Should fetch by receiver...
        	
    		// kirjeet.vastaanottajakorvauskentat
    		Map<String, Object> replacements = new HashMap<String, Object>();        	
            for (LetterReceiverReplacement letterRepl : letterRec.getLetterReceiverReplacement()) {
            	replacements.put(letterRepl.getName(), letterRepl.getDefaultValue());        	
            }
        	letter.setTemplateReplacements(replacements);
        
    		// kirjeet.vastaanottajaosoite
        	// Not implemented
        	
        	letters.add(letter);
        }
		
		
		return letters;
	}

}
