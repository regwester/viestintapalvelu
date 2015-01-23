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

import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

public class AddressLabelIT extends AbstractWebServiceIT {

    @Test
    public void createLabelForValidForeignAddress() {
        /*AddressLabel label = new AddressLabel("Åle", "Öistämö", "Brännkyrksgatan 177 B 149",
                "Södermalm", "13", "65330", "Stockholm", "SL", "Sweden", "SE");
        Entity<AddressLabel> addressLabelEntity = Entity.entity(label, MediaType.APPLICATION_JSON);
        Response res = target(getResourcePath() + "pdf").request().post(addressLabelEntity);
        String output = res.readEntity(String.class);
        System.out.println(output);
        */
        /*
        List<String> pdf = new ArrayList<String>();
        assertThat(pdf).hasSize(7);
        assertThat(pdf.get(0)).isEqualTo("Åle Öistämö");
        assertThat(pdf.get(1)).isEqualTo("Brännkyrksgatan 177 B 149");
        assertThat(pdf.get(2)).isEqualTo("Södermalm");
        assertThat(pdf.get(3)).isEqualTo("13");
        assertThat(pdf.get(4)).isEqualTo("65330 Stockholm");
        assertThat(pdf.get(5)).isEqualTo("SL");
        assertThat(pdf.get(6)).isEqualTo("Sweden");
        */
    }

    @Override
    protected ResourceConfig configure(ResourceConfig rc) {
        return rc;
    }

    @Override
    protected String getResourcePath() {
        return "addresslabel/";
    }

    /*
    public static class WhenCreatingLabelWithSpecialCharacters {
        @Test
        public void specialCharactersAreDisplayed() throws Exception {
            List<String> label = callGenerateLabels("Åle &", "Öistämö #", "Brännkyrksgatan 177 B 149&", "Södermalm $€",
                    "13@", "65330&", "Stockholm&", "SL&", "Sweden&", "SE");
            assertEquals("Åle & Öistämö #", label.get(0));
            assertEquals("Brännkyrksgatan 177 B 149&", label.get(1));
            assertEquals("Södermalm $€", label.get(2));
            assertEquals("13@", label.get(3));
            assertEquals("65330& Stockholm&", label.get(4));
            assertEquals("SL&", label.get(5));
            assertEquals("Sweden&", label.get(6));
        }
    }

    public static class WhenFirstNameIsEmpty {
        @Test
        public void firstRowContainsLastName() throws Exception {
            assertEquals(
                    "Öistämö",
                    callGenerateLabels("", "Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330",
                            "Stockholm", "SL", "Sweden", "SE").get(0));
        }
    }

    public static class WhenLastNameIsEmpty {
        @Test
        public void firstRowContainsFirstName() throws Exception {
            assertEquals(
                    "Åle",
                    callGenerateLabels("Åle", "", "Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm",
                            "SL", "Sweden", "SE").get(0));
        }
    }

    public static class WhenAddresslineIsEmpty {
        @Test
        public void labelContainsNameAddressline2Addressline3PostOfficeRegionAndCountry() throws Exception {
            List<String> label = callGenerateLabels("Åle", "Öistämö", "", "Södermalm", "13", "65330", "Stockholm",
                    "SL", "Sweden", "SE");
            assertEquals("Åle Öistämö", label.get(0));
            assertEquals("Södermalm", label.get(1));
            assertEquals("13", label.get(2));
            assertEquals("65330 Stockholm", label.get(3));
            assertEquals("SL", label.get(4));
            assertEquals("Sweden", label.get(5));
            assertEquals(6, label.size());
        }
    }

    public static class WhenAddressline2IsEmpty {
        @Test
        public void labelContainsNameAddresslineAddressline3PostOfficeAndCountry() throws Exception {
            List<String> label = callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "", "13", "65330",
                    "Stockholm", "SL", "Sweden", "SE");
            assertEquals("Åle Öistämö", label.get(0));
            assertEquals("Brännkyrksgatan 177 B 149", label.get(1));
            assertEquals("13", label.get(2));
            assertEquals("65330 Stockholm", label.get(3));
            assertEquals("SL", label.get(4));
            assertEquals("Sweden", label.get(5));
            assertEquals(6, label.size());
        }
    }

    public static class WhenAddressline3IsEmpty {
        @Test
        public void labelContainsNameAddresslineAddressline3PostOfficeAndCountry() throws Exception {
            List<String> label = callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", "",
                    "65330", "Stockholm", "SL", "Sweden", "SE");
            assertEquals("Åle Öistämö", label.get(0));
            assertEquals("Brännkyrksgatan 177 B 149", label.get(1));
            assertEquals("Södermalm", label.get(2));
            assertEquals("65330 Stockholm", label.get(3));
            assertEquals("SL", label.get(4));
            assertEquals("Sweden", label.get(5));
            assertEquals(6, label.size());
        }
    }

    public static class WhenPostalCodeIsEmpty {
        @Test
        public void fifthRowContainsPostOffice() throws Exception {
            assertEquals(
                    "Stockholm",
                    callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", "13", "",
                            "Stockholm", "SL", "Sweden", "SE").get(4));
        }
    }

    public static class WhenCityIsEmpty {
        @Test
        public void fifthRowContainsPostalCode() throws Exception {
            assertEquals(
                    "65330",
                    callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "",
                            "SL", "Sweden", "SE").get(4));
        }
    }

    public static class WhenCountryIsEmpty {
        @Test
        public void labelContainsNameAddresslineAddressline2Addressline3CityAndRegion() throws Exception {
            List<String> label = callGenerateLabels("Åle", "Öistämö", "Brännkyrksgatan 177 B 149", "Södermalm", "13",
                    "65330", "Stockholm", "SL", "", "FI");
            assertEquals("Åle Öistämö", label.get(0));
            assertEquals("Brännkyrksgatan 177 B 149", label.get(1));
            assertEquals("Södermalm", label.get(2));
            assertEquals("13", label.get(3));
            assertEquals("65330 Stockholm", label.get(4));
            assertEquals("SL", label.get(5));
            assertEquals(6, label.size());
        }
    }

    public static class WhenAddressIsLocal {
        @Test
        public void labelHasOnlyThreeRows() throws Exception {
            List<String> label = callGenerateLabels("Åle", "Öistämö", "Mannerheimintie 177 B 149", "", "", "65330",
                    "Helsinki", "", "Finland", "FI");
            assertEquals("Åle Öistämö", label.get(0));
            assertEquals("Mannerheimintie 177 B 149", label.get(1));
            assertEquals("65330 Helsinki", label.get(2));
            assertEquals(3, label.size());
        }
    }

    public static class WhenAddressIsLocalAndCountryIsUppercaseFINLAND {
        @Test
        public void labelHasOnlyThreeRows() throws Exception {
            List<String> label = callGenerateLabels("Åle", "Öistämö", "Mannerheimintie 177 B 149", "", "", "65330",
                    "Helsinki", "", "FINLAND", "FI");
            assertEquals("Åle Öistämö", label.get(0));
            assertEquals("Mannerheimintie 177 B 149", label.get(1));
            assertEquals("65330 Helsinki", label.get(2));
            assertEquals(3, label.size());
        }
    }

    public static class WhenCreatingLabelsForDomesticAndForeignAddresses {

        private static AddressLabel domestic = new AddressLabel("Åle", "Öistämö", "Mannerheimintie 177 B 149", "", "",
                "65330", "Helsinki", "", "FINLAND", "FI");
        private static AddressLabel foreign = new AddressLabel("Åle", "Öistämö", "Brännkyrksgatan 177 B 149",
                "Södermalm", "13", "65330", "Stockholm", "SL", "Sweden", "SE");
        private static List<List<String>> response;

        @BeforeClass
        public static void setUp() throws Exception {
            response = TestUtil.generateAddressLabelsPDF(Arrays.asList(domestic, foreign));
        }

        @Test
        public void responseContainsTwoAddressLabels() throws Exception {
            assertEquals(2, response.size());
        }

        @Test
        public void domesticAddressHasThreeRows() throws Exception {
            assertEquals(3, response.get(1).size()); // Order reversed
            // when parsing pdf
            // to html
        }

        @Test
        public void foreignAddressHasSevenRows() throws Exception {
            assertEquals(7, response.get(0).size()); // Order reversed
            // when parsing pdf
            // to html
        }
    }

    public static class WhenCreatingLabelsInABigBatch {

        private static List<AddressLabel> batch;
        private static List<List<String>> response;

        @BeforeClass
        public static void setUp() throws Exception {
            batch = createLabels(1000);
            response = TestUtil.generateAddressLabelsPDF(batch);
        }

        @Test
        public void returnedCSVContainsEqualAmountOfRows() throws Exception {
            assertEquals(batch.size(), response.size());
        }
    }

    private static List<AddressLabel> createLabels(int count) throws IOException {
        return new Generator<AddressLabel>() {
            protected AddressLabel createObject(TestData testData) {
                String postOffice = testData.random("postOffice");
                String[] country = testData.randomArray("country");
                return new AddressLabel(testData.random("firstname"), testData.random("lastname"),
                        testData.random("street") + " " + testData.random("houseNumber"), "", "", postOffice.substring(
                                0, postOffice.indexOf(" ")), postOffice.substring(postOffice.indexOf(" ") + 1), "",
                        country[0], country[1]);
            }
        }.generateObjects(count);
    }

    private static List<String> callGenerateLabels(String firstName, String lastName, String addressline,
            String addressline2, String addressline3, String postalCode, String postOffice, String region,
            String country, String countryCode) throws Exception {
        return TestUtil.generateAddressLabelsPDF(
                Arrays.asList(new AddressLabel(firstName, lastName, addressline, addressline2, addressline3,
                        postalCode, postOffice, region, country, countryCode))).get(0);
    }
    */
}
