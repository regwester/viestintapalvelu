package fi.vm.sade.ryhmasahkoposti.api.constants;

/**
 * Autentikointiin ja käyttöoikeuteen liittyviä vakioita
 *  
 * @author vehei1
 *
 */
public interface SecurityConstants {
	// Autentikointi  
	public static final String USER_IS_AUTHENTICATED = "isAuthenticated()";
	
	// Käyttäjäroolit
	public static final String SEND = "hasRole('ROLE_APP_RYHMASAHKOPOSTI_SEND')";
	public static final String READ = "hasRole('ROLE_APP_RYHMASAHKOPOSTI_VIEW')";
    // Järjestelmäkäyttäjälle takaisinkutsurajapintaan ryhmäsähköpostista viestintäpalveluun liitteiden lataamiseksi:
    public static final String SYSTEM_ACCOUNT_ATTACHMENT_DOWNLOAD = "hasRole('ROLE_APP_ASIAKIRJAPALVELU_SYSTEM_ATTACHMENT_DOWNLOAD')";

    public static final String ASIOINTITILI = "hasRole('ROLE_APP_ASIAKIRJAPALVELU_ASIOINTITILICRUD')";
}
