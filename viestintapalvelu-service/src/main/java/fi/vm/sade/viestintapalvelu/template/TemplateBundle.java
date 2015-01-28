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
package fi.vm.sade.viestintapalvelu.template;

import java.util.List;

/**
 * @author migar1
 *
 */
public class TemplateBundle {
    private Template latestTemplate;
    private List<Replacement> latestOrganisationReplacements;
    private List<Replacement> latestOrganisationReplacementsWithTag;

    public TemplateBundle() {
    }

    public Template getLatestTemplate() {
        return latestTemplate;
    }

    public void setLatestTemplate(Template latestTemplate) {
        this.latestTemplate = latestTemplate;
    }

    public List<Replacement> getLatestOrganisationReplacements() {
        return latestOrganisationReplacements;
    }

    public void setLatestOrganisationReplacements(List<Replacement> latestOrganisationReplacements) {
        this.latestOrganisationReplacements = latestOrganisationReplacements;
    }

    public List<Replacement> getLatestOrganisationReplacementsWithTag() {
        return latestOrganisationReplacementsWithTag;
    }

    public void setLatestOrganisationReplacementsWithTag(List<Replacement> latestOrganisationReplacementsWithTag) {
        this.latestOrganisationReplacementsWithTag = latestOrganisationReplacementsWithTag;
    }

}
