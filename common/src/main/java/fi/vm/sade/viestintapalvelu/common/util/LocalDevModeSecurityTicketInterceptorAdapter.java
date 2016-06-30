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

package fi.vm.sade.viestintapalvelu.common.util;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import fi.vm.sade.authentication.cas.CasClient;

/**
 * User: ratamaa
 * Date: 29.9.2014
 * Time: 20:04
 */
public class LocalDevModeSecurityTicketInterceptorAdapter extends AbstractPhaseInterceptor<Message> {
    private static final String CAS_HEADER  =  "CasSecurityTicket";

    @Value("${auth.mode:cas}")
    private String authMode;
    @Value("${web.url.cas}")
    private String casService;
    private boolean basicAuthentication =false;
    private String appClientUsername;
    private String appClientPassword;
    private AbstractPhaseInterceptor<Message> target;

    public LocalDevModeSecurityTicketInterceptorAdapter() {
        super(Phase.PRE_PROTOCOL);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        final String casTargetService = getCasTargetService((String) message.get(Message.ENDPOINT_ADDRESS));

        boolean systemUse = this.appClientUsername != null && this.appClientPassword != null;
        Authentication authentication  =  SecurityContextHolder.getContext().getAuthentication();

        if (systemUse && basicAuthentication && "dev".equals(authMode)) {
            // auth.mode=dev and this is an basicAuthentication target component with system account -> use HTTP Basic:
            ((HttpURLConnection) message.get("http.connection")).setRequestProperty("Authorization", "Basic "
                    + getBasicAuthenticationEncoding(this.appClientUsername, this.appClientPassword));
        } else if(authentication instanceof UsernamePasswordAuthenticationToken && "dev".equals(authMode)) {
            // Development mode, this works provided that Spring Security's authentication manager has
            // erase-credientals = false: <authentication-manager alias = "authenticationManager"  erase-credentials = "false">
            UsernamePasswordAuthenticationToken token  =  (UsernamePasswordAuthenticationToken) authentication;
            if (!basicAuthentication) {
                Map<String,String> headers = provideTicketHeaders(casTargetService, token.getName(), ""  +  token.getCredentials());
                for (Map.Entry<String,String> header : headers.entrySet()) {
                    ((HttpURLConnection) message.get("http.connection")).setRequestProperty(header.getKey(), header.getValue());
                }
            } else {
                ((HttpURLConnection) message.get("http.connection")).setRequestProperty("Authorization", "Basic "
                        + getBasicAuthenticationEncoding(token.getName(), ""  +  token.getCredentials()));
            }
        } else {
            target.handleMessage(message);
        }
    }

    private Map<String, String> provideTicketHeaders(String serviceUrl, String username, String password) {
        Map<String, String> headers = new HashMap<>();
        String casHeader = CasClient.getTicket(casService + "/v1/tickets", username, password, serviceUrl);
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

    public void setBasicAuthentication(boolean basicAuthentication) {
        this.basicAuthentication = basicAuthentication;
    }

    public void setTarget(AbstractPhaseInterceptor<Message> target) {
        this.target = target;
    }

    public void setAppClientUsername(String appClientUsername) {
        this.appClientUsername = appClientUsername;
    }

    public void setAppClientPassword(String appClientPassword) {
        this.appClientPassword = appClientPassword;
    }
}
