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

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fi.suomi.asiointitili.*;
import fi.vm.sade.viestintapalvelu.asiointitili.AsiointitiliService;

import static fi.vm.sade.viestintapalvelu.asiointitili.ws.XMLTypeHelper.dateTime;
import static org.joda.time.DateTime.now;

/**
 * User: ratamaa
 * Date: 10.10.2014
 * Time: 15:46
 */
@Service
public class AsiointitiliServiceImpl implements AsiointitiliService {
    public static final String SANOMA_VERSIO = "1.0";

//    @Value("${viestintapalvelu.asiointitili.service.location}")
//    private String serviceLocation;
//    @Value("${viestintapalvelu.asiointitili.wss.keystore.instance:JKS}")
//    private String keystoreInstance;
//    @Value("${viestintapalvelu.asiointitili.wss.keystore.file}")
//    private String keystoreFile;
//    @Value("${viestintapalvelu.asiointitili.wss.keystore.password}")
//    private String keystorePw;
//    @Value("${viestintapalvelu.asiointitili.wss.keystore.alias}")
//    private String keystoreAlias;
    @Value("${viestintapalvelu.asiointitili.viranomainen.kayttajatunnus}")
    private String viranomainenKayttajatunnus;
    @Value("${viestintapalvelu.asiointitili.viranomainen.palvelutunnus}")
    private String virnaomainenPalvelutunnus;
    @Value("${viestintapalvelu.asiointitili.viranomainen.sanoma.varmenne}")
    private String virnaomainenSanomaVarmenne;

    @Resource
    private Viranomaispalvelut asiointitiliViranomaispalvelutClient;

//    private Viranomaispalvelut service;
//
//    public Viranomaispalvelut getService() {
//        if (this.service == null) {
//            Viranomaispalvelut_Service service = null;
//            try {
//                service = new Viranomaispalvelut_Service(new URL(serviceLocation));
//            } catch (MalformedURLException e) {
//                throw new IllegalArgumentException(e);
//            }
//            service.setHandlerResolver(new HandlerResolver() {
//                @Override
//                public List<Handler> getHandlerChain(PortInfo portInfo) {
//                    return Arrays.asList((Handler) new SOAPSigningHanlder(
//                            keystoreInstance,
//                            keystoreFile,
//                            keystorePw,
//                            keystoreAlias
//                    ));
//                }
//            });
//            this.service = service.getViranomaispalvelutSoap();
//        }
//        return this.service;
//    }

    protected Viranomainen createViranomainen() {
        Viranomainen virnaomainen = new Viranomainen();
        virnaomainen.setKayttajaTunnus(viranomainenKayttajatunnus);
        virnaomainen.setPalveluTunnus(virnaomainenPalvelutunnus);
        virnaomainen.setSanomaVersio(SANOMA_VERSIO);
        virnaomainen.setSanomaVarmenneNimi(virnaomainenSanomaVarmenne);
        virnaomainen.setSanomaTunniste(UUID.randomUUID().toString());
        return virnaomainen;
    }

    @Override
    public VastausWS2 kysely() {
        KyselyWS2 kysely = new KyselyWS2();
        kysely.setKohdeMaara(1);
        ArrayOfKohdeWS2 kohteet = new ArrayOfKohdeWS2();
        KohdeWS2 kohde = new KohdeWS2();
        Asiakas asiakas = new Asiakas();
        asiakas.setAsiakasTunnus("010101-123N");
        asiakas.setTunnusTyyppi("SSN");
        kohde.getAsiakas().add(asiakas);
        kohde.setViranomaisTunniste("13f35ca5-b892-48e3-bc0c-3c1ac5413i4x");
        kohde.setVahvistusVaatimus("0");
        kohde.setAsiaNumero(null);
        kohde.setNimeke("testi kaksirivi");
        kohde.setLahetysPvm(dateTime(now()));
        kohde.setLahettajaNimi("Teppo Tallaaja");
        kohde.setKuvausTeksti("Tommin testiviesti");
        kohde.setMaksullisuus("0");
        kohteet.getKohde().add(kohde);
        kysely.setKohteet(kohteet);
        return asiointitiliViranomaispalvelutClient.lisaaKohteita(createViranomainen(), kysely);
    }

}
