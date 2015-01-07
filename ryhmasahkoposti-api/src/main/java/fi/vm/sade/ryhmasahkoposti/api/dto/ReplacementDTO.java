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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fi.vm.sade.generic.common.BaseDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplacementDTO extends BaseDTO {

    private static final long serialVersionUID = 8136375073148653926L;

    /**
     * Name
     */
    private String name = null;

    /**
     * Default value
     */
    private String defaultValue = null;

    /**
     * Mandatory
     */
    private boolean mandatory = false;

    /**
     * Timestamp
     */
    private Date timestamp;

    /**
     * Replacement name of sender from field
     */
    public final static String NAME_EMAIL_SENDER_FROM = "sender-from";

    /**
     * Replacement name of sender from personal field
     */
    public final static String NAME_EMAIL_SENDER_NAME_PERSONAL = "sender-from-personal";

    /**
     * Replacement name of replay-to field
     */
    public final static String NAME_EMAIL_REPLY_TO = "reply-to";

    /**
     * Replacement name of replay-to personal field
     */
    public final static String NAME_EMAIL_REPLY_TO_PERSONAL = "reply-to-personal";

    /**
     * Replacement name of email subject
     */
    public final static String NAME_EMAIL_SUBJECT = "subject";

    /**
     * Replacement name of email subject
     */
    public final static String NAME_EMAIL_BODY = "sisalto";

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue
     *            the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the mandatory
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * @param mandatory
     *            the mandatory to set
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ReplacementDTO [name=" + name + ", defaultValue=" + defaultValue + ", mandatory=" + mandatory + ", timestamp=" + timestamp + ", getId()="
                + getId() + "]";
    }
}
