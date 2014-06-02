package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.Date;

import fi.vm.sade.generic.common.BaseDTO;

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
     * @return the defaultValue
     */
    public String getDefaultValue() {
	return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
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
     * @param mandatory the mandatory to set
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
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ReplacementDTO [name=" + name + ", defaultValue="
		+ defaultValue + ", mandatory=" + mandatory + ", timestamp="
		+ timestamp + ", getId()=" + getId() + "]";
    }
}
