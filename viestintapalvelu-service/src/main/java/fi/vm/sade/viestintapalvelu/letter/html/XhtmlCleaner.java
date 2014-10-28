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

package fi.vm.sade.viestintapalvelu.letter.html;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * User: ratamaa
 * Date: 28.10.2014
 * Time: 12:52
 */
public class XhtmlCleaner implements Cleaner {
    public static final XhtmlCleaner INSTANCE = new XhtmlCleaner();

    protected XhtmlCleaner() {
    }

    @Override
    public String clean(String str) {
        return Jsoup.clean(str, Whitelist.relaxed());
    }
}
