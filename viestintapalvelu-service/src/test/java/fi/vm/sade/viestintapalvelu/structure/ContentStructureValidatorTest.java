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
package fi.vm.sade.viestintapalvelu.structure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fi.vm.sade.viestintapalvelu.model.types.ContentRole;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.model.types.ContentType;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.constraint.ContentStructureValidator;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

import static fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData.contentSaveDto;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 16:24
 */
@RunWith(JUnit4.class)
public class ContentStructureValidatorTest {
    private ContentStructureValidator validator = new ContentStructureValidator();

    @Test
    public void testLetterStructureValidation() {
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.letter)));
        assertTrue( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.letter,
                contentSaveDto(ContentRole.body, ContentType.html)
        )));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.letter,
                contentSaveDto(ContentRole.body, ContentType.plain)
        )));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.letter,
                contentSaveDto(ContentRole.sms, ContentType.plain)
        )));
        assertFalse( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.letter,
                contentSaveDto(ContentRole.attachment, ContentType.html)
        )));
        assertFalse( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.letter,
                contentSaveDto(ContentRole.body, ContentType.html),
                contentSaveDto(ContentRole.attachment, ContentType.html)
        )));
        assertTrue(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.letter,
                contentSaveDto(ContentRole.body, ContentType.html),
                contentSaveDto(ContentRole.body, ContentType.html)
        )));
    }

    @Test
    public void testEmailStructureValidation() {
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email)));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain))));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.body, ContentType.html))));
        assertFalse( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.html),
                contentSaveDto(ContentRole.body, ContentType.html)
        )));
        assertFalse( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.html)
        )));
        assertTrue( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.html)
        )));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.body, ContentType.plain)
        )));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.plain)
        )));
        assertTrue(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.html),
                contentSaveDto(ContentRole.body, ContentType.plain)
        )));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.html),
                contentSaveDto(ContentRole.body, ContentType.html),
                contentSaveDto(ContentRole.body, ContentType.plain)
        )));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.html),
                contentSaveDto(ContentRole.body, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.plain)
        )));
        assertTrue( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.html),
                contentSaveDto(ContentRole.attachment, ContentType.html)
        )));
        assertFalse( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.html),
                contentSaveDto(ContentRole.attachment, ContentType.plain)
        )));
        assertTrue( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.html),
                contentSaveDto(ContentRole.body, ContentType.plain),
                contentSaveDto(ContentRole.attachment, ContentType.html),
                contentSaveDto(ContentRole.attachment, ContentType.html)
        )));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.html),
                contentSaveDto(ContentRole.sms, ContentType.plain)
        )));
        assertFalse( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.email,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.attachment, ContentType.html)
        )));
    }

    @Test
    public void testAsiointitiliStructureValidation() {
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili)));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.header, ContentType.plain))));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.body, ContentType.plain))));
        assertTrue( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.plain)
        )));
        assertFalse( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.plain)
        )));
        assertFalse( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.header, ContentType.html),
                contentSaveDto(ContentRole.body, ContentType.plain)
        )));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.html)
        )));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.sms, ContentType.plain)
        )));
        assertTrue( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.plain),
                contentSaveDto(ContentRole.attachment, ContentType.html)
        )));
        assertTrue( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.plain),
                contentSaveDto(ContentRole.sms, ContentType.plain)
        )));
        assertFalse(isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.plain),
                contentSaveDto(ContentRole.sms, ContentType.html)
        )));
        assertTrue( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.plain),
                contentSaveDto(ContentRole.attachment, ContentType.html),
                contentSaveDto(ContentRole.attachment, ContentType.html)
        )));
        assertTrue( isValid(DocumentProviderTestData.contentStructureSaveDto(ContentStructureType.asiointitili,
                contentSaveDto(ContentRole.header, ContentType.plain),
                contentSaveDto(ContentRole.body, ContentType.plain),
                contentSaveDto(ContentRole.attachment, ContentType.html),
                contentSaveDto(ContentRole.attachment, ContentType.html),
                contentSaveDto(ContentRole.sms, ContentType.plain)
        )));
    }

    private boolean isValid(ContentStructureSaveDto dto) {
        return validator.isValid(dto, null);
    }

}
