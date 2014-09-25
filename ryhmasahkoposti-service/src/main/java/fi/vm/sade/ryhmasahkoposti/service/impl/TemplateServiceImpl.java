package fi.vm.sade.ryhmasahkoposti.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.TemplateComponent;
import fi.vm.sade.ryhmasahkoposti.service.TemplateService;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateComponent templateComponent;

    @Override
    public TemplateDTO getTemplate(String templateName, String languageCode, String templateType, String applicationPeriod) {
        return templateComponent.getTemplateContent(templateName, languageCode, templateType, applicationPeriod);
    }
    
    @Override
    public TemplateDTO getTemplate(String id) {
        if (id != null) {
            return templateComponent.getTemplateContent(id.toString());
        }
        return null;
    }

}
