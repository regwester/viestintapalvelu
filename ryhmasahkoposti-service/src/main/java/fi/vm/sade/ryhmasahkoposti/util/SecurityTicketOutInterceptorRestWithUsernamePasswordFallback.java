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

package fi.vm.sade.ryhmasahkoposti.util;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import fi.vm.sade.authentication.cas.CasClient;
import fi.vm.sade.generic.ui.portlet.security.AbstractSecurityTicketOutInterceptor;
import fi.vm.sade.generic.ui.portlet.security.ProxyAuthenticator;

/**
 * User: ratamaa
 * Date: 29.9.2014
 * Time: 14:30
 */
// korvattava httpsessio/cookie pohjaisella ratkaisulla, esim: SessionBasedCxfAuthInterceptor
// based on fi.vm.sade.generic.ui.portlet.security.SecurityTicketOutInterceptorRest
public class SecurityTicketOutInterceptorRestWithUsernamePasswordFallback
        extends AbstractSecurityTicketOutInterceptor<Message> {
    private static final String CAS_HEADER  =  "CasSecurityTicket";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${auth.mode:cas}")
    private String authMode;
    @Value("${web.url.cas}")
    private String casService;
    private boolean internal=false;

    private ProxyAuthenticator proxyAuthenticator = new ProxyAuthenticator();

    @Override
    public void handleMessage(final Message message) throws Fault {
        final String casTargetService = getCasTargetService((String) message.get(Message.ENDPOINT_ADDRESS));

        Authentication authentication  =  SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof UsernamePasswordAuthenticationToken && "dev".equals(authMode)) {
            // Development mode, this works provided that Spring Security's authentication manager has
            // erase-credientals = false: <authentication-manager alias = "authenticationManager"  erase-credentials = "false">
            UsernamePasswordAuthenticationToken token  =  (UsernamePasswordAuthenticationToken) authentication;
            if (!internal) {
                Map<String,String> headers = provideTicketHeaders(casTargetService, token.getName(), ""  +  token.getCredentials());
                for (Map.Entry<String,String> header : headers.entrySet()) {
                    ((HttpURLConnection) message.get("http.connection")).setRequestProperty(header.getKey(), header.getValue());
                }
            } else {
                ((HttpURLConnection) message.get("http.connection")).setRequestProperty("Authorization", "Basic "
                        + getBasicAuthenticationEncoding(token.getName(), ""  +  token.getCredentials()));
            }
        } else {
            proxyAuthenticator.proxyAuthenticate(casTargetService, authMode, new ProxyAuthenticator.Callback() {
                @Override
                public void setRequestHeader(String key, String value) {
                    log.info("setRequestHeader: " + key + "=" + value + " (targetService: " + casTargetService + ")");
                    ((HttpURLConnection) message.get("http.connection")).setRequestProperty(key, value);
                }

                @Override
                public void gotNewTicket(Authentication authentication, String proxyTicket) {
                    log.info("gotNewTicket, auth: " + authentication.getName() + ", proxyTicket: " + proxyTicket
                            + ", (targetService: " + casTargetService + ")");
                }
            });
        }
    }

    private Map<String, String> provideTicketHeaders(String serviceUrl, String username, String password) {
        Map<String, String> headers  =  new HashMap<String, String>();
        String casHeader  =  CasClient.getTicket(casService + "/v1/tickets", username, password, serviceUrl);
        headers.put(CAS_HEADER, casHeader);
        return headers;
    }

    private String getBasicAuthenticationEncoding(String username, String password) {
        String userPassword = username + ":" + password;
        return new String(Base64.encodeBase64(userPassword.getBytes()));
    }

    /**
     * Get cas service from url string, get string before 4th '/' char. For
     * example:
     * <p/>
     * https://asd.asd.asd:8080/backend-service/asd/qwe/qwe2.foo?bar=asd --->
     * https://asd.asd.asd:8080/backend-service
     */
    private static String getCasTargetService(String url) {
        return url.replaceAll("(.*?//.*?/.*?)/.*", "$1") + "/j_spring_cas_security_check";
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }
}
