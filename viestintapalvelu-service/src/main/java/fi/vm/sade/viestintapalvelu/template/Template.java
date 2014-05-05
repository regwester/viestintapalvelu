package fi.vm.sade.viestintapalvelu.template;

import java.util.Date;
import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Template", description = "Kirjepohje, perustiedot (nimi / tyyli / kielikoodi")
public class Template{

    private static final long serialVersionUID = 4178735997933155683L;

	@ApiModelProperty(value = "ID")
	private long id;
    
	@ApiModelProperty(value = "Nimi")
    private String name;

	@ApiModelProperty(value = "Aikaleima")
    private Date timestamp;

	@ApiModelProperty(value = "Kielikoodi")
    private String language;

	@ApiModelProperty(value = "Tyyli")
    private String styles;
    
	@ApiModelProperty(value = "Tallentajan Oid")
    private String storingOid;

	@ApiModelProperty(value = "Organisaation Oid")
    private String organizationOid;

	@ApiModelProperty(value = "Kirjepohjan sisältö. 1 per sivu")
    private List<TemplateContent> contents;

	@ApiModelProperty(value = "Kirjepohjan korvauskentät")
    private List<Replacement> replacements;
    
	@ApiModelProperty(value = "Kirjepohjan versio")
	private String templateVersio;    

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getStyles() {
        return styles;
    }

    public void setStyles(String styles) {
        this.styles = styles;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<TemplateContent> getContents() {
        return contents;
    }

    public void setContents(List<TemplateContent> contents) {
        this.contents = contents;
    }

    public List<Replacement> getReplacements() {
        return replacements;
    }

    public void setReplacements(List<Replacement> replacements) {
        this.replacements = replacements;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
	/**
	 * Method getTemplateVersio returns the version of the template.
	 * 
	 * @return	String
	 */
	public String getTemplateVersio() {
		return templateVersio;
	}
	public void setTemplateVersio(String templateVersio) {
		this.templateVersio = templateVersio;
	}

	@Override
	public String toString() {
		return "Template [id=" + id + ", timestamp=" + timestamp + ", name="
				+ name + ", language=" + language + ", styles=" + styles
				+ ", storingOid=" + storingOid + ", organizationOid="
				+ organizationOid + ", contents=" + contents
				+ ", replacements=" + replacements + ", templateVersio="
				+ templateVersio + "]";
	}

}
