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
package fi.vm.sade.viestintapalvelu.dao;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.model.TemplateApplicationPeriod;

import java.util.List;

public interface TemplateDAO extends JpaDAO<Template, Long> {

    /**
     * @param criteria
     * @return the first Template matching the criteria or null if not found
     */
    Template findTemplate(TemplateCriteria criteria);

    List<Template> findTemplates(TemplateCriteria templateCriteria);

    Template persist(Template templateHaku);

    TemplateApplicationPeriod insert(TemplateApplicationPeriod templateApplicationPeriod);

    void remove(TemplateApplicationPeriod templateApplicationPeriod);

    List<String> getAvailableTemplates();

    List<String> getAvailableTemplatesByType(Template.State state);

    Template findByIdAndState(Long id, State state);

    List<Template> findByOrganizationOIDs(List<String> oids);
}
