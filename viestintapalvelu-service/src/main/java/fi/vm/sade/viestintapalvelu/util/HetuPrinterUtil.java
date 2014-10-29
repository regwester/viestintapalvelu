/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.viestintapalvelu.util;

import java.util.regex.Pattern;

/**
 * User: ratamaa
 * Date: 27.10.2014
 * Time: 12:08
 */
public class HetuPrinterUtil {
    private HetuPrinterUtil() {
    }

    private static final Pattern HETU_PATTERN = Pattern.compile("(\\d{6})([+-AB])(\\d)(\\d{2}[0-9A-Za-z])");

    /**
     * @param textContainingHetu
     * @return the text with HETUs masked so that last three number/letters are replaced by ###
     */
    public static String clearHetu(String textContainingHetu) {
        return HETU_PATTERN.matcher(textContainingHetu).replaceAll("$1$2$3###");
    }
}
