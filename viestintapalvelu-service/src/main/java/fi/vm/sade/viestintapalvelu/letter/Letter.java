package fi.vm.sade.viestintapalvelu.letter;

import java.util.Map;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.model.Template;

@ApiModel(value = "Kirjemallipohjaan sisällytettävät kirjekohtaiset tiedot")
public class Letter {

    @ApiModelProperty(value = "Osoitetiedot", required = true)
    private AddressLabel addressLabel;

    @ApiModelProperty(value = "Kirjepohjan vastaanottaja kielikoodi. Mikäli annettua kielikoodia vastaava kirjepohja löytyy, käytetään sitä."
            + " kielikoodi ISO 639-1")
    private String languageCode;

    @ApiModelProperty(value = "Kirjeen vastaanottajakohtaiset personointikentät", required = false, notes = "")
    private Map<String, Object> templateReplacements;
    
    private LetterContent letterContent;
    
    
    public Letter() {
    }

    public Letter(AddressLabel addressLabel,  Map<String, Object> customLetterContents) {
        this.addressLabel = addressLabel;
        this.templateReplacements = customLetterContents;
    }
    public Letter(AddressLabel addressLabel, Template template,
            Map<String, Object> customLetterContents) {
        this.addressLabel = addressLabel;
        this.templateReplacements = customLetterContents;
    }

    public Letter(AddressLabel addressLabel, String templateName,
            String languageCode, Map<String, Object> replacements) {
        this.addressLabel = addressLabel;
        this.templateReplacements = replacements;
    }

    public AddressLabel getAddressLabel() {
        return addressLabel;
    }

    public Map<String, Object> getCustomLetterContents() {
        return this.templateReplacements;
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
        
    public LetterContent getLetterContent() {
		return letterContent;
	}

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

	public void setLetterContent(LetterContent letterContent) {
		this.letterContent = letterContent;
	}

	@Override
    public String toString() {
        return "Letter [addressLabel=" + addressLabel + ", templateReplacements="
                + templateReplacements + "]";
    }

}
