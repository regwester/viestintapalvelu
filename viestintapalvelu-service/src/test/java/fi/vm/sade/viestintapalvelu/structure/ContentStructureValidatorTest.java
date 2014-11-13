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

package fi.vm.sade.viestintapalvelu.structure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fi.vm.sade.viestintapalvelu.model.types.ContentRole;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.model.types.ContentType;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureContentSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.constraint.ContentStructureValidator;

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
        assertFalse(isValid(structure(ContentStructureType.letter)));
        assertTrue( isValid(structure(ContentStructureType.letter,
                content(ContentRole.body, ContentType.html)
        )));
        assertFalse(isValid(structure(ContentStructureType.letter,
                content(ContentRole.body, ContentType.plain)
        )));
        assertFalse(isValid(structure(ContentStructureType.letter,
                content(ContentRole.sms, ContentType.plain)
        )));
        assertFalse( isValid(structure(ContentStructureType.letter,
                content(ContentRole.attachment, ContentType.html)
        )));
        assertFalse( isValid(structure(ContentStructureType.letter,
                content(ContentRole.body, ContentType.html),
                content(ContentRole.attachment, ContentType.html)
        )));
        assertTrue( isValid(structure(ContentStructureType.letter,
                content(ContentRole.body, ContentType.html),
                content(ContentRole.body, ContentType.html)
        )));
    }

    @Test
    public void testEmailStructureValidation() {
        assertFalse(isValid(structure(ContentStructureType.email)));
        assertFalse(isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain))));
        assertFalse(isValid(structure(ContentStructureType.email,
                content(ContentRole.body, ContentType.html))));
        assertFalse( isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.html),
                content(ContentRole.body, ContentType.html)
        )));
        assertFalse( isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.html)
        )));
        assertTrue( isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.html)
        )));
        assertFalse(isValid(structure(ContentStructureType.email,
                content(ContentRole.body, ContentType.plain)
        )));
        assertFalse(isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.plain)
        )));
        assertTrue(isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.html),
                content(ContentRole.body, ContentType.plain)
        )));
        assertFalse(isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.html),
                content(ContentRole.body, ContentType.html),
                content(ContentRole.body, ContentType.plain)
        )));
        assertFalse(isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.html),
                content(ContentRole.body, ContentType.plain),
                content(ContentRole.body, ContentType.plain)
        )));
        assertTrue( isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.html),
                content(ContentRole.attachment, ContentType.html)
        )));
        assertFalse( isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.html),
                content(ContentRole.attachment, ContentType.plain)
        )));
        assertTrue( isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.html),
                content(ContentRole.body, ContentType.plain),
                content(ContentRole.attachment, ContentType.html),
                content(ContentRole.attachment, ContentType.html)
        )));
        assertFalse(isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.html),
                content(ContentRole.sms, ContentType.plain)
        )));
        assertFalse( isValid(structure(ContentStructureType.email,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.attachment, ContentType.html)
        )));
    }

    @Test
    public void testAsiointitiliStructureValidation() {
        assertFalse(isValid(structure(ContentStructureType.asiointitili)));
        assertFalse(isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.header, ContentType.plain))));
        assertFalse(isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.body, ContentType.plain))));
        assertTrue( isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.plain)
        )));
        assertFalse( isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.plain)
        )));
        assertFalse( isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.header, ContentType.html),
                content(ContentRole.body, ContentType.plain)
        )));
        assertFalse(isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.html)
        )));
        assertFalse(isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.sms, ContentType.plain)
        )));
        assertTrue( isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.plain),
                content(ContentRole.attachment, ContentType.html)
        )));
        assertTrue( isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.plain),
                content(ContentRole.sms, ContentType.plain)
        )));
        assertFalse(isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.plain),
                content(ContentRole.sms, ContentType.html)
        )));
        assertTrue( isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.plain),
                content(ContentRole.attachment, ContentType.html),
                content(ContentRole.attachment, ContentType.html)
        )));
        assertTrue( isValid(structure(ContentStructureType.asiointitili,
                content(ContentRole.header, ContentType.plain),
                content(ContentRole.body, ContentType.plain),
                content(ContentRole.attachment, ContentType.html),
                content(ContentRole.attachment, ContentType.html),
                content(ContentRole.sms, ContentType.plain)
        )));
    }

    private boolean isValid(ContentStructureSaveDto dto) {
        return validator.isValid(dto, null);
    }

    private ContentStructureSaveDto structure(ContentStructureType type, ContentStructureContentSaveDto... contents) {
        ContentStructureSaveDto structure = new ContentStructureSaveDto();
        structure.setType(type);
        for (ContentStructureContentSaveDto content : contents) {
            structure.getContents().add(content);
        }
        return structure;
    }

    private ContentStructureContentSaveDto content(ContentRole role, ContentType type) {
        return new ContentStructureContentSaveDto(role, "name", type,
                type == ContentType.html ? "<html><head><title>T</title></head><body>B</body></html>"
                : "content");
    }
}
