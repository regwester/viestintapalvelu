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
 **/package fi.vm.sade.viestintapalvelu;

import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextUserAgent;

import java.io.InputStream;

/**
 * If on a template external file is referenced, flying saucer uses user agent to load the referenced
 * resource. For example:
 * <p/>
 * <link rel="stylesheet" type="text/css" href="classpath:/template_styles/ipost_pdf.css"/>
 * <p/>
 * See also: https://svn.atlassian.com/svn/public/atlassian/vendor/xhtmlrenderer-8.0/tags/8.3-atlassian/www/r7/users-guide-r7.html#xil_17
 */
public class OPHUserAgent extends ITextUserAgent {

    private static final String CLASSPATH_SCHEME = "classpath";
    private static final String SCHEME_SEPARATOR = ":";

    public OPHUserAgent(ITextOutputDevice outputDevice) {
        super(outputDevice);
    }

    @Override
    protected InputStream resolveAndOpenStream(String uri) {
        if (isClasspathResource(uri)) {
            return getClass()
                    .getResourceAsStream(toPlainClasspathResource(uri));
        } else {
            return super.resolveAndOpenStream(uri);
        }
    }

    @Override
    public String resolveURI(String uri) {
        if (isClasspathResource(uri)) {
            return uri;
        }
        return super.resolveURI(uri);
    }

    private boolean isClasspathResource(String uri) {
        return uri != null && uri.startsWith(CLASSPATH_SCHEME + SCHEME_SEPARATOR);
    }

    private String toPlainClasspathResource(String uri) {
        return uri.replace(CLASSPATH_SCHEME + SCHEME_SEPARATOR, "");
    }
}
