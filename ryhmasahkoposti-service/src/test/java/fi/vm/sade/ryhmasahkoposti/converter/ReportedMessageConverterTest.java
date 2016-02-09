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

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReportedMessageConverter.class)
@ContextConfiguration("/test-bundle-context.xml")
public class ReportedMessageConverterTest {
    private ReportedMessageConverter reportedMessageConverter;
    @Mock
    CurrentUserComponent mockedCurrentUserComponent;

    @Before
    public void setup() {
        signIn();
        this.reportedMessageConverter = new ReportedMessageConverter(mockedCurrentUserComponent);
    }

    private void signIn() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Mockito.when(context.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContextHolder.setContext(context);
    }

    @Test
    public void testReportedMessageConversion() throws IOException {
        EmailMessage emailMessage = RaportointipalveluTestData.getEmailMessage();
        Henkilo henkilo = RaportointipalveluTestData.getHenkilo();
        henkilo.setOidHenkilo(emailMessage.getSenderOid());

        when(mockedCurrentUserComponent.getCurrentUser()).thenReturn(henkilo);

        ReportedMessage reportedMessage = reportedMessageConverter.convert(emailMessage);

        assertNotNull(reportedMessage);
        assertEquals(emailMessage.getSenderOid(), reportedMessage.getSenderOid());
    }
}
