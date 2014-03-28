package fi.vm.sade.viestintapalvelu.letter;

import java.util.Map;

import org.apache.cxf.common.util.StringUtils;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.model.Template;

@ApiModel(value = "Kirjemallipohjaan sisällytettävät kirjekohtaiset tiedot")
public class Letter {

    @ApiModelProperty(value = "Osoitetiedot", required = true)
    private AddressLabel addressLabel;

    @ApiModelProperty(value = "Kirjepohjan tunniste")
    private Template template;

    @ApiModelProperty(value = "Kirjepohjan tunniste")
    private String templateName;

    @ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'")
    private String languageCode;

    @ApiModelProperty(value = "Kirjeen yleiset personointikentät", required = false, notes = "")
    private Map<String, Object> templateReplacements;

    public Letter() {
    }

    public Letter(AddressLabel addressLabel, Template template,
            Map<String, Object> customLetterContents) {
        this.addressLabel = addressLabel;
        this.languageCode = StringUtils.isEmpty(languageCode) ? "FI"
                : languageCode;
        this.templateReplacements = customLetterContents;
    }

    public Letter(AddressLabel addressLabel, String templateName,
            String languageCode, Map<String, Object> replacements) {
        this.addressLabel = addressLabel;
        this.languageCode = StringUtils.isEmpty(languageCode) ? "FI"
                : languageCode;
        this.templateReplacements = replacements;
        this.templateName = templateName;
    }

    public AddressLabel getAddressLabel() {
        return addressLabel;
    }

    public Map<String, Object> getCustomLetterContents() {
        return this.templateReplacements;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, Object> getTemplateReplacements() {
        return templateReplacements;
    }

    public void setTemplateReplacements(Map<String, Object> templateReplacements) {
        this.templateReplacements = templateReplacements;
    }

    public void setAddressLabel(AddressLabel addressLabel) {
        this.addressLabel = addressLabel;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return "Letter [addressLabel=" + addressLabel + ", template="
                + template + ", templateName=" + templateName
                + ", languageCode=" + languageCode + ", templateReplacements="
                + templateReplacements + "]";
    }

}
