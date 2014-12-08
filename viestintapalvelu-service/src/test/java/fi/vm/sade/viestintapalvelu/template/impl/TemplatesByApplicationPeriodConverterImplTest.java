package fi.vm.sade.viestintapalvelu.template.impl;

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.Lists;

import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.template.TemplatesByApplicationPeriod;
import fi.vm.sade.viestintapalvelu.template.TemplatesByApplicationPeriod.TemplateInfo;
import fi.vm.sade.viestintapalvelu.template.TemplatesByApplicationPeriodConverter;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import static org.junit.Assert.assertTrue;


public class TemplatesByApplicationPeriodConverterImplTest {
    
    private TemplatesByApplicationPeriodConverter converter = new TemplatesByApplicationPeriodConverterImpl();
    
    @Test
    public void convertsTemplateToTemplateInfo() {
        assertTrue(converter.convert(DocumentProviderTestData.getTemplate(1l)) instanceof TemplateInfo);
    }
    
    @Test
    public void convertsToTemplateByApplicationPeriod() {
        assertTrue(converter.convert("1.9.234.220", Lists.newArrayList(DocumentProviderTestData.getTemplate(1l)), new ArrayList<Template>(), new ArrayList<Template>()) instanceof TemplatesByApplicationPeriod);
    }

}
