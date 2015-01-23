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
package fi.vm.sade.viestintapalvelu;

import com.google.common.collect.ImmutableMap;

import java.util.Locale;
import java.util.Map;

public class IPostCountryCodes {

    private static final Map<String, String> ISO3_TO_ISO2_MAP;
    public static final String DEFAULT_COUTRY_CODE = "XX";

    private IPostCountryCodes() {
    }

    static {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        for (String countryCode : Locale.getISOCountries()) {
            Locale locale = new Locale("", countryCode);
            builder.put(locale.getISO3Country(), countryCode);
        }
        ISO3_TO_ISO2_MAP = builder.build();
    }

    public static String iso3CountryCodeToIso2CountryCode(final String countryCode) {
        if (countryCode.length() == 2) {
            return countryCode;
        }
        String iso3 = ISO3_TO_ISO2_MAP.get(countryCode);
        return (iso3 != null ? iso3 : DEFAULT_COUTRY_CODE);
    }

    public static void main(String[] args) {
        System.out.println(IPostCountryCodes.iso3CountryCodeToIso2CountryCode("fff"));

    }
}
