package fi.vm.sade.viestintapalvelu.letter;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class LetterListItem {

    @ApiModelProperty(value = "Kirjeen ID")
    private long id;

    @ApiModelProperty(value = "Haun OID")
    private String hakuOid;

    @ApiModelProperty(value = "Kirjeen tyyppi (jälkiohjauskirje/hyväksymiskirje)")
    private String tyyppi;

    @ApiModelProperty(value = "Kirjeen tiedostotyyppi (yleensä application/pdf)")
    private String tiedostotyyppi;

    @ApiModelProperty(value = "Aikaleima")
    private Date timestamp;

    public LetterListItem(long id, String hakuOid, String tyyppi, String tiedostotyyppi, Date timestamp) {
        this.id = id;
        this.hakuOid = hakuOid;
        this.tyyppi = tyyppi;
        this.tiedostotyyppi = tiedostotyyppi;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHakuOid() {
        return hakuOid;
    }

    public void setHakuOid(String hakuOid) {
        this.hakuOid = hakuOid;
    }

    public String getTyyppi() {
        return tyyppi;
    }

    public void setTyyppi(String tyyppi) {
        this.tyyppi = tyyppi;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTiedostotyyppi() {
        return tiedostotyyppi;
    }

    public void setTiedostotyyppi(String tiedostotyyppi) {
        this.tiedostotyyppi = tiedostotyyppi;
    }

    @Override
    public String toString() {
        return "LetterListItem{" +
                "id=" + id +
                ", hakuOid='" + hakuOid + '\'' +
                ", tyyppi='" + tyyppi + '\'' +
                ", tiedostotyyppi='" + tiedostotyyppi + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
