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
package fi.vm.sade.ryhmasahkoposti.api.constants;

/**
 * Autentikointiin ja käyttöoikeuteen liittyviä vakioita
 *  
 * @author vehei1
 *
 */
public interface SecurityConstants {
	// Autentikointi  
	String USER_IS_AUTHENTICATED = "isAuthenticated()";
	String ALLOW_ALL = "permitAll";
	// Käyttäjäroolit
	String SEND = "hasRole('ROLE_APP_RYHMASAHKOPOSTI_SEND')";
	String READ = "hasRole('ROLE_APP_RYHMASAHKOPOSTI_VIEW')";
    // Järjestelmäkäyttäjälle takaisinkutsurajapintaan ryhmäsähköpostista viestintäpalveluun liitteiden lataamiseksi:
	String SYSTEM_ACCOUNT_ATTACHMENT_DOWNLOAD = "hasRole('ROLE_APP_ASIAKIRJAPALVELU_SYSTEM_ATTACHMENT_DOWNLOAD')";

    String ASIOINTITILI = "hasRole('ROLE_APP_ASIAKIRJAPALVELU_ASIOINTITILICRUD')";
}
