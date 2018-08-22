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
package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateDTO;
import org.dom4j.DocumentException;

import java.io.IOException;

public interface TemplateResource {
    TemplateDTO getTemplateContent(String templateName, String languageCode, String type, String applicationPeriod) throws IOException, DocumentException;
    TemplateDTO getTemplateContent(String templateName, String languageCode, String type) throws IOException, DocumentException;
    TemplateDTO getTemplateByID(String templateId, String type) throws IOException;
}
