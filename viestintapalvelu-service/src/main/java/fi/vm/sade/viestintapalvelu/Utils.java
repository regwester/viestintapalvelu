package fi.vm.sade.viestintapalvelu;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class Utils {
    private static Logger log = LoggerFactory.getLogger(Utils.class);
    private static final FastDateFormat THREAD_SAFE_DATE_FORMATTER = FastDateFormat
            .getInstance("dd.MM.yyyy_HH.mm");

    /**
     * Resolve template by name and language code
     * 
     * @param template
     * @param languageCode
     * @return template name
     */
    public static String resolveTemplateName(String template,
            String languageCode) {
        return resolveTemplateName(template, languageCode, null);
    }

    /**
     * Resolve template by name, language code and type
     * 
     * @param template
     * @param languageCode
     * @param type
     * @return template name
     */
    public static String resolveTemplateName(String template,
            String languageCode, String type) {
        languageCode = languageCode == null || "".equals(languageCode) ? "FI"
                : languageCode;
        languageCode = "SV".equalsIgnoreCase(languageCode)
                || "SE".equalsIgnoreCase(languageCode)
                || "FI".equalsIgnoreCase(languageCode) ? languageCode : "EN";

        if (type != null) {
            template = template.replace("{TYPE}", "_" + type);
        } else {
            template = template.replace("{TYPE}", "");
        }
        return template.replace("{LANG}", languageCode.toUpperCase());
    }

    public static String globalRandomId() {
        return UUID.randomUUID().toString();
    }

    public static String filenamePrefixWithUsernameAndTimestamp(String filename) {
        return new StringBuilder().append(getAuthenticatedUserName())
                .append(".")
                .append(THREAD_SAFE_DATE_FORMATTER.format(new Date()))
                .append(".").append(filename).toString();
    }

    public static String getResource(String resource) {
        try {
            InputStream fs = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            return IOUtils.toString(fs, "UTF-8");
        } catch(IOException e) {
            log.error("Failed to read the resource file: {}.\n{}", resource, e.toString());
        }
        return "";
    }

    private static String getAuthenticatedUserName() {
        try {
            Authentication auth = SecurityContextHolder.getContext()
                    .getAuthentication();
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
