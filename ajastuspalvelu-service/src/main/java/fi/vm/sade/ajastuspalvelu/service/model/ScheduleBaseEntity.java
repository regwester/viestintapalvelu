package fi.vm.sade.ajastuspalvelu.service.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@MappedSuperclass
public abstract class ScheduleBaseEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @SequenceGenerator(name = "ajastettu_ajo_id_seq", sequenceName = "ajastettu_ajo_id_seq")
    @GeneratedValue(generator = "ajastettu_ajo_id_seq")
    private Long id;
    
    @Version
    @Column(name = "versio", nullable = false)
    private Long versio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersio() {
        return versio;
    }

    public void setVersio(Long versio) {
        this.versio = versio;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append("[id=");
        sb.append(getId());
        sb.append(", version=");
        sb.append(getVersio());
        sb.append("]");

        return sb.toString();
    }

}
