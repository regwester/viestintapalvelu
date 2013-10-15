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
