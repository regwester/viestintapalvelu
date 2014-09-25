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
     * Value of the replacement
     */
    private Object value;
    /**
     * Default value
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
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return value if set, defaultValue otherwise
     */
    public Object getEffectiveValue() {
        if (this.value!= null) {
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
     * @param value MAY be a java.lang.String (for static content), java.util.List<String> (for iterable lists)
     *              or special combination of Lists/Maps containing Strings (for tabular presentations)
     *              (replaces defaultValue if set)
     */
    public void setValue(Object value) {
        this.defaultValue = null;
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
     * @param defaultValue the defaultValue to set (replaces value if set)
     * @deprecated supported for compatibility, use getValue
     * @see #setValue(Object)
     */
    @Deprecated
    public void setDefaultValue(String defaultValue) {
        this.value = null;
        this.defaultValue = defaultValue;
    }

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
    @Override
    public String toString() {
        return "ReportedRecipientReplacementDTO [name=" + name
                + ", value=" + value
                + ", defaultValue=" + defaultValue + "]";
    }
}
