/**
 * Copyright (c) 2012 The Finnish Board of Education - Opetushallitus
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
package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.Date;
import java.util.Set;

import fi.vm.sade.generic.common.BaseDTO;

public class TemplateDTO extends BaseDTO {

    private static final long serialVersionUID = 6850901653896485389L;

    /**
     * Name
     */
    private String name;

    /**
     * Timestamp
     */
    private Date timestamp;

    /**
     * Language
     */
    private String language;

    /**
     * Version no
     */
    private String versionro;	

    /**
     * Styles
     */
    private String styles;

    /**
     * Type
     */
    private String type;

    /**
     * Storing oid
     */
    private String storingOid;

    /**
     * Organization oid
     */
    private String organizationOid;

    /**
     * Template content
     */
    private Set<TemplateContentDTO> contents;

    /**
     * Template replacements
     */
    private Set<ReplacementDTO> replacements;


    /**
     * Type email
     */
    public static final String TYPE_EMAIL = "email";

    /**
     * Type letter
     */
    public static final String TYPE_LETTER = "letter";

    /**
     * Default template language code
     */

    public static final String DEFAULT_LANG_CODE = "FI";

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
	return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
	return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
	this.language = language;
    }

    /**
     * @return the versionro
     */
    public String getVersionro() {
	return versionro;
    }

    /**
     * @param versionro the versionro to set
     */
    public void setVersionro(String versionro) {
	this.versionro = versionro;
    }

    /**
     * @return the styles
     */
    public String getStyles() {
	return styles;
    }

    /**
     * @param styles the styles to set
     */
    public void setStyles(String styles) {
	this.styles = styles;
    }

    /**
     * @return the type
     */
    public String getType() {
	return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
	this.type = type;
    }

    /**
     * @return the storingOid
     */
    public String getStoringOid() {
	return storingOid;
    }

    /**
     * @param storingOid the storingOid to set
     */
    public void setStoringOid(String storingOid) {
	this.storingOid = storingOid;
    }

    /**
     * @return the organizationOid
     */
    public String getOrganizationOid() {
	return organizationOid;
    }

    /**
     * @param organizationOid the organizationOid to set
     */
    public void setOrganizationOid(String organizationOid) {
	this.organizationOid = organizationOid;
    }

    /**
     * @return the contents
     */
    public Set<TemplateContentDTO> getContents() {
	return contents;
    }

    /**
     * @param contents the contents to set
     */
    public void setContents(Set<TemplateContentDTO> contents) {
	this.contents = contents;
    }

    /**
     * @return the replacements
     */
    public Set<ReplacementDTO> getReplacements() {
	return replacements;
    }

    /**
     * @param replacements the replacements to set
     */
    public void setReplacements(Set<ReplacementDTO> replacements) {
	this.replacements = replacements;
    }

    @Override
    public String toString() {
	return "TemplateDTO [name=" + name + ", timestamp=" + timestamp
		+ ", language=" + language + ", versionro=" + versionro
		+ ", styles=" + styles + ", type=" + type + ", storingOid="
		+ storingOid + ", organizationOid=" + organizationOid
		+ ", contents=" + contents + ", replacements=" + replacements
		+ "]";
    }
}
