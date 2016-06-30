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
package fi.vm.sade.ryhmasahkoposti.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedRecipientReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateContentDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateDTO;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TemplateBuilder.class)
@ContextConfiguration("/test-bundle-context.xml")
public class TemplateBuilderTest {
    private EmailData emailData;

    @Before
    public void setup() {
        this.emailData = RaportointipalveluTestData.getEmailData();
    }

    @Test
    public void testTemplateBuilder() throws IOException {

        TemplateDTO templateDTO = new TemplateDTO();
        TemplateContentDTO content = new TemplateContentDTO();
        content.setContent("test-content");
        content.setContentType("text/plain");

        Set<TemplateContentDTO> contents = new HashSet<TemplateContentDTO>();
        contents.add(content);

        templateDTO.setContents(contents);

        TemplateBuilder builder = new TemplateBuilder();

        String template = builder.buildTemplate(templateDTO, emailData);

        assertNotNull(template);
        assertEquals(template, content.getContent());
    }

    @Test
    public void testTemplateMessageBuilder1() throws IOException {

        TemplateDTO templateDTO = new TemplateDTO();
        TemplateContentDTO content = new TemplateContentDTO();
        content.setContent("test-content- $key-1 - $key-2 - $key-3 - $letterDate");
        content.setContentType("text/plain");

        Set<TemplateContentDTO> contents = new HashSet<TemplateContentDTO>();
        contents.add(content);

        templateDTO.setContents(contents);

        TemplateBuilder builder = new TemplateBuilder();
        String message = builder.buildTemplate(templateDTO, emailData);

        List<ReplacementDTO> messageReplacements = RaportointipalveluTestData.getEmailReplacements(1, 2, 3);
        List<ReportedRecipientReplacementDTO> recipientReplacements = RaportointipalveluTestData.getReportedReceientReplacements(2, 3);

        String buildMessage = builder.buildTempleMessage(message, messageReplacements, recipientReplacements);

        assertNotNull(buildMessage);
        assertEquals(buildMessage, "test-content- email-1 - recipient-2 - recipient-3 - " + new SimpleDateFormat("d.M.yyyy").format(new Date()));

    }

    @Test
    public void testTemplateMessageBuilder2() throws IOException {

        TemplateDTO templateDTO = new TemplateDTO();
        TemplateContentDTO content = new TemplateContentDTO();
        content.setContent("test-content- $key-1 - $key-2 - $key-3 - $letterDate");
        content.setContentType("text/plain");

        Set<TemplateContentDTO> contents = new HashSet<TemplateContentDTO>();
        contents.add(content);

        templateDTO.setContents(contents);

        TemplateBuilder builder = new TemplateBuilder();
        String message = builder.buildTemplate(templateDTO, emailData);

        List<ReplacementDTO> messageReplacements = RaportointipalveluTestData.getEmailReplacements(1, 2, 3);
        List<ReportedRecipientReplacementDTO> recipientReplacements = RaportointipalveluTestData.getReportedReceientReplacements(4, 5, 6);

        String buildMessage = builder.buildTempleMessage(message, messageReplacements, recipientReplacements);

        assertNotNull(buildMessage);
        assertEquals(buildMessage, "test-content- email-1 - email-2 - email-3 - " + new SimpleDateFormat("d.M.yyyy").format(new Date()));

    }

    @Test
    public void testTemplateMessageBuilder3() throws IOException {

        TemplateDTO templateDTO = new TemplateDTO();
        TemplateContentDTO content = new TemplateContentDTO();
        content.setContent("test-content- $key-1 - $key-2 - $key-3 - $letterDate");
        content.setContentType("text/plain");

        Set<TemplateContentDTO> contents = new HashSet<TemplateContentDTO>();
        contents.add(content);

        templateDTO.setContents(contents);

        TemplateBuilder builder = new TemplateBuilder();
        String message = builder.buildTemplate(templateDTO, emailData);

        List<ReplacementDTO> messageReplacements = RaportointipalveluTestData.getEmailReplacements(1, 2, 3);
        List<ReportedRecipientReplacementDTO> recipientReplacements = RaportointipalveluTestData.getReportedReceientReplacements(1, 2, 3);

        String buildMessage = builder.buildTempleMessage(message, messageReplacements, recipientReplacements);

        assertNotNull(buildMessage);
        assertEquals(buildMessage, "test-content- recipient-1 - recipient-2 - recipient-3 - " + new SimpleDateFormat("d.M.yyyy").format(new Date()));

    }

    @Test
    public void testTemplateMessageBuilder4() throws IOException {

        TemplateDTO templateDTO = new TemplateDTO();
        TemplateContentDTO content = new TemplateContentDTO();
        content.setContent("test-content- $key-1 - $key-2 - $key-3 - $letterDate");
        content.setContentType("text/plain");

        Set<TemplateContentDTO> contents = new HashSet<TemplateContentDTO>();
        contents.add(content);

        templateDTO.setContents(contents);

        TemplateBuilder builder = new TemplateBuilder();
        String message = builder.buildTemplate(templateDTO, emailData);

        List<ReplacementDTO> messageReplacements = RaportointipalveluTestData.getEmailReplacements(1, 2, 3);
        List<ReportedRecipientReplacementDTO> recipientReplacements = null;

        String buildMessage = builder.buildTempleMessage(message, messageReplacements, recipientReplacements);

        assertNotNull(buildMessage);
        assertEquals(buildMessage, "test-content- email-1 - email-2 - email-3 - " + new SimpleDateFormat("d.M.yyyy").format(new Date()));

    }

    @Test
    public void testTemplateMessageBuilder5() throws IOException {

        TemplateDTO templateDTO = new TemplateDTO();
        TemplateContentDTO content = new TemplateContentDTO();
        content.setContent("test-content- $key-1 - $key-2 - $key-3 - $letterDate");
        content.setContentType("text/plain");

        Set<TemplateContentDTO> contents = new HashSet<TemplateContentDTO>();
        contents.add(content);

        templateDTO.setContents(contents);

        TemplateBuilder builder = new TemplateBuilder();
        String message = builder.buildTemplate(templateDTO, emailData);

        List<ReplacementDTO> messageReplacements = null;
        List<ReportedRecipientReplacementDTO> recipientReplacements = RaportointipalveluTestData.getReportedReceientReplacements(1, 2, 3);

        String buildMessage = builder.buildTempleMessage(message, messageReplacements, recipientReplacements);

        assertNotNull(buildMessage);
        assertEquals(buildMessage, "test-content- recipient-1 - recipient-2 - recipient-3 - " + new SimpleDateFormat("d.M.yyyy").format(new Date()));

    }

    @Test
    public void testTemplateMessageBuilder6() throws IOException {

        TemplateDTO templateDTO = new TemplateDTO();
        TemplateContentDTO content = new TemplateContentDTO();
        content.setContent("test-content- $letterDate");
        content.setContentType("text/plain");

        Set<TemplateContentDTO> contents = new HashSet<TemplateContentDTO>();
        contents.add(content);

        templateDTO.setContents(contents);

        TemplateBuilder builder = new TemplateBuilder();
        String message = builder.buildTemplate(templateDTO, emailData);

        List<ReplacementDTO> messageReplacements = null;
        List<ReportedRecipientReplacementDTO> recipientReplacements = null;

        String buildMessage = builder.buildTempleMessage(message, messageReplacements, recipientReplacements);

        assertNotNull(buildMessage);
        assertEquals(buildMessage, "test-content- " + new SimpleDateFormat("d.M.yyyy").format(new Date()));

    }

    @Test
    public void testTemplateMessageBuilderWithCustomValue() throws IOException {
        TemplateBuilder builder = new TemplateBuilder();
        String message = "test-content- $letterDate <ul>#foreach( $v in $list )<li>$v</li>#end</ul>";

        List<ReportedRecipientReplacementDTO> recipientReplacements = new ArrayList<ReportedRecipientReplacementDTO>();
        recipientReplacements.add(new ReportedRecipientReplacementDTO("list", Arrays.asList("a", "b", "c")));
        List<ReplacementDTO> messageReplacements = null;

        String buildMessage = builder.buildTempleMessage(message, messageReplacements, recipientReplacements);

        assertNotNull(buildMessage);
        assertEquals("test-content- " + new SimpleDateFormat("d.M.yyyy").format(new Date()) + " <ul><li>a</li><li>b</li><li>c</li></ul>", buildMessage);
    }

}
