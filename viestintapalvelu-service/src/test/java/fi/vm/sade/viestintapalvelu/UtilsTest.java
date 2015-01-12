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
package fi.vm.sade.viestintapalvelu;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilsTest {
    @Test
    public void testResolveTemplateNameFI() throws Exception {
        assertEquals("FI", Utils.resolveTemplateName("{LANG}", "FI"));
    }

    @Test
    public void testResolveTemplateNameSV() throws Exception {
        assertEquals("SE", Utils.resolveTemplateName("{LANG}", "SE"));
    }

    @Test
    public void testResolveTemplateNameEN() throws Exception {
        assertEquals("EN", Utils.resolveTemplateName("{LANG}", "EN"));
    }

    @Test
    public void testResolveTemplateNameEMPTY() throws Exception {
        assertEquals("FI", Utils.resolveTemplateName("{LANG}", ""));
    }

    @Test
    public void testResolveTemplateNameNull() throws Exception {
        assertEquals("FI", Utils.resolveTemplateName("{LANG}", null));
    }

    @Test
    public void testResolveTemplateNameRO() throws Exception {
        assertEquals("EN", Utils.resolveTemplateName("{LANG}", "RO"));
    }
}
