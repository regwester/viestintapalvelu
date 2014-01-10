package fi.vm.sade.viestintapalvelu;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// TODO isipila 23.5.2013: where should we have static utils?
public final class Utils {

    private static final FastDateFormat THREAD_SAFE_DATE_FORMATTER = FastDateFormat.getInstance("dd.MM.yyyy_HH.mm");

    public static String resolveTemplateName(String template, String languageCode) {
        languageCode = languageCode == null || "".equals(languageCode) ? "FI" : languageCode;
        languageCode = "SE".equalsIgnoreCase(languageCode) || "FI".equalsIgnoreCase(languageCode) ? languageCode : "EN";
        return template.replace("{LANG}", languageCode.toUpperCase());
    }

    public static String globalRandomId() {
        return UUID.randomUUID().toString();
    }

    public static String filenamePrefixWithUsernameAndTimestamp(String filename) {
        return new StringBuilder().append(getAuthenticatedUserName()).append(".")
                .append(THREAD_SAFE_DATE_FORMATTER.format(new Date())).append(".").append(filename).toString();
    }

    private static String getAuthenticatedUserName() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                return auth.getName();
            }
        } catch (Exception e) {
            // should never happen!
            // e.printStackTrace();
        }
        return "tuntematon";
    }
}
