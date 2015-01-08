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
package fi.vm.sade.viestintapalvelu.externalinterface.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * User: ratamaa Date: 29.10.2014 Time: 14:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisaatioHierarchyDto implements Serializable {
    private static final long serialVersionUID = -4472354752997371430L;
    @JsonIgnore(value = true)
    private OrganisaatioHierarchyDto parent;
    private String oid;
    private String parentOid;
    private String ytunnus;
    private Map<String, String> nimi = new HashMap<String, String>();
    private List<OrganisaatioHierarchyDto> children = new ArrayList<OrganisaatioHierarchyDto>();

    public OrganisaatioHierarchyDto() {
    }

    public OrganisaatioHierarchyDto(OrganisaatioHierarchyDto other) {
        if (other == null) {
            return;
        }
        // this.parent = new OrganisaatioHierarchyDto(other.parent);
        this.oid = other.oid;
        this.parentOid = other.parentOid;
        this.ytunnus = other.ytunnus;
        this.nimi = new HashMap<String, String>();
        this.nimi.putAll(other.nimi);
        this.children = new ArrayList<OrganisaatioHierarchyDto>();
        for (OrganisaatioHierarchyDto otherChild : other.children) {
            OrganisaatioHierarchyDto newChild = new OrganisaatioHierarchyDto(otherChild);
            newChild.setParent(this);
            this.children.add(newChild);
        }
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getParentOid() {
        return parentOid;
    }

    public void setParentOid(String parentOid) {
        this.parentOid = parentOid;
    }

    public String getYtunnus() {
        return ytunnus;
    }

    public void setYtunnus(String ytunnus) {
        this.ytunnus = ytunnus;
    }

    public Map<String, String> getNimi() {
        return nimi;
    }

    public void setNimi(Map<String, String> nimi) {
        this.nimi = nimi;
    }

    public List<OrganisaatioHierarchyDto> getChildren() {
        return children;
    }

    public void setChildren(List<OrganisaatioHierarchyDto> children) {
        this.children = children;
    }

    public OrganisaatioHierarchyDto getParent() {
        return parent;
    }

    public void setParent(OrganisaatioHierarchyDto parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OrganisaatioHierarchyDto that = (OrganisaatioHierarchyDto) o;

        if (oid != null ? !oid.equals(that.oid) : that.oid != null)
            return false;
        if (parentOid != null ? !parentOid.equals(that.parentOid) : that.parentOid != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = oid != null ? oid.hashCode() : 0;
        result = 31 * result + (parentOid != null ? parentOid.hashCode() : 0);
        return result;
    }
}
