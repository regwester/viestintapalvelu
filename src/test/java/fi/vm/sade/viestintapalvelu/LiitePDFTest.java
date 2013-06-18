package fi.vm.sade.viestintapalvelu;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class LiitePDFTest {
    @ClassRule
    public static TomcatRule tomcat = new TomcatRule();

    private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
            "Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330",
            "Stockholm", "SL", "Sweden", "FI");

    public static class WhenCreatingLiiteWithOneHakutoive {

        private static Map<String, String> tulos = new HashMap<String, String>();
        private static String liite;

        @SuppressWarnings("unchecked")
        @BeforeClass
        public static void setUp() throws Exception {
            Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, "FI",
                    Arrays.asList(tulos));
            liite = TestUtil.generateLiite(kirje);
        }

        @Test
        public void liiteWasPrinted() throws Exception {
            assertTrue(liite != null);
        }
    }
}
