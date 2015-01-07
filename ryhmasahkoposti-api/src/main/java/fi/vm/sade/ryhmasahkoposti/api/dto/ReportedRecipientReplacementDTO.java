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
package fi.vm.sade.ryhmasahkoposti.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Reported recipient replacement data transfer object.
 * 
 * @author ovmol1
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportedRecipientReplacementDTO {
    /**
     * Name
     */
    private String name = null;
    /**
     * Value of the replacement
     */
    private Object value;
    /**
     * Default value
     * 
     * @deprecated use value instead
     */
    @Deprecated
    private String defaultValue = null;

    public ReportedRecipientReplacementDTO() {
    }

    public ReportedRecipientReplacementDTO(String name, Object value) {
        this.name = name;
        this.value = value;
    }

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
     * @return value if set, defaultValue otherwise
     */
    @JsonIgnore
    public Object getEffectiveValue() {
        if (this.value != null) {
            return this.value;
        }
        return this.defaultValue;
    }

    /**
     * @return value for the replacement
     * @see #setValue(Object) for more details
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value
     *            MAY be a java.lang.String (for static content),
     *            java.util.List<String> (for iterable lists) or special
     *            combination of Lists/Maps containing Strings (for tabular
     *            presentations) (replaces defaultValue if set)
     */
    public void setValue(Object value) {
        if (value != null && this.defaultValue != null) {
            this.defaultValue = null;
        }
        this.value = value;
    }

    /**
     * @return the defaultValue
     * @deprecated supported for compatibility, use getValue
     * @see #setValue(Object)
     */
    @Deprecated
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue
     *            the defaultValue to set (replaces value if set)
     * @deprecated supported for compatibility, use getValue
     * @see #setValue(Object)
     */
    @Deprecated
    public void setDefaultValue(String defaultValue) {
        if (defaultValue != null && this.value != null) {
            this.value = null;
        }
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return "ReportedRecipientReplacementDTO [name=" + name + ", value=" + value + ", defaultValue=" + defaultValue + "]";
    }
}
