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
package fi.vm.sade.viestintapalvelu.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuDetailsDto;
import fi.vm.sade.viestintapalvelu.externalinterface.component.TarjontaComponent;
import fi.vm.sade.viestintapalvelu.options.OptionsResource;

import static fi.vm.sade.viestintapalvelu.util.AnswerChain.atFirstReturn;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * User: ratamaa
 * Date: 7.10.2014
 * Time: 16:18
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("/test-application-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class OptionsResourceTest {
    @Mock
    private TarjontaComponent tarjontaComponent;
    @InjectMocks
    private OptionsResource optionsResource;

    @Test
    public void testCache() {
        when(tarjontaComponent.findPublished(any(Integer.class))).thenAnswer(atFirstReturn(testAnswer("1234"))
                .thenThrow(new IllegalStateException("Should not be called twice!")));

        List<HakuDetailsDto> results = optionsResource.listHakus(null);
        assertEquals(1, results.size());
        assertEquals("1234", results.get(0).getOid());
        results = optionsResource.listHakus(null);
        assertEquals(1, results.size());
        assertEquals("1234", results.get(0).getOid());
    }

    private List<HakuDetailsDto> testAnswer(String oid) {
        List<HakuDetailsDto> dtos = new ArrayList<HakuDetailsDto>();
        HakuDetailsDto result = new HakuDetailsDto();
        result.setOid(oid);
        result.setNimi(new HashMap<String, String>());
        dtos.add(result);
        return dtos;
    }

    @Test
    public void testForceCacheRefresh() {
        when(tarjontaComponent.findPublished(any(Integer.class))).thenAnswer(atFirstReturn(testAnswer("1234"))
                .thenReturn(testAnswer("5432"))
                .thenThrow(new IllegalStateException("Should not be called third time!")));
        List<HakuDetailsDto> results = optionsResource.listHakus(null);
        assertEquals(1, results.size());
        assertEquals("1234", results.get(0).getOid());
        results = optionsResource.listHakus(false);
        assertEquals(1, results.size());
        assertEquals("1234", results.get(0).getOid());
        results = optionsResource.listHakus(true);
        assertEquals(1, results.size());
        assertEquals("5432", results.get(0).getOid());
        optionsResource.listHakus(false);
    }
}
