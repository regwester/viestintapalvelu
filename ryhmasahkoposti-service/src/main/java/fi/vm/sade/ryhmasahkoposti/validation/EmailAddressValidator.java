package fi.vm.sade.ryhmasahkoposti.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import fi.vm.sade.generic.common.validation.ValidationConstants;

@Component
public class EmailAddressValidator {
	private static Pattern pattern = Pattern.compile(ValidationConstants.EMAIL_PATTERN);
	
	public static boolean validate(String s) {
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}
}
