package fi.vm.sade.ryhmasahkoposti.api.dto;

/**
 * Reported recipient replacement data transfer object.
 * 
 * @author ovmol1
 *
 */
public class ReportedRecipientReplacementDTO {
    
    /**
     * Name
     */
    private String name = null;

    /**
     * Default value
     */
    private String defaultValue = null;

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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ReportedRecipientReplacementDTO [name=" + name
		+ ", defaultValue=" + defaultValue + "]";
    }
}
