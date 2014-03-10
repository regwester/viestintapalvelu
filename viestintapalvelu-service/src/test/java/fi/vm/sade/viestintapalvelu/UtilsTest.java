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
