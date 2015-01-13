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
package fi.vm.sade.viestintapalvelu.template;

import java.util.List;

import org.junit.Test;

import fi.vm.sade.viestintapalvelu.model.types.ContentRole;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.model.types.ContentType;
import fi.vm.sade.viestintapalvelu.structure.dto.constraint.ContentStructurePredicate;
import fi.vm.sade.viestintapalvelu.template.impl.StructureConverterImpl;

import static fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData.content;
import static fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData.contentStructure;
import static org.junit.Assert.assertEquals;

public class StructureConverterImplTest {
    private StructureConverter structureConverter = new StructureConverterImpl();

    @Test
    public void testToContentsLetter() throws Exception {
        List<TemplateContent> contents = structureConverter
                .toContents(contentStructure(ContentStructureType.letter,
                        content(ContentRole.body, ContentType.html),
                        content(ContentRole.body, ContentType.html)));
        assertEquals(2, contents.size());
        assertEquals("letter_content", contents.get(0).getName());
    }

    @Test
    public void testToContentsEmail() throws Exception {
        List<TemplateContent> contents = structureConverter
                .toContents(contentStructure(ContentStructureType.email,
                        content(ContentRole.header, ContentType.plain),
                        content(ContentRole.body, ContentType.html),
                        content(ContentRole.body, ContentType.plain),
                        content(ContentRole.attachment, ContentType.html),
                        content(ContentRole.attachment, ContentType.html)
                ));
        assertEquals(4, contents.size());
        assertEquals("email_subject", contents.get(0).getName());
        assertEquals("email_body", contents.get(1).getName());
        assertEquals("liite", contents.get(2).getName());
        assertEquals("liite", contents.get(3).getName());
    }

    @Test
    public void testToContentsFiltered() throws Exception {
        List<TemplateContent> contents = structureConverter
                .toContents(contentStructure(ContentStructureType.email,
                        content(ContentRole.header, ContentType.plain),
                        content(ContentRole.body, ContentType.html),
                        content(ContentRole.body, ContentType.plain),
                        content(ContentRole.attachment, ContentType.html),
                        content(ContentRole.attachment, ContentType.html)
                ), new ContentStructurePredicate(ContentRole.attachment).max(1));
        assertEquals(1, contents.size());
        assertEquals("liite", contents.get(0).getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalContents() throws Exception {
        structureConverter.toContents(contentStructure(ContentStructureType.email,
                content(ContentRole.body, ContentType.html)
        ));
    }
}