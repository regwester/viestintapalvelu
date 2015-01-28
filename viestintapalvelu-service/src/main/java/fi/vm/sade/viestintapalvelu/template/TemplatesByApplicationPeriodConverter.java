/**
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
package fi.vm.sade.viestintapalvelu.template;

import java.util.List;

import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateInfo;

/**
 * @author risal1
 *
 */
public interface TemplatesByApplicationPeriodConverter {

    TemplatesByApplicationPeriod convert(String applicationPeriod, List<Template> publisheds, List<Template> drafts, List<Template> closeds);

    TemplateInfo convert(Template template);

    List<TemplateInfo> convert(List<Template> templates);
}
