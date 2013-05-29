package fi.vm.sade.viestintapalvelu;

import java.nio.charset.Charset;

// TODO vpeurala 22.5.2013: Maybe make these configurable?
public class Constants {
	public static final int IPOST_BATCH_LIMIT = 10;
	public final static String HYVAKSYMISKIRJE_TEMPLATE = "/hyvaksymiskirje_{LANG}.html";
	public final static String LIITE_TEMPLATE = "/liite_{LANG}.html";
	public final static String JALKIOHJAUSKIRJE_TEMPLATE = "/jalkiohjauskirje_{LANG}.html";
	public final static String IPOST_TEMPLATE = "/ipost.xml";
	public final static Charset UTF_8 = Charset.forName("UTF-8");
}
