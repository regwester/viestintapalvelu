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

package fi.vm.sade.viestintapalvelu.asiointitili.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fi.suomi.asiointitili.KyselyWS10;
import fi.suomi.asiointitili.Viranomainen;
import fi.suomi.asiointitili.Viranomaispalvelut;
import fi.suomi.asiointitili.Viranomaispalvelut_Service;
import fi.vm.sade.viestintapalvelu.asiointitili.AsiointitiliService;
import fi.vm.sade.viestintapalvelu.asiointitili.ws.SOAPSigningHanlder;

/**
 * User: ratamaa
 * Date: 10.10.2014
 * Time: 15:46
 */
@Service
public class AsiointitiliServiceImpl implements AsiointitiliService {
    @Value("${viestintapalvelu.asiointitili.service.location}")
    private String serviceLocation;
    @Value("${viestintapalvelu.asiointitili.wss.keystore.instance:JKS}")
    private String keystoreInstance;
    @Value("${viestintapalvelu.asiointitili.wss.keystore.file}")
    private String keystoreFile;
    @Value("${viestintapalvelu.asiointitili.wss.keystore.password}")
    private String keystorePw;
    @Value("${viestintapalvelu.asiointitili.wss.keystore.alias}")
    private String keystoreAlias;

    private Viranomaispalvelut service;

    public Viranomaispalvelut getService() {
        if (this.service == null) {
            Viranomaispalvelut_Service service = null;
            try {
                service = new Viranomaispalvelut_Service(new URL(serviceLocation));
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
            service.setHandlerResolver(new HandlerResolver() {
                @Override
                public List<Handler> getHandlerChain(PortInfo portInfo) {
                    return Arrays.asList((Handler) new SOAPSigningHanlder(
                            keystoreInstance,
                            keystoreFile,
                            keystorePw,
                            keystoreAlias
                    ));
                }
            });
            this.service = service.getViranomaispalvelutSoap();
        }
        return this.service;
    }

    @Override
    public void doSome() {
        getService().haeTilaTieto(new Viranomainen(), new KyselyWS10());
    }

}
