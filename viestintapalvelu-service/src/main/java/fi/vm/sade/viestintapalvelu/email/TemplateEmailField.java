package fi.vm.sade.viestintapalvelu.email;

public enum TemplateEmailField {

    BODY("sisalto"),
    SUBJECT("otsikko");

    String fieldName;
    private TemplateEmailField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
