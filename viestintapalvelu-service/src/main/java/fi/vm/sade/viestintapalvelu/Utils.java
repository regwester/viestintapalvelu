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

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import fi.vm.sade.auditlog.ApplicationType;
import fi.vm.sade.auditlog.Audit;
import fi.vm.sade.viestintapalvelu.auditlog.AuditLogger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class Utils {
    private static Logger log = LoggerFactory.getLogger(Utils.class);
    private static final FastDateFormat THREAD_SAFE_DATE_FORMATTER = FastDateFormat.getInstance("dd.MM.yyyy_HH.mm");
    public static final Audit ViestintaPalveluAudit = new Audit(new AuditLogger(), "viestintapalvelu-service", ApplicationType.VIRKAILIJA); ;

    /**
     * Resolve template by name and language code
     * 
     * @param template
     * @param languageCode
     * @return template name
     */
    public static String resolveTemplateName(String template, String languageCode) {
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
    public static String resolveTemplateName(String template, String languageCode, String type) {
        languageCode = languageCode == null || "".equals(languageCode) ? "FI" : languageCode;
        languageCode = "SV".equalsIgnoreCase(languageCode) || "SE".equalsIgnoreCase(languageCode) || "FI".equalsIgnoreCase(languageCode) ? languageCode : "EN";

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
        return getAuthenticatedUserName() + "." + THREAD_SAFE_DATE_FORMATTER.format(new Date()) + "." + filename;
    }

    public static String getResource(String relativePath) {
        InputStream fs = Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath);
        return getResource(fs);
    }

    public static String getResource(InputStream fs) {
        try {
            return IOUtils.toString(fs, "UTF-8");
        } catch (IOException e) {
            log.error("Failed to read the resource file: {}.\n{}", fs.toString(), e.toString());
        } catch (NullPointerException e) {
            log.error("Resource not found.\n{}", e.toString());
        }
        return "";
    }

    public static Resource[] getResourceList(String pattern) {
        Resource[] resources = new Resource[0];
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
            resources = resolver.getResources(pattern);
        } catch (IOException e) {
            log.error("Failed to read the resources with pattern: {}.\n{}", pattern, e.toString());
        }
        return resources;
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
