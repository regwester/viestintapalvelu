package fi.vm.sade.viestintapalvelu.template;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Kirjetemplate")
public class Template {

    private long id;
    private long version;
    private String name;
    private List<TemplateContent> content;

    @ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'")
    private String languageCode;
    private List<Replacement> replacements;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TemplateContent> getContent() {
        return content;
    }

    public void setContent(List<TemplateContent> content) {
        this.content = content;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public List<Replacement> getReplacements() {
        return replacements;
    }

    public void setReplacements(List<Replacement> replacements) {
        this.replacements = replacements;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Template [id=" + id + ", name=" + name + ", content=" + content
                + ", languageCode=" + languageCode + ", replacements="
                + replacements + "]";
    }

}
