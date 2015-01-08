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
package fi.vm.sade.viestintapalvelu.letter.html;

import javax.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;

import com.google.common.base.Function;

/**
 * User: ratamaa
 * Date: 28.10.2014
 * Time: 12:53
 */
public class TextWithoutHtmlCleaner implements Cleaner {
    public static final TextWithoutHtmlCleaner INSTANCE = new TextWithoutHtmlCleaner();
    public static final Function<? super String,? extends Object> FUNCTION = new Function<String, Object>() {
        @Nullable
        @Override
        public Object apply(@Nullable String input) {
            if (input == null) {
                return null;
            }
            return INSTANCE.clean(input);
        }
    };

    protected TextWithoutHtmlCleaner() {
    }

    @Override
    public String clean(String str) {// Parse str into a Document
        Document doc = Jsoup.parse(str);
        doc = new org.jsoup.safety.Cleaner(Whitelist.none()).clean(doc);
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        return doc.body().html();
    }
}
