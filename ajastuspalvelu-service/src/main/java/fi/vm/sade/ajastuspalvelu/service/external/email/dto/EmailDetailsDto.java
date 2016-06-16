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

package fi.vm.sade.ajastuspalvelu.service.external.email.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ratamaa
 * Date: 23.10.2014
 * Time: 15:34
 */
public class EmailDetailsDto {
    private String templateName;
    private String hakuOid;
    private Map<String,Object> replacements = new HashMap<>();
    private List<EmailReceiver> receivers = new ArrayList<>();

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateNamke) {
        this.templateName = templateNamke;
    }

    public String getHakuOid() {
        return hakuOid;
    }

    public void setHakuOid(String hakuOid) {
        this.hakuOid = hakuOid;
    }

    public Map<String, Object> getReplacements() {
        return replacements;
    }

    public void setReplacements(Map<String, Object> replacements) {
        this.replacements = replacements;
    }

    public List<EmailReceiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<EmailReceiver> receivers) {
        this.receivers = receivers;
    }
}
