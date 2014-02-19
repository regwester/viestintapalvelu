package fi.vm.sade.ryhmasahkoposti.api.constants;

/**
 * Autentikointiin ja käyttöoikeuteen liittyviä vakioita
 *  
 * @author vehei1
 *
 */
public interface SecurityAndRoleConstants {
	// Autentikointi  
	public static final String USER_IS_AUTHENTICATED = "isAuthenticated()";
	
	// Käyttäjäroolit
	public static final String SEND = "ROLE_APP_RYHMASAHKOPOSTI_SEND";
	public static final String READ = "ROLE_APP_RYHMASAHKOPOSTI_READ";
}
