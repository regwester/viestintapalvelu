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
package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageReplacement;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReportedMessageConverter.class)
@ContextConfiguration("/test-bundle-context.xml")
public class ReportedMessageReplacementConverterTest {

    private ReportedMessageReplacementConverter reportedMessageReplacementConverter;

    @Mock
    CurrentUserComponent mockedCurrentUserComponent;

    @Before
    public void setup() {

        this.reportedMessageReplacementConverter = new ReportedMessageReplacementConverter(mockedCurrentUserComponent);
    }

    @Test
    public void testReportedMessageReplacementConversion1() throws IOException {

        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();

        Henkilo henkilo = RaportointipalveluTestData.getHenkilo();
        henkilo.setOidHenkilo(reportedMessage.getSenderOid());

        when(mockedCurrentUserComponent.getCurrentUser()).thenReturn(henkilo);

        Set<ReplacementDTO> templateReplacements = RaportointipalveluTestData.getTemplateReplacements(1, 2, 3);
        List<ReplacementDTO> emailReplacements = RaportointipalveluTestData.getEmailReplacements(2, 3, 4, 5);

        List<ReportedMessageReplacement> reportedMessageReplacements = reportedMessageReplacementConverter.convert(reportedMessage, templateReplacements,
                emailReplacements);

        assertNotNull(reportedMessage);
        assertNotNull(reportedMessageReplacements);
        assertEquals(reportedMessageReplacements.size(), 3);

        ReportedMessageReplacement reportedMessageReplacement1 = reportedMessageReplacements.get(0);
        ReportedMessageReplacement reportedMessageReplacement2 = reportedMessageReplacements.get(1);
        ReportedMessageReplacement reportedMessageReplacement3 = reportedMessageReplacements.get(2);

        assertNotNull(reportedMessageReplacement1);
        assertNotNull(reportedMessageReplacement2);
        assertNotNull(reportedMessageReplacement3);

        if (reportedMessageReplacement1.getName().equals("key-1"))
            assertEquals(reportedMessageReplacement1.getDefaultValue(), "template-1");

        if (reportedMessageReplacement2.getName().equals("key-2"))
            assertEquals(reportedMessageReplacement2.getDefaultValue(), "email-2");

        if (reportedMessageReplacement3.getName().equals("key-3"))
            assertEquals(reportedMessageReplacement3.getDefaultValue(), "email-3");

    }

    @Test
    public void testReportedMessageReplacementConversion2() throws IOException {

        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();

        Henkilo henkilo = RaportointipalveluTestData.getHenkilo();
        henkilo.setOidHenkilo(reportedMessage.getSenderOid());

        when(mockedCurrentUserComponent.getCurrentUser()).thenReturn(henkilo);

        Set<ReplacementDTO> templateReplacements = RaportointipalveluTestData.getTemplateReplacements(1, 2, 3);
        List<ReplacementDTO> emailReplacements = RaportointipalveluTestData.getEmailReplacements(4, 5, 6, 7, 8);

        List<ReportedMessageReplacement> reportedMessageReplacements = reportedMessageReplacementConverter.convert(reportedMessage, templateReplacements,
                emailReplacements);

        assertNotNull(reportedMessage);
        assertNotNull(reportedMessageReplacements);
        assertEquals(reportedMessageReplacements.size(), 3);

        ReportedMessageReplacement reportedMessageReplacement1 = reportedMessageReplacements.get(0);
        ReportedMessageReplacement reportedMessageReplacement2 = reportedMessageReplacements.get(1);
        ReportedMessageReplacement reportedMessageReplacement3 = reportedMessageReplacements.get(2);

        assertNotNull(reportedMessageReplacement1);
        assertNotNull(reportedMessageReplacement2);
        assertNotNull(reportedMessageReplacement3);

        if (reportedMessageReplacement1.getName().equals("key-1"))
            assertEquals(reportedMessageReplacement1.getDefaultValue(), "template-1");

        if (reportedMessageReplacement2.getName().equals("key-2"))
            assertEquals(reportedMessageReplacement2.getDefaultValue(), "template-2");

        if (reportedMessageReplacement3.getName().equals("key-3"))
            assertEquals(reportedMessageReplacement3.getDefaultValue(), "template-3");

    }

    @Test
    public void testReportedMessageReplacementConversion3() throws IOException {

        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();

        Henkilo henkilo = RaportointipalveluTestData.getHenkilo();
        henkilo.setOidHenkilo(reportedMessage.getSenderOid());

        when(mockedCurrentUserComponent.getCurrentUser()).thenReturn(henkilo);

        Set<ReplacementDTO> templateReplacements = RaportointipalveluTestData.getTemplateReplacements(1, 2, 3);
        List<ReplacementDTO> emailReplacements = RaportointipalveluTestData.getEmailReplacements();

        List<ReportedMessageReplacement> reportedMessageReplacements = reportedMessageReplacementConverter.convert(reportedMessage, templateReplacements,
                emailReplacements);

        assertNotNull(reportedMessage);
        assertNotNull(reportedMessageReplacements);
        assertEquals(reportedMessageReplacements.size(), 3);

        ReportedMessageReplacement reportedMessageReplacement1 = reportedMessageReplacements.get(0);
        ReportedMessageReplacement reportedMessageReplacement2 = reportedMessageReplacements.get(1);
        ReportedMessageReplacement reportedMessageReplacement3 = reportedMessageReplacements.get(2);

        assertNotNull(reportedMessageReplacement1);
        assertNotNull(reportedMessageReplacement2);
        assertNotNull(reportedMessageReplacement3);

        if (reportedMessageReplacement1.getName().equals("key-1"))
            assertEquals(reportedMessageReplacement1.getDefaultValue(), "template-1");

        if (reportedMessageReplacement2.getName().equals("key-2"))
            assertEquals(reportedMessageReplacement2.getDefaultValue(), "template-2");

        if (reportedMessageReplacement3.getName().equals("key-3"))
            assertEquals(reportedMessageReplacement3.getDefaultValue(), "template-3");

    }

    @Test
    public void testReportedMessageReplacementConversion4() throws IOException {

        ReportedMessage reportedMessage = RaportointipalveluTestData.getReportedMessage();

        Henkilo henkilo = RaportointipalveluTestData.getHenkilo();
        henkilo.setOidHenkilo(reportedMessage.getSenderOid());

        when(mockedCurrentUserComponent.getCurrentUser()).thenReturn(henkilo);

        Set<ReplacementDTO> templateReplacements = RaportointipalveluTestData.getTemplateReplacements();
        List<ReplacementDTO> emailReplacements = RaportointipalveluTestData.getEmailReplacements(1, 2, 3);

        List<ReportedMessageReplacement> reportedMessageReplacements = reportedMessageReplacementConverter.convert(reportedMessage, templateReplacements,
                emailReplacements);

        assertNotNull(reportedMessage);
        assertEquals(reportedMessageReplacements.size(), 0);

    }

}
