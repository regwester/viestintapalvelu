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
}
