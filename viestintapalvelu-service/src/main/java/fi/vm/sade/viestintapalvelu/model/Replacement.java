package fi.vm.sade.viestintapalvelu.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name = "korvauskentat", schema= "kirjeet")
@Entity()
public class Replacement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kirjepohja_id")
    private Template template;

    @Column(name = "nimi")
    private String name = null;

    @Column(name = "oletus_arvo")
    private String defaultValue = null;

    @Column(name = "pakollinen")
    private boolean mandatory = false;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    
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

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

	@Override
	public String toString() {
		return "Replacement [template=" + template + ", name=" + name
				+ ", defaultValue=" + defaultValue + ", mandatory=" + mandatory
				+ ", timestamp=" + timestamp + "]";
	}

}
