package fi.vm.sade.viestintapalvelu;

// TODO isipila 23.5.2013: where should we have static utils?
public final class Utils {

    public static String resolveTemplateName(String template,
                                             String languageCode) {
        languageCode = languageCode == null || "".equals(languageCode) ? "FI"
                : languageCode;
        languageCode = "SE".equalsIgnoreCase(languageCode)
                || "FI".equalsIgnoreCase(languageCode) ? languageCode : "EN";
        return template.replace("{LANG}", languageCode.toUpperCase());
    }
}
