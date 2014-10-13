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
    @Value("${viestintapalvelu.asiointitili.viranomainen.viranomaistunnus}")
    private String viranomainenTunnus;
    @Value("${viestintapalvelu.asiointitili.viranomainen.kayttajatunnus}")
    private String viranomainenKayttajatunnus;
    @Value("${viestintapalvelu.asiointitili.viranomainen.palvelutunnus}")
    private String virnaomainenPalvelutunnus;
    @Value("${viestintapalvelu.asiointitili.viranomainen.sanoma.varmenne}")
    private String virnaomainenSanomaVarmenne;

    @Resource
    private Viranomaispalvelut asiointitiliViranomaispalvelutClient;
    protected Viranomainen createViranomainen() {
        Viranomainen virnaomainen = new Viranomainen();
        virnaomainen.setViranomaisTunnus(viranomainenTunnus);
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
        kohde.setViranomaisTunniste(UUID.randomUUID().toString());
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
