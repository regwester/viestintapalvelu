package fi.vm.sade.viestintapalvelu;

import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class LiitePDFTest {
    //@ClassRule
    //public static TomcatRule tomcat = new TomcatRule();
    // TODO: FIX ME
    /*private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
            "Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330",
            "Stockholm", "SL", "Sweden", "FI");

    public static class WhenCreatingLiiteWithOneHakutoive {

        private static Map<String, String> tulos = new HashMap<String, String>();
        private static String liite;

        @SuppressWarnings("unchecked")
        @BeforeClass
        public static void setUp() throws Exception {
            Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, "FI",
                    JalkiohjauskirjePDFTest.buildValidTulosList());
            liite = TestUtil.generateLiite(kirje);
        }

        @Test
        public void liiteWasPrinted() throws Exception {
            assertTrue(liite != null);
        }
    }
    */
}
