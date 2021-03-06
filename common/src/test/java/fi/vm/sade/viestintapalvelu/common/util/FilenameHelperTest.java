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

package fi.vm.sade.viestintapalvelu.common.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class FilenameHelperTest {
    @Test
    public void testWithoutExtension() throws Exception {
        assertEquals("a/b/c123456-abacs7897", FilenameHelper.withoutExtension("a/b/c123456-abacs7897"));
        assertEquals("a/b/c123456-abacs7897", FilenameHelper.withoutExtension("a/b/c123456-abacs7897.pdf"));
        assertEquals("a.b/c.d", FilenameHelper.withoutExtension("a.b/c.d.zip"));
    }
}