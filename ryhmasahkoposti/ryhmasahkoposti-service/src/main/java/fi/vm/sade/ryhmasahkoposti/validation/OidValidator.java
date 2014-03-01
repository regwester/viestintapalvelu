package fi.vm.sade.ryhmasahkoposti.validation;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.constants.GroupEmailConstants;

@Component
public class OidValidator {

	public static boolean isOID(String searchArgument) {
		String trimmedSearchArgument = searchArgument.trim();
		
		if (!trimmedSearchArgument.startsWith(GroupEmailConstants.OID_OPH_TREE)) {
			return false;
		}
		
		return true;
	}
	
	public static boolean isHenkiloOID(String searchArgument) {
		String trimmedSearchArgument = searchArgument.trim();
		
		if (!trimmedSearchArgument.startsWith(GroupEmailConstants.OID_OPH_TREE)) {
			return false;
		}

		if (!trimmedSearchArgument.startsWith(GroupEmailConstants.OID_OPH_PERSON_TREE)) {
			return false;
		}

		return true;
	}
	
	public static boolean isOrganisaatioOID(String searchArgument) {
		String trimmedsearchargument = searchArgument.trim();
		
		if (!isOID(trimmedsearchargument)) {
			return false;
		}
		
		if (!trimmedsearchargument.startsWith(GroupEmailConstants.OID_OPH_ORGANISATION_TREE)) {
			return false;
		}
		
		return true;
	}	
}
