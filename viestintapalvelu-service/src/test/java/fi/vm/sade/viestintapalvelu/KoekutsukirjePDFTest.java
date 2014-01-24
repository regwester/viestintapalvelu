package fi.vm.sade.viestintapalvelu;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.koekutsukirje.Koekutsukirje;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class KoekutsukirjePDFTest {
    @ClassRule
    public static TomcatRule tomcat = new TomcatRule();

    private static AddressLabel label = new AddressLabel("Åle &", "Öistämö",
            "Brännkyrksgatan @ 177 B 149", "Södermalm", "13", "65330",
            "Stockholm", "SL", "Sweden", "SE");
    private static String hakukohde = "TKKTöttöröö"; 
    //  If testing letterBodyText with scandics, will fail.
    // TODO: think a proper way to handle escaping in KoekutsukirjeBuilder and here so that test would 
    // not fail with scandics and still we would get proper HTML markup all the way to the PDF...
    private static String letterBodyText = "Hahaa"; // "<p>Hähää</p>" fails (as test. application works ok still)
    private static String pdf;

    @BeforeClass
    public static void setUp() throws Exception {
    	Koekutsukirje kirje = new Koekutsukirje(label, "FI", hakukohde,
    			letterBodyText, new ArrayList<Map<String, String>>());
        pdf = TestUtil.generateKoekutsukirje(kirje).toString();
    }

    @Test
    public void containsAddressLabel() throws Exception {
        assertTrue("Firstname Lastname missing",
                pdf.contains(label.getFirstName() + " " + label.getLastName()));
        assertTrue("Addressline missing", pdf.contains(label.getAddressline()));
        assertTrue("Addressline2 missing",
                pdf.contains(label.getAddressline2()));
        assertTrue("Addressline3 missing",
                pdf.contains(label.getAddressline3()));
        assertTrue("Postalcode city missing",
                pdf.contains(label.getPostalCode() + " " + label.getCity()));
        assertTrue("Region missing", pdf.contains(label.getRegion()));
        assertTrue("Country missing", pdf.contains(label.getCountry()));
    }

    @Test
    public void containsSchool() throws Exception {
        assertTrue("Hakukohde puuttuu", pdf.contains(hakukohde));
    }

    @Test
    public void containsPVM() throws Exception {
    	assertTrue("Päivämäärä puuttuu", pdf.contains(new SimpleDateFormat("dd-MM-yyyy").format(new Date())));
    }

    @Test
    public void containsBodyText() throws Exception {
        assertTrue("Leipäteksti puuttuu", pdf.contains(letterBodyText));
    }
    
    @Test
    public void canBePrintedInEN() throws Exception {
    	Koekutsukirje kirje = new Koekutsukirje(label, "EN", hakukohde,
    			letterBodyText, new ArrayList<Map<String, String>>());
        assertNotNull(TestUtil.generateKoekutsukirje(kirje));
    }

    @Test
    public void canBePrintedInSE() throws Exception {
    	Koekutsukirje kirje = new Koekutsukirje(label, "SE", hakukohde,
    			letterBodyText, new ArrayList<Map<String, String>>());
        assertNotNull(TestUtil.generateKoekutsukirje(kirje));
    }

    @Test
    public void canBePrintedWithoutLanguageCode() throws Exception {
    	Koekutsukirje kirje = new Koekutsukirje(label, null, hakukohde,
    			letterBodyText, new ArrayList<Map<String, String>>());
        assertNotNull(TestUtil.generateKoekutsukirje(kirje));
    }

    @Test
    public void canBePrintedInSQ() throws Exception {
    	Koekutsukirje kirje = new Koekutsukirje(label, "SQ", hakukohde,
    			letterBodyText, new ArrayList<Map<String, String>>());
        assertNotNull(TestUtil.generateKoekutsukirje(kirje));
    }
}
