/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.ryhmasahkoposti.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.Message;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.core.Response;

import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import fi.vm.sade.ryhmasahkoposti.api.dto.*;
import fi.vm.sade.ryhmasahkoposti.config.IntegrationTestConfig;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.AttachmentResource;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.TemplateResource;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.AttachmentComponent;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.TemplateComponent;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;
import junit.framework.Assert;
import junit.framework.TestCase;

import static fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: ratamaa
 * Date: 25.9.2014
 * Time: 10:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestConfig.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class EmailResourceIT {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EmailResourceImpl emailResource;

    @Autowired
    private TemplateComponent templateComponent;

    @Autowired
    private AttachmentComponent attachmentComponent;

    @Autowired
    private IntegrationTestConfig.MailerStatus mailerStatus;

    private TemplateResource templateClient;
    private AttachmentResource attachmentResource;

    @Before
    public void setup() {
        templateClient = mock(TemplateResource.class);
        templateComponent.setTemplateResourceClient(templateClient);

        attachmentResource = mock(AttachmentResource.class);
        attachmentComponent.setAttachmentResource(attachmentResource);
    }

    @Test
    public void testEmailSendingWithReceiverSepcificDownloadedAttachments() throws Exception {
        TemplateDTO template = with(with(assumeTestTemplate("Testitemplate", "FI"),
                content("Testitemplate",
                        "<html><head><title>$title</title><style type=\"text/css\">$tyylit</style></head>" +
                        "<body><p>Hei $etunimi,</p>$sisalto <ul>#foreach ($v in $lista)<li>$v</li>#end</ul> $loppuosa</body></html>")
            ),
            replacement("loppuosa", "<p>Terveisin Lähettäjä</p>"),
            replacement("title", "Otsikko"),
            replacement("etunimi", "")
        );

        EmailData emailData = new EmailData();
        emailData.getEmail().setSubject("Varsinainen otsikko");
        emailData.getEmail().setBody("<p>Varsinainen viestin ssisältö</p>");
        emailData.getEmail().setCharset("UTF-8");
        emailData.getEmail().setHtml(true);
        emailData.getEmail().setTemplateName("Testitemplate");
        emailData.getEmail().setLanguageCode("FI");
        emailData.getEmail().setFrom("varsinainen_from@example.com");
        emailData.getEmail().setReplyTo("varsinainen_replyto@example.com");
        emailData.getEmail().setCallingProcess("prosessi");
        emailData.getEmail().setSenderOid("senderUserOid.123456.78990");
        emailData.getEmail().setOrganizationOid("senderOrganizationOid.123456.7890");
        emailData.getEmail().setSourceRegister(Arrays.asList(new SourceRegister("opintopolku")));
        emailData.setSenderOid("senderUserOid.123456.78990");

        EmailRecipient recipient = new EmailRecipient();
        recipient.setEmail("varsinainen_vastaanottaja@example.com");
        recipient.setLanguageCode("FI");
        recipient.setName("Milla Mallikas");
        recipient.setOid("vastaanottajaHenkiloOid.123456.7890");
        recipient.setOidType("virkailija");
        recipient.setRecipientReplacements(new ArrayList<ReportedRecipientReplacementDTO>());
        recipient.getRecipientReplacements().add(new ReportedRecipientReplacementDTO("etunimi", "Milla"));
        recipient.getRecipientReplacements().add(new ReportedRecipientReplacementDTO("lista", Arrays.asList(1,2,3)));
        emailData.getRecipient().add(recipient);

        long start = System.currentTimeMillis();
        Response response = emailResource.sendEmail(emailData);
        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof EmailSendId);
        EmailSendId id = (EmailSendId)response.getEntity();
        assertNotNull(id.getId());
        // Wait for the queue to be handled:
        while (isSending(id.getId())) {
            Thread.sleep(10l);
        }

        long duration = System.currentTimeMillis()-start;
        logger.info("Duration: " + duration + " ms.");
        assertTrue(duration < 1000l);

        // actually outgoing message at the javax.mail.Transport static send method:
        assertEquals(1, mailerStatus.getMessages().size());
        Message message = mailerStatus.getMessages().get(0);
        assertEquals("Varsinainen otsikko", message.getSubject());
        assertTrue(message.getContent() instanceof MimeMultipart);
        MimeMultipart multipart = (MimeMultipart)message.getContent();
        MimeBodyPart bodyPart = (MimeBodyPart) multipart.getBodyPart(0);
        String content = bodyPart.getContent().toString();
        assertEquals("<html><head><title>Otsikko</title>" +
                "<style type=\"text/css\">body {padding:10px;}</style></head>" +
                "<body><p>Hei Milla,</p><p>Varsinainen viestin ssis&auml;lt&ouml;</p> " +
                "<ul></ul> " +
                // TODO: Should be:
                //"<ul><li>1</li><li>2</li><li>3</li></ul> " +
                "<p>Terveisin L&auml;hett&auml;j&auml;</p></body></html>", content);
        logger.info("Message: " + content);
    }

    private boolean isSending(String sendId) {
        Response response = emailResource.getStatus(sendId);
        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof SendingStatusDTO);
        SendingStatusDTO status = (SendingStatusDTO)response.getEntity();
        return status.getSendingEnded() == null;
    }

    private TemplateDTO assumeTestTemplate(String templateName, String languageCode) throws IOException, DocumentException {
        TemplateDTO template = RaportointipalveluTestData.template(templateName, languageCode);
        when(templateClient.getTemplateContent(eq(templateName), eq(languageCode), eq(TemplateDTO.TYPE_EMAIL)))
                .thenReturn(template);
        return template;
    }
}
