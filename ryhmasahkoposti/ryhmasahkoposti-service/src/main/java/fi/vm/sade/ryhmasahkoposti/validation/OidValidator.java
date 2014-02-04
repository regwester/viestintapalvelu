package fi.vm.sade.ryhmasahkoposti.validation;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.constants.GroupEmailConstants;

@Component
public class OidValidator {

	public static boolean isOID(String hakuKentta) {
		String trimmedHakukentta = hakuKentta.trim();
		
		if (!trimmedHakukentta.startsWith(GroupEmailConstants.OID_OPH_TREE)) {
			return false;
		}
		
		return true;
	}
	
	public static boolean isHenkiloOID(String hakuKentta) {
		String trimmedHakukentta = hakuKentta.trim();
		
		if (!trimmedHakukentta.startsWith(GroupEmailConstants.OID_OPH_TREE)) {
			return false;
		}

		if (!trimmedHakukentta.startsWith(GroupEmailConstants.OID_OPH_PERSON_TREE)) {
			return false;
		}

		return true;
	}
	
	public static boolean isOrganisaatioOID(String hakuKentta) {
		String trimmedHakukentta = hakuKentta.trim();
		
		if (!isOID(trimmedHakukentta)) {
			return false;
		}
		
		if (!trimmedHakukentta.startsWith(GroupEmailConstants.OID_OPH_ORGANISATION_TREE)) {
			return false;
		}
		
		return true;
	}	
}
