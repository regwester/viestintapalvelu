/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.viestintapalvelu;

import org.junit.Ignore;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@Ignore
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
