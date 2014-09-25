package fi.vm.sade.ryhmasahkoposti.service;

import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateDTO;

public interface TemplateService {
    TemplateDTO  getTemplate(String templateName, String languageCode, String templateType, String applicationPeriod);
    TemplateDTO  getTemplate(String id);
}
