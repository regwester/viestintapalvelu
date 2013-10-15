package fi.vm.sade.viestintapalvelu;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JalkiohjauskirjePDFTest {
    @ClassRule
    public static TomcatRule tomcat = new TomcatRule();

    private static AddressLabel label = new AddressLabel("Åle &", "Öistämö",
            "Brännkyrksgatan @ 177 B 149", "Södermalm", "13", "65330",
            "Stockholm", "SL", "Sweden", "SE");
    private static String pdf;

    @BeforeClass
    public static void setUp() throws Exception {
        Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, "FI",
                buildValidTulosList());
        pdf = TestUtil.generateJalkiohjauskirje(kirje).toString();
    }

    @Test
    public void isPrintedWithAddressLabel() throws Exception {
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
    public void canBePrintedInEN() throws Exception {
        testNotNullKirjeWithLanguageCode("EN");
    }

    @Test
    public void canBePrintedInSE() throws Exception {
        testNotNullKirjeWithLanguageCode("SE");
    }

    @Test
    public void canBePrintedWithoutLanguageCode() throws Exception {
        testNotNullKirjeWithLanguageCode(null);
    }

    @Test
    public void canBePrintedInSQ() throws Exception {
        testNotNullKirjeWithLanguageCode("SQ");
    }

    private void testNotNullKirjeWithLanguageCode(final String languageCode) throws Exception {
        Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, languageCode,
                buildValidTulosList());
        assertNotNull(TestUtil.generateJalkiohjauskirje(kirje));
    }

    public static List<Map<String, String>> buildValidTulosList() {
        Map<String, String> tulos = new ImmutableMap.Builder<String, String>()
                .put("organisaationNimi", "test")
                .put("oppilaitoksenNimi", "test")
                .put("hakukohteenNimi", "test")
                .put("hyvaksytyt", "1")
                .put("kaikkiHakeneet", "1")
                .put("omatPisteet", "1")
                .put("alinHyvaksyttyPistemaara", "1")
                .build();
        return ImmutableList.of(tulos);
    }

}
