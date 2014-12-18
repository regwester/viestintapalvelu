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

import java.io.Serializable;
import java.util.Map;

/**
 * User: ratamaa
 * Date: 23.10.2014
 * Time: 15:36
 */
public class EmailReceiver implements Serializable {
    private static final long serialVersionUID = 3805640040013407269L;

    private String oidType;
    private String oid;
    private String email;
    private Map<String,Object> replacements;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOidType() {
        return oidType;
    }

    public void setOidType(String oidType) {
        this.oidType = oidType;
    }

    public Map<String, Object> getReplacements() {
        return replacements;
    }

    public void setReplacements(Map<String, Object> replacements) {
        this.replacements = replacements;
    }
}
