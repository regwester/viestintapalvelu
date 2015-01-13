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
package fi.vm.sade.viestintapalvelu.externalinterface.asiointitili;

import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.AsiakasTilaKyselyVastausDto;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.AsiakasTilaTarkastusKyselyDto;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.HaeAsiakasTilojaKyselyDto;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.KohdeLisaysDto;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.KohdeLisaysVastausDto;

/**
 * User: ratamaa
 * Date: 10.10.2014
 * Time: 15:45
 */
public interface AsiointitiliCommunicationService {

    /**
     * Direct call to the Asiointitili-service.
     *
     * Omien asiointitiliasiakkaiden tarkistus
     *
     * Tämän kyselyn avulla viranomaisjärjestelmä voi tarkistaa mitkä sen asiakkaista ovat asiointitilipalvelun
     * käyttäjiä ja voivat siis vastaanottaa asiointiasioita asiointitilille. Kyselyn avulla voi hakea kaikki omat
     * asiakkaat tai tarkistaa annetun tai annettujen hetujen osalta, ovatko he ottaneet asiointitilin käyttöön.
     *
     * Vastauksessa saadaan tieto, onko  asiakas asiointitilin käyttäjä.
     *
     * @param kyselyDto tarkistuskysely
     * @return vastaus
     */
    AsiakasTilaKyselyVastausDto tarkistaAsiointitilinTila(AsiakasTilaTarkastusKyselyDto kyselyDto);

    /**
     * Direct call to the Asiointitili-service.
     *
     * Omien asiointitiliasiakkaiden tarkistus
     *
     * Kyselyä rajataan ajan suhteen siten, että haetaan vain tietyllä aikavälillä
     * asiointitilin käyttäjäksi liittyneet.
     *
     * Vastauksessa saadaan tieto, onko  asiakas asiointitilin käyttäjä.
     *
     * @param kyselyDto kysely
     * @return vastaus
     */
    AsiakasTilaKyselyVastausDto haeAsiakasTiloja(HaeAsiakasTilojaKyselyDto kyselyDto);

    /**
     * Direct call to the Asiointitili-service.
     *
     * Asiointiasian lähettäminen / viranomaisen tiedoksianto
     *
     * @param kyselyWS2 tiedoksianto
     * @return vastaus
     */
    KohdeLisaysVastausDto lisaaKohteitaAsiointitilille(KohdeLisaysDto kyselyWS2);
}
