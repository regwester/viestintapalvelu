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

    private String storingOid;

    private String organizationOid;

    private List<TemplateContent> contents;

    private List<Replacement> replacements;

    private String templateVersio;
    
    private String type;
    
	public List<TemplateContent> getContents() {
        return contents;
    }    

    public long getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getName() {
        return name;
    }

    public String getOrganizationOid() {
        return organizationOid;
    }

    public List<Replacement> getReplacements() {
        return replacements;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getStoringOid() {
        return storingOid;
    }

    public String getStyles() {
        return styles;
    }

    /**
	 * Method getTemplateVersio returns the version of the template.
	 * 
	 * @return	String
	 */
	public String getTemplateVersio() {
		return templateVersio;
	}

    public Date getTimestamp() {
        return timestamp;
    }

    public void setContents(List<TemplateContent> contents) {
        this.contents = contents;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganizationOid(String organizationOid) {
        this.organizationOid = organizationOid;
    }

    public void setReplacements(List<Replacement> replacements) {
        this.replacements = replacements;
    }

    public void setStoringOid(String storingOid) {
        this.storingOid = storingOid;
    }

    public void setStyles(String styles) {
        this.styles = styles;
    }
    
	public void setTemplateVersio(String templateVersio) {
		this.templateVersio = templateVersio;
	}
	public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Template [id=" + id + ", timestamp=" + timestamp + ", name="
				+ name + ", language=" + language + ", styles=" + styles
				+ ", storingOid=" + storingOid + ", organizationOid="
				+ organizationOid + ", contents=" + contents
				+ ", replacements=" + replacements + ", templateVersio="
				+ templateVersio + ", type=" + type + "]";
	}
}
