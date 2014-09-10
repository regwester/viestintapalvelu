package fi.vm.sade.viestintapalvelu.email;

public enum TemplateEmailField {

    BODY("sisalto", true, String.class),
    SUBJECT("otsikko", true, String.class),
    ATTACHMENT("liite", false, null);

    String fieldName;
    boolean mandatory;

    @SuppressWarnings("rawtypes")
    Class type; 
    
    private TemplateEmailField(String fieldName, boolean mandatory, Class type) {
        this.fieldName = fieldName;
        this.mandatory = mandatory;
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }
    
    public boolean isMandatory() {
        return this.mandatory;
    }
    
    @SuppressWarnings("rawtypes")
    public Class getType() {
        return this.type;
    }
}
