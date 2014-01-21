package fi.vm.sade.ryhmasahkoposti.validation;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.constants.RyhmasahkopostiConstants;

@Component
public class OidValidator {

	public static boolean isOID(String hakuKentta) {
		String trimmedHakukentta = hakuKentta.trim();
		
		if (!trimmedHakukentta.startsWith(RyhmasahkopostiConstants.OID_OPH_PUU)) {
			return false;
		}
		
		return true;
	}
	
	public static boolean isHenkiloOID(String hakuKentta) {
		String trimmedHakukentta = hakuKentta.trim();
		
		if (!trimmedHakukentta.startsWith(RyhmasahkopostiConstants.OID_OPH_PUU)) {
			return false;
		}

		if (!trimmedHakukentta.startsWith(RyhmasahkopostiConstants.OID_OPH_HENKILO_PUU)) {
			return false;
		}

		return true;
	}
	
	public static boolean isOrganisaatioOID(String hakuKentta) {
		String trimmedHakukentta = hakuKentta.trim();
		
		if (!isOID(trimmedHakukentta)) {
			return false;
		}
		
		if (!trimmedHakukentta.startsWith(RyhmasahkopostiConstants.OID_OPH_ORGANISAATIO_PUU)) {
			return false;
		}
		
		return true;
	}	
}
