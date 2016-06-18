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
package fi.vm.sade.ryhmasahkoposti.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fi.vm.sade.ryhmasahkoposti.api.dto.OrganizationDTO;
import fi.vm.sade.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessagesDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.resource.MessageReportingResource;
import fi.vm.sade.converter.PagingAndSortingDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedMessageQueryDTOConverter;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class })
public class MessageReportingResourceTest {
    @Mock
    private GroupEmailReportingService mockedGroupEmailReportingService;
    @Mock
    private ReportedMessageQueryDTOConverter mockedReportedMessageQueryDTOConverter;
    @Mock
    private PagingAndSortingDTOConverter mockedPagingAndSortingDTOConverter;
    private MessageReportingResource messageReportingResource;

    @Before
    public void setup() {
        this.messageReportingResource = new MessageReportingResourceImpl(mockedGroupEmailReportingService, mockedReportedMessageQueryDTOConverter,
                mockedPagingAndSortingDTOConverter);
    }

    @Test
    public void testGetReportedMessagesIsSuccessful() throws Exception {
        PagingAndSortingDTO mockedPagingAndSortingDTO = RaportointipalveluTestData.getPagingAndSortingDTO();
        when(mockedPagingAndSortingDTOConverter.convert(any(Integer.class), any(Integer.class), any(String.class), any(String.class))).thenReturn(
                mockedPagingAndSortingDTO);

        List<OrganizationDTO> organizations = new ArrayList<OrganizationDTO>();
        organizations.add(RaportointipalveluTestData.getOrganizationDTO());
        when(mockedGroupEmailReportingService.getUserOrganizations()).thenReturn(organizations);

        ReportedMessagesDTO mockedReportedMessagesDTO = RaportointipalveluTestData.getReportedMessagesDTO();
        when(mockedGroupEmailReportingService.getReportedMessagesByOrganizationOid("1.2.246.562.10.00000000001", mockedPagingAndSortingDTO)).thenReturn(
                mockedReportedMessagesDTO);

        Response response = messageReportingResource.getReportedMessages("1.2.246.562.10.00000000001", 10, 1, "sendingStarted", "asc");
        ReportedMessagesDTO searchedReportedMessagesDTO = (ReportedMessagesDTO) response.getEntity();

        assertNotNull(searchedReportedMessagesDTO);
        assertNotNull(searchedReportedMessagesDTO.getReportedMessages());
        assertTrue(searchedReportedMessagesDTO.getReportedMessages().size() == 1);
        assertTrue(searchedReportedMessagesDTO.getNumberOfReportedMessages() == 1);
        assertNotNull(searchedReportedMessagesDTO.getOrganizations());
        assertTrue(searchedReportedMessagesDTO.getOrganizations().size() == 1);
    }

    @Test
    public void testGetReportedMessagesWithSearchArgumentIsSuccessful() throws Exception {
        PagingAndSortingDTO mockedPagingAndSortingDTO = RaportointipalveluTestData.getPagingAndSortingDTO();
        when(mockedPagingAndSortingDTOConverter.convert(any(Integer.class), any(Integer.class), any(String.class), any(String.class))).thenReturn(
                mockedPagingAndSortingDTO);

        List<OrganizationDTO> organizations = new ArrayList<OrganizationDTO>();
        organizations.add(RaportointipalveluTestData.getOrganizationDTO());
        when(mockedGroupEmailReportingService.getUserOrganizations()).thenReturn(organizations);

        ReportedMessageQueryDTO mockedReportedMessageQueryDTO = RaportointipalveluTestData.getReportedMessageQueryDTO();
        when(mockedReportedMessageQueryDTOConverter.convert(any(String.class), any(String.class))).thenReturn(mockedReportedMessageQueryDTO);

        ReportedMessagesDTO mockedReportedMessagesDTO = RaportointipalveluTestData.getReportedMessagesDTO();
        when(mockedGroupEmailReportingService.getReportedMessages(mockedReportedMessageQueryDTO, mockedPagingAndSortingDTO)).thenReturn(
                mockedReportedMessagesDTO);

        Response response = messageReportingResource.getReportedMessages("1.2.246.562.10.00000000001", "testi.vastaanottaja@sposti.fi", 10, 1,
                "sendingStarted", "asc");
        ReportedMessagesDTO searchedReportedMessagesDTO = (ReportedMessagesDTO) response.getEntity();

        assertNotNull(searchedReportedMessagesDTO);
        assertNotNull(searchedReportedMessagesDTO.getReportedMessages());
        assertTrue(searchedReportedMessagesDTO.getReportedMessages().size() == 1);
        assertTrue(searchedReportedMessagesDTO.getNumberOfReportedMessages() == 1);
        assertNotNull(searchedReportedMessagesDTO.getOrganizations());
        assertTrue(searchedReportedMessagesDTO.getOrganizations().size() == 1);
    }
}
