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

package fi.vm.sade.viestintapalvelu.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 9.9.2014
 * Time: 10:42
 *
 * TemplateApplicationPeriod's multicolumn primary key.
 *
 * @see TemplateApplicationPeriod
 */
@Embeddable
public class TemplateApplicationPeriodId implements Serializable {
    private static final int HASH_FACTOR = 31;
    @ApiModelProperty(value = "Haun OID")
    private String applicationPeriod;
    @ApiModelProperty(value = "Kirjepoohjan ID")
    private Long templateId;

    public TemplateApplicationPeriodId() {
    }

    public TemplateApplicationPeriodId(Long templateId, String applicationPeriod) {
        this.applicationPeriod = applicationPeriod;
        this.templateId = templateId;
    }

    public String getApplicationPeriod() {
        return applicationPeriod;
    }

    public void setApplicationPeriod(String hakuOid) {
        this.applicationPeriod = hakuOid;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplateApplicationPeriodId)) {
            return false;
        }
        TemplateApplicationPeriodId that = (TemplateApplicationPeriodId) o;
        if (applicationPeriod != null ? !applicationPeriod.equals(that.applicationPeriod) : that.applicationPeriod != null) {
            return false;
        }
        if (templateId != null ? !templateId.equals(that.templateId) : that.templateId != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = applicationPeriod != null ? applicationPeriod.hashCode() : 0;
        result = HASH_FACTOR * result + (templateId != null ? templateId.hashCode() : 0);
        return result;
    }
}
