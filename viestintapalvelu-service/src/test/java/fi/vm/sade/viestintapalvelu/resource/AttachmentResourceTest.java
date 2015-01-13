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

import java.util.Arrays;

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

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.viestintapalvelu.attachment.dto.UrisContainerDto;
import fi.vm.sade.viestintapalvelu.attachment.impl.AttachmentResourceImpl;
import fi.vm.sade.viestintapalvelu.attachment.impl.AttachmentServiceImpl;
import fi.vm.sade.viestintapalvelu.attachment.impl.AttachmentUri;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterAttachmentDAO;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetterAttachment;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 18:29
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("/test-application-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class AttachmentResourceTest {
    @Mock
    private LetterReceiverLetterAttachmentDAO letterReceiverLetterAttachmentDAO;
    private AttachmentResourceImpl attachmentResource;
    private AttachmentServiceImpl attachmentService;

    @Before
    public void setup() {
        this.attachmentResource = new AttachmentResourceImpl();
        this.attachmentService = new AttachmentServiceImpl();
        this.attachmentService.setLetterReceiverLetterAttachmentDAO(this.letterReceiverLetterAttachmentDAO);
        this.attachmentResource.setAttachmentService(this.attachmentService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDownloadByUriInvalid() {
        this.attachmentResource.downloadByUri("foo://bar");
    }

    @Test
    public void testDownloadByUriNotFound() {
        assertNull(this.attachmentResource.downloadByUri(
                AttachmentUri.getLetterReceiverLetterAttachment(1l).toString()));
    }

    @Test
    public void testDownloadByUri() {
        LetterReceiverLetterAttachment attachment = DocumentProviderTestData.getLetterReceiverLetterAttachment(null);
        when(letterReceiverLetterAttachmentDAO.read(eq(1l))).thenReturn(attachment);

        EmailAttachment attachmentDto = attachmentResource.downloadByUri(AttachmentUri.getLetterReceiverLetterAttachment(1l).toString());
        assertNotNull(attachmentDto);
        assertEquals(attachment.getName(), attachmentDto.getName());
        assertEquals(attachment.getContentType(), attachmentDto.getContentType());
        assertEquals(attachment.getContents(), attachmentDto.getData());
    }

    @Test
    public void testDeleteByUri() {
        LetterReceiverLetterAttachment attachment = DocumentProviderTestData.getLetterReceiverLetterAttachment(null);
        when(letterReceiverLetterAttachmentDAO.read(eq(2l))).thenReturn(attachment);

        // Not found by still continued:
        attachmentResource.deleteByUris(new UrisContainerDto(Arrays.asList(
                AttachmentUri.getLetterReceiverLetterAttachment(1l).toString(),
                AttachmentUri.getLetterReceiverLetterAttachment(2l).toString()
        )));
    }
}
