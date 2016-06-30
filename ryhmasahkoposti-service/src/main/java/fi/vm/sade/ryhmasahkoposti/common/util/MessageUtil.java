/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.ryhmasahkoposti.common.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
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
        try {
            return messageSource.getMessage(key, new Object[] {}, getLocale());
        } catch (NoSuchMessageException e) {
            return messageSource.getMessage(key, new Object[] {}, DEFAULT_LOCALE);
        }
    }

    private static Locale getLocale() {
        Locale locale = Locale.getDefault();
        if (locale == null) {
            locale = DEFAULT_LOCALE;
        }
        return locale;
    }

    public static String getMessage(String key, Object... args) {
        try {
            return messageSource.getMessage(key, args, getLocale());
        } catch (NoSuchMessageException e) {
            return messageSource.getMessage(key, args, DEFAULT_LOCALE);
        }
    }
}
