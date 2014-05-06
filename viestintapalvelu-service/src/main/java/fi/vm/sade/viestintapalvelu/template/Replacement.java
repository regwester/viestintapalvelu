package fi.vm.sade.viestintapalvelu.template;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Replacement", description = "Korvaukentät, nimi / arvo -parit")
public class Replacement {

	@ApiModelProperty(value = "ID")
    private long id;
    
	@ApiModelProperty(value = "Nimi")
    private String name = null;

	@ApiModelProperty(value = "Oletusarvo")
    private String defaultValue = null;

	@ApiModelProperty(value = "Aikaleima")
    private Date timestamp;
    
	@ApiModelProperty(value = "Pakollinen (True/False)")
    private boolean mandatory = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

	@Override
	public String toString() {
		return "Replacement [name=" + name + ", defaultValue="
				+ defaultValue + ", mandatory=" + mandatory
				+ ", timestamp=" + timestamp + ", id=" + id + "]";
	}

}

