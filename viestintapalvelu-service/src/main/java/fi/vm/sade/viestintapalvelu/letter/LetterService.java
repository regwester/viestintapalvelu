package fi.vm.sade.viestintapalvelu.letter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverAddress;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
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
    private CurrentUserComponent currentUserComponent;

    @Autowired
    private LetterBatchDAO letterBatchDAO;

    @Autowired
    private LetterReceiverLetterDAO letterReceiverLetterDAO;

    /* ---------------------- */
    /* - Create LetterBatch - */
    /* ---------------------- */
	public LetterBatch createLetter(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch) {
	    Henkilo henkilo = currentUserComponent.getCurrentUser();
	    
		// kirjeet.kirjelahetys
		LetterBatch letterB = new LetterBatch();
		letterB.setTemplateId(letterBatch.getTemplateId());
		letterB.setTemplateName(letterBatch.getTemplateName());
		letterB.setFetchTarget(letterBatch.getFetchTarget());
		letterB.setTimestamp(new Date());
		letterB.setLanguage(letterBatch.getLanguageCode());
		letterB.setStoringOid(henkilo.getOidHenkilo());
		letterB.setOrganizationOid(letterBatch.getOrganizationOid());
		
		// kirjeet.lahetyskorvauskentat
		letterB.setLetterReplacements(parseLetterReplacementsModels(letterBatch, letterB));

		// kirjeet.vastaanottaja
        letterB.setLetterReceivers(parseLetterReceiversModels(letterBatch, letterB));
		
		return storeLetterBatch(letterB);
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
        result.setTemplateName(searchResult.getTemplateName());
        result.setFetchTarget(searchResult.getFetchTarget());                
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

	/* ------------------------------- */
    /* - findLetterBatchByNameOrgTag - */
    /* ------------------------------- */
	public fi.vm.sade.viestintapalvelu.letter.LetterBatch findLetterBatchByNameOrgTag(String templateName, String organizationOid, String tag) {		
        fi.vm.sade.viestintapalvelu.letter.LetterBatch result = new fi.vm.sade.viestintapalvelu.letter.LetterBatch();
		
		LetterBatch letterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(templateName, organizationOid, tag);
        if (letterBatch != null) {
        
			// kirjeet.kirjelahetys
	        result.setTemplateId(letterBatch.getTemplateId());       
	        result.setTemplateName(letterBatch.getTemplateName());
	        result.setFetchTarget(letterBatch.getFetchTarget());                
	        result.setLanguageCode(letterBatch.getLanguage());
	        result.setStoringOid(letterBatch.getStoringOid());
	        result.setOrganizationOid(letterBatch.getOrganizationOid());
	        
			// kirjeet.lahetyskorvauskentat        
	    	result.setTemplateReplacements(parseReplDTOs(letterBatch.getLetterReplacements()));	    	
        }
        
    	return result;
    }

	/* ------------------------------- */
    /* - findReplacementByNameOrgTag - */
    /* ------------------------------- */
	public List<fi.vm.sade.viestintapalvelu.template.Replacement> findReplacementByNameOrgTag(String templateName, String organizationOid, String tag) {		

    	List<fi.vm.sade.viestintapalvelu.template.Replacement> replacements = new LinkedList<fi.vm.sade.viestintapalvelu.template.Replacement>();		
		
		LetterBatch letterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(templateName, organizationOid, tag);
        if (letterBatch != null) {
        
			// kirjeet.lahetyskorvauskentat        
	        for (LetterReplacement letterRepl : letterBatch.getLetterReplacements()) {
	        	fi.vm.sade.viestintapalvelu.template.Replacement repl = new fi.vm.sade.viestintapalvelu.template.Replacement();
	        	repl.setId(letterRepl.getId());
	        	repl.setName(letterRepl.getName());
	        	repl.setDefaultValue(letterRepl.getDefaultValue());
	        	repl.setMandatory(letterRepl.isMandatory());
	        	repl.setTimestamp(letterRepl.getTimestamp());
	        	
	        	replacements.add(repl);        	
	        }
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
            	
            	// not implemented totally.
            	
            }
        	letter.setTemplateReplacements(replacements);
        
    		// kirjeet.vastaanottajaosoite
        	fi.vm.sade.viestintapalvelu.address.AddressLabel addr = new fi.vm.sade.viestintapalvelu.address.AddressLabel(
        			letterRec.getLetterReceiverAddress().getFirstName(),
        			letterRec.getLetterReceiverAddress().getLastName(),
        			letterRec.getLetterReceiverAddress().getAddressline(),
        			letterRec.getLetterReceiverAddress().getAddressline2(),
        			letterRec.getLetterReceiverAddress().getAddressline3(),
        			letterRec.getLetterReceiverAddress().getPostalCode(),
        			letterRec.getLetterReceiverAddress().getCity(),
        			letterRec.getLetterReceiverAddress().getRegion(),
        			letterRec.getLetterReceiverAddress().getCountry(),
        			letterRec.getLetterReceiverAddress().getCountryCode());
        	letter.setAddressLabel(addr);
        	
        	
        	letters.add(letter);
        }
		return letters;
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
	
	
    private Map<String, Object> parseReplDTOs(Set<LetterReplacement> letterReplacements) {	 
		Map<String, Object> replacements = new HashMap<String, Object>();
		
        for (LetterReplacement letterRepl : letterReplacements) {
        	replacements.put(letterRepl.getName(), letterRepl.getDefaultValue());        	
        }
		return replacements;
	}
    

    private LetterBatch storeLetterBatch(LetterBatch letterB) {
		return letterBatchDAO.insert(letterB);
    }
    
	
    /* ------------- */
    /* - getLetter - */
    /* ------------- */
	public fi.vm.sade.viestintapalvelu.letter.LetterContent getLetter(long id) {

		
    	List<LetterReceiverLetter> letterReceiverLetter = letterReceiverLetterDAO.findBy("id", id);		
				
		fi.vm.sade.viestintapalvelu.letter.LetterContent content = new fi.vm.sade.viestintapalvelu.letter.LetterContent();
		
		if (letterReceiverLetter!= null && !letterReceiverLetter.isEmpty()) {
			LetterReceiverLetter lb = letterReceiverLetter.get(0);
			
			content.setContentType(lb.getOriginalContentType());				
			content.setTimestamp(lb.getTimestamp());

			if ("application/zip".equals(lb.getContentType())) {
				try {
					content.setContent(unZip(lb.getLetter()));
					
				} catch (IOException e) {
					content.setContent(lb.getLetter());
					content.setContentType(lb.getContentType());				
				} catch (DataFormatException e) {
					content.setContent(lb.getLetter());
					content.setContentType(lb.getContentType());				
				}
			} else {
				content.setContent(lb.getLetter());
			}		
			
		}
		
		return content;
	}
    

	private static byte[] unZip(byte[] content) throws IOException, DataFormatException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(content.length);
		
		Inflater inflater = new Inflater();   
		inflater.setInput(content);  
		   
		byte[] buffer = new byte[1024];  
		   
		while (!inflater.finished()) {  
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		
		outputStream.close();  
		return outputStream.toByteArray();  	
	}

}
