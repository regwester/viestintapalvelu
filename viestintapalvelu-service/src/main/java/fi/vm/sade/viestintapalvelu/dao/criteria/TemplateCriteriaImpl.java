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

package fi.vm.sade.viestintapalvelu.dao.criteria;

/**
 * User: ratamaa
 * Date: 9.9.2014
 * Time: 10:46
 */
public class TemplateCriteriaImpl implements TemplateCriteria {
    private String name;
    private String language;
    private String type;
    private String applicationPeriod;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getApplicationPeriod() {
        return applicationPeriod;
    }

    protected TemplateCriteriaImpl copy() {
        TemplateCriteriaImpl copy = new TemplateCriteriaImpl();
        copy.name = this.name;
        copy.language = this.language;
        copy.type = this.type;
        copy.applicationPeriod = this.applicationPeriod;
        return copy;
    }

    @Override
    public TemplateCriteria withName(String name) {
        TemplateCriteriaImpl copy = copy();
        copy.name = name;
        return copy;
    }

    @Override
    public TemplateCriteria withLanguage(String language) {
        TemplateCriteriaImpl copy = copy();
        copy.language = language;
        return copy;
    }

    @Override
    public TemplateCriteria withType(String type) {
        TemplateCriteriaImpl copy = copy();
        copy.type = type;
        return copy;
    }

    @Override
    public TemplateCriteria withApplicationPeriod(String hakuOid) {
        TemplateCriteriaImpl copy = copy();
        copy.applicationPeriod = hakuOid;
        return copy;
    }

    @Override
    public String toString() {
        return "TemplateCriteriaImpl{" +
                "name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", type='" + type + '\'' +
                ", applicationPeriod='" + applicationPeriod + '\'' +
                '}';
    }
}
