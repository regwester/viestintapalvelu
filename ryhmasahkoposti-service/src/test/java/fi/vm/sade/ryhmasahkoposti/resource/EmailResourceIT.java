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
import java.util.*;

import javax.ws.rs.core.Response;

import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import fi.vm.sade.ryhmasahkoposti.api.dto.*;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.TemplateResource;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.TemplateComponent;

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
@Ignore // WIP
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EmailResourceIT.Config.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class EmailResourceIT {
    @Autowired
    private EmailResourceImpl emailResource;

    @Autowired
    private TemplateComponent templateComponent;

    @Autowired
    private TemplateResource templateClient;

    @Before
    public void setup() {
        templateClient = mock(TemplateResource.class);
        templateComponent.setTemplateResourceClient(templateClient);
    }

    @Test
    public void testEmailSendingWithReceiverSepcificDownloadedAttachments() throws Exception {
        TemplateDTO template = with(with(assumeTestTemplate("Testitemplate", "FI"),
                        content("Testitemplate",
                                "<html><head><title>$otsikko</title><style type=\"text/css\">$tyylit</style></head>" +
                                        "<body><p>Hei $etunimi,</p>$sisalto $loppuosa</body></html>")
                ),
                replacement("loppuosa", "<p>Terveisin Lähettäjä</p>")
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
        recipient.getRecipientReplacements().add(new ReportedRecipientReplacementDTO("etunimi", "Milla"));
        emailData.getRecipient().add(recipient);

        Response response = emailResource.sendEmail(emailData);
        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof EmailSendId);
        EmailSendId id = (EmailSendId)response.getEntity();
        assertNotNull(id.getId());
        // Wait for the queue to be handled:
        // WIP
        // TODO ...
    }

    private TemplateDTO assumeTestTemplate(String templateName, String languageCode) throws IOException, DocumentException {
        TemplateDTO template = template(templateName, languageCode);
        when(templateClient.getTemplateContent(eq(templateName), eq(languageCode), eq(TemplateDTO.TYPE_EMAIL)))
                .thenReturn(template);
        return template;
    }

    private static TemplateDTO template(String templateName, String languageCode) throws IOException, DocumentException {
        TemplateDTO template = new TemplateDTO();
        template.setName(templateName);
        template.setLanguage(languageCode);
        template.setVersionro("0");
        template.setOrganizationOid("123.456.789");
        template.setStoringOid("123");
        template.setType("email");
        template.setStyles("body {padding:10px;}");

        Set<ReplacementDTO> replacements = new HashSet<ReplacementDTO>();

        template.setReplacements(replacements);
        return template;
    }

    public static TemplateDTO with(TemplateDTO template, TemplateContentDTO... contents) {
        Set<TemplateContentDTO> templateContents = new HashSet<TemplateContentDTO>();
        for (TemplateContentDTO content : contents) {
            templateContents.add(content);
        }
        template.setContents(templateContents);
        return template;
    }

    public static TemplateDTO with(TemplateDTO template, ReplacementDTO... replacements) {
        Set<ReplacementDTO> replacementsSet = new HashSet<ReplacementDTO>();
        for (ReplacementDTO replacement : replacements) {
            replacementsSet.add(replacement);
        }
        template.setReplacements(replacementsSet);
        return template;
    }

    public static TemplateContentDTO content(String name, String contentStr) {
        TemplateContentDTO content = new TemplateContentDTO();
        content.setName(name);
        content.setContentType("text/html");
        content.setOrder(1);
        content.setTimestamp(new Date());
        content.setContent(contentStr);
        return content;
    }

    public static ReplacementDTO replacement(String name, String defaultValue) {
        ReplacementDTO loppuOsa = new ReplacementDTO();
        loppuOsa.setName(name);
        loppuOsa.setDefaultValue(defaultValue);
        loppuOsa.setMandatory(true);
        return loppuOsa;
    }

    @Configuration
    @ImportResource(value = "classpath:test-bundle-context.xml")
    public static class Config {
    }

}
