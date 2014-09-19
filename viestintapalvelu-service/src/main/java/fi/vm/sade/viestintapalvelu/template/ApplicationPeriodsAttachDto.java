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

package fi.vm.sade.viestintapalvelu.template;

import java.io.Serializable;
import java.util.List;

/**
 * User: ratamaa
 * Date: 9.9.2014
 * Time: 16:08
 */
public class ApplicationPeriodsAttachDto implements Serializable {
    private Long templateId;
    private List<String> applicationPeriods;
    private boolean useAsDefault=false;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public List<String> getApplicationPeriods() {
        return applicationPeriods;
    }

    public void setApplicationPeriods(List<String> applicationPeriods) {
        this.applicationPeriods = applicationPeriods;
    }

    public boolean isUseAsDefault() {
        return useAsDefault;
    }

    public void setUseAsDefault(boolean useAsDefault) {
        this.useAsDefault = useAsDefault;
    }
}
