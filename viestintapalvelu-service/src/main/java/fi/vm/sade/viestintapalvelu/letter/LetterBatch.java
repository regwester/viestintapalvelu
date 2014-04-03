package fi.vm.sade.viestintapalvelu.letter;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import fi.vm.sade.viestintapalvelu.template.Template;

@ApiModel(value = "Kerralla muodostettavien koekutsukirjeiden joukko")
public class LetterBatch {
    @ApiModelProperty(value = "Kerralla muodostettavien koekutsukirjeiden joukko, (1-n)", required = true)
    private List<Letter> letters;

    @ApiModelProperty(value = "Kirjepohja")
    private Template template;

    @ApiModelProperty(value = "Kirjepohjan tunniste")
    private Long templateId;

    @ApiModelProperty(value = "Kirjeen yleiset personointikentät", required = false, notes = "")
    private Map<String, Object> templateReplacements;

    @ApiModelProperty(value = "Kirjepohjan tunniste")
    private String templateName;

    @ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'")
    private String languageCode;

    private String storingOid;

    private String organizationOid;
    
    
    public Map<String, Object> getTemplateReplacements() {
        return templateReplacements;
    }

    public void setTemplateReplacements(Map<String, Object> templateReplacements) {
        this.templateReplacements = templateReplacements;
    }

    public void setLetters(List<Letter> letters) {
        this.letters = letters;
    }

    public LetterBatch() {
    }

    public LetterBatch(List<Letter> letters) {
        this.letters = letters;
    }

    public List<Letter> getLetters() {
        return letters;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
   
	public String getStoringOid() {
		return storingOid;
	}

	public void setStoringOid(String storingOid) {
		this.storingOid = storingOid;
	}

	public String getOrganizationOid() {
		return organizationOid;
	}

	public void setOrganizationOid(String organizationOid) {
		this.organizationOid = organizationOid;
	}

	@Override
	public String toString() {
		return "LetterBatch [letters=" + letters + ", template=" + template
				+ ", templateId=" + templateId + ", templateReplacements="
				+ templateReplacements + ", templateName=" + templateName
				+ ", languageCode=" + languageCode + ", storingOid="
				+ storingOid + ", organizationOid=" + organizationOid + "]";
	}
    
}
