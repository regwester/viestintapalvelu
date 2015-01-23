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
package fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.impl;

import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.suomi.asiointitili.*;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.AsiointitiliCommunicationService;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.*;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.converter.AsiointitiliDtoConverter;

/**
 * User: ratamaa
 * Date: 10.10.2014
 * Time: 15:46
 */
@Service
public class AsiointitiliCommunicationServiceImpl implements AsiointitiliCommunicationService {
    private static final Logger logger = LoggerFactory.getLogger(AsiointitiliCommunicationServiceImpl.class);

    public static final String SANOMA_VERSIO = "1.0";
    public static final String KYSELYLAJI_ASIAKKAAT = "Asiakkaat";
    public static final String KYSELYLAJI_KAIKKI_ASIAKKAAT = "Kaikki";

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

    @Autowired
    private AsiointitiliDtoConverter asiointitiliDtoConverter;

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
    @Transactional
    public KohdeLisaysVastausDto lisaaKohteitaAsiointitilille(KohdeLisaysDto kyselyDto) {
        KyselyWS2 kysely = asiointitiliDtoConverter.convert(kyselyDto, new KyselyWS2());
        Viranomainen viranomainen = createViranomainen();
        // TODO: Save to database (including viranomainen.sanomaTunniste)?
        VastausWS2 vastaus = asiointitiliViranomaispalvelutClient.lisaaKohteita(viranomainen, kysely);
        // TODO: Save vastaus to database?
        return asiointitiliDtoConverter.convert(vastaus, new KohdeLisaysVastausDto());
    }

    @Override
    @Transactional
    public AsiakasTilaKyselyVastausDto tarkistaAsiointitilinTila(AsiakasTilaTarkastusKyselyDto kyselyDto) {

        KyselyWS1 kysely = asiointitiliDtoConverter.convert(kyselyDto, new KyselyWS1());
        kysely.setKyselyLaji(KYSELYLAJI_ASIAKKAAT);
        Viranomainen viranomainen = createViranomainen();
        logger.info("Requesting tarkistaAsiointitilinTila query (message id: {}) for {} customers",
                viranomainen.getSanomaTunniste(),
                kyselyDto.getAsiakkaat().size());
        // TODO: Save to database (including viranomainen.sanomaTunniste)?
        VastausWS1 vastaus = asiointitiliViranomaispalvelutClient.haeAsiakkaita(viranomainen, kysely);
        // TODO: Save vastaus to database?
        AsiakasTilaKyselyVastausDto vastausDto = asiointitiliDtoConverter.convert(vastaus, new AsiakasTilaKyselyVastausDto());
        logger.info("Received tarkistaAsiointitilinTila response (message id: {}) with status: {} ({})",
                vastausDto.getSanomaTunniste(), vastausDto.getTilaKoodi(),
                vastausDto.getKuvaus());
        return vastausDto;
    }

    @Override
    @Transactional
    public AsiakasTilaKyselyVastausDto haeAsiakasTiloja(HaeAsiakasTilojaKyselyDto kyselyDto) {
        KyselyWS1 kysely = asiointitiliDtoConverter.convert(kyselyDto, new KyselyWS1());
        kysely.setKyselyLaji(KYSELYLAJI_KAIKKI_ASIAKKAAT);
        Viranomainen viranomainen = createViranomainen();
        // TODO: Save to database (including viranomainen.sanomaTunniste)?
        VastausWS1 vastaus = asiointitiliViranomaispalvelutClient.haeAsiakkaita(viranomainen, kysely);
        // TODO: Save vastaus to database?
        return asiointitiliDtoConverter.convert(vastaus, new AsiakasTilaKyselyVastausDto());
    }

    public void setAsiointitiliDtoConverter(AsiointitiliDtoConverter asiointitiliDtoConverter) {
        this.asiointitiliDtoConverter = asiointitiliDtoConverter;
    }

    public void setViranomainenTunnus(String viranomainenTunnus) {
        this.viranomainenTunnus = viranomainenTunnus;
    }

    public void setViranomainenKayttajatunnus(String viranomainenKayttajatunnus) {
        this.viranomainenKayttajatunnus = viranomainenKayttajatunnus;
    }

    public void setVirnaomainenPalvelutunnus(String virnaomainenPalvelutunnus) {
        this.virnaomainenPalvelutunnus = virnaomainenPalvelutunnus;
    }

    public void setVirnaomainenSanomaVarmenne(String virnaomainenSanomaVarmenne) {
        this.virnaomainenSanomaVarmenne = virnaomainenSanomaVarmenne;
    }
}
