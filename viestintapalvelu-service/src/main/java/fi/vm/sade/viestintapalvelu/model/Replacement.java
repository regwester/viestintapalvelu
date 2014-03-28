package fi.vm.sade.viestintapalvelu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.wordnik.swagger.annotations.ApiModel;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name = "korvauskentat")
@Entity()
public class Replacement extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kirjepohja_id")
    private Template template;

    @Column(name = "nimi")
    private String name = null;

    @Column(name = "oletus_arvo")
    private String defaultValue = null;

    @Column(name = "pakollinen")
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

    @Override
    public String toString() {
        return "Replacement [name=" + name + ", defaultValue=" + defaultValue
                + ", mandatory=" + mandatory + "]";
    }

}
