package fi.vm.sade.ryhmasahkoposti.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {
	private static MessageSource messageSource;
	private static Locale DEFAULT_LOCALE = new Locale("fi");

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		MessageUtil.messageSource = messageSource;
	}
	
	public static String getMessage(String key) {
		Locale locale = Locale.getDefault();
		
		if (locale == null) {
			locale = DEFAULT_LOCALE;
		}
		
		return messageSource.getMessage(key, new Object[] {}, locale);
	}

	public static String getMessage(String key, Object... args ) {
		Locale locale = Locale.getDefault();
		
		if (locale == null) {
			locale = DEFAULT_LOCALE;
		}
		
		return messageSource.getMessage(key, args, locale);
	}
}
