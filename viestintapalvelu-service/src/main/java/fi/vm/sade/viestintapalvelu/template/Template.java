package fi.vm.sade.viestintapalvelu.template;

import java.util.Date;
import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(value = "Kirjetemplate")
public class Template{

    private static final long serialVersionUID = 4178735997933155683L;

    private long id;
    
    private Date timestamp;

    private String name;

    private String language;

    private String styles;
    
    public String getStyles() {
        return styles;
    }

    public void setStyles(String styles) {
        this.styles = styles;
    }

    private String storingOid;

    private String organizationOid;

    private List<TemplateContent> contents;

    private List<Replacement> replacements;
    
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
