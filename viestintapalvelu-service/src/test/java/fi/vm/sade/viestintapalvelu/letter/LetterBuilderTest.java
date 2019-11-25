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
package fi.vm.sade.viestintapalvelu.letter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.UsedTemplate;
import fi.vm.sade.viestintapalvelu.model.types.ContentTypes;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.MergedPdfDocument;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;
import fi.vm.sade.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.template.TemplateService;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;

import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LetterBuilderTest {

    private LetterBuilder builder;

    @Mock
    private DocumentBuilder documentBuilder;

    @Mock
    private TemplateService templateService;

    @Before
    public void init() throws Exception {
        builder = new LetterBuilder(documentBuilder, templateService, null, new ObjectMapperProvider());
    }

    @Test
    public void storesHtmlAsBytesIntoLetterReceiverLetter() throws Exception {
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(1l);

        final UsedTemplate usedTemplate = new UsedTemplate();
        batch.setUsedTemplates(Collections.singleton(usedTemplate));
        final Template template = new Template();
        usedTemplate.setTemplate(template);
        template.setId(1234L);
        template.setLanguage("FI");

        LetterReceivers receiver = batch.getLetterReceivers().iterator().next();
        receiver.getLetterReceiverLetter().setContentType(ContentTypes.CONTENT_TYPE_HTML);
        receiver.getLetterReceiverLetter().setOriginalContentType(ContentTypes.CONTENT_TYPE_HTML);
        when(templateService.findById(any(Long.class), any(ContentStructureType.class)))
                .thenReturn(DocumentProviderTestData.getTemplateWithType(ContentStructureType.accessibleHtml));
        byte[] htmlLetter = "html-sivun teksti".getBytes();
        when(documentBuilder.applyTextTemplate(any(byte[].class), anyMapOf(String.class, Object.class))).thenReturn(htmlLetter);

        final HashMap<String, Object> letterReplacements = new HashMap<>();
        letterReplacements.put("syntymaaika", "1998-14-12");
        builder.constructPagesForLetterReceiverLetter(receiver, batch, new HashMap<String, Object>(), letterReplacements);

        assertEquals(new String(htmlLetter), new String(receiver.getLetterReceiverLetter().getLetter()));
        assertEquals(ContentTypes.CONTENT_TYPE_HTML, receiver.getLetterReceiverLetter().getContentType());
    }

    @Test
    public void storesPDFAsBytesIntoLetterReceiverLetter() throws Exception {
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(1l);
        LetterReceivers receiver = batch.getLetterReceivers().iterator().next();
        when(templateService.findById(any(Long.class), any(ContentStructureType.class)))
                .thenReturn(DocumentProviderTestData.getTemplateWithType(ContentStructureType.letter));
        when(documentBuilder.merge(any(PdfDocument.class))).thenReturn(new MergedPdfDocument());
        builder.constructPagesForLetterReceiverLetter(receiver, batch, new HashMap<String, Object>(), new HashMap<String, Object>());
        assertNotNull(receiver.getLetterReceiverLetter().getLetter());
    }
}
