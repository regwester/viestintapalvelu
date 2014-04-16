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
}
