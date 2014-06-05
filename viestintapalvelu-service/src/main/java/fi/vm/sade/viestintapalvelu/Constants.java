package fi.vm.sade.viestintapalvelu;

import java.nio.charset.Charset;

// TODO vpeurala 22.5.2013: Maybe make these configurable?
public class Constants {
    public static final int IPOST_BATCH_LIMIT = 500;
    public static final String HYVAKSYMISKIRJE_TEMPLATE = "/hyvaksymiskirje_{LANG}.html";
    public static final String LIITE_TEMPLATE = "/liite_{LANG}.html";
    public static final String JALKIOHJAUSKIRJE_TEMPLATE = "/jalkiohjauskirje_{LANG}.html";
    public static final String KOEKUTSUKIRJE_TEMPLATE = "/koekutsukirje_{LANG}.html";
    public static final String LETTER_TEMPLATE = "/letter_{LANG}.html";
    public static final String IPOST_TEMPLATE = "/ipost.xml";
    public static final String LETTER_IPOST_TEMPLATE = "/letter_ipost.xml";
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Boolean IPOST_TEST = Boolean.FALSE;
    
    // Käyttöoikeudet
    public static final String ASIAKIRJAPALVELU_READ = "ROLE_APP_ASIAKIRJAPALVELU_READ";
    public static final String ASIAKIRJAPALVELU_CREATE_TEMPLATE = "ROLE_APP_ASIAKIRJAPALVELU_CREATE_TEMPLATE";
    public static final String ASIAKIRJAPALVELU_CREATE_LETTER = "ROLE_APP_ASIAKIRJAPALVELU_CREATE_LETTER";
    public static final String IPOSTI_READ = "ROLE_APP_IPOSTI_READ";
    public static final String IPOSTI_SEND = "ROLE_APP_IPOSTI_SEND";
}
