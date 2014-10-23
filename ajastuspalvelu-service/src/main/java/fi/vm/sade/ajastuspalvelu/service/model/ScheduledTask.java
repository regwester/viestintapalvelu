package fi.vm.sade.ajastuspalvelu.service.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;

@TypeDef(name = "dateTime", typeClass = org.jadira.usertype.dateandtime.joda.PersistentDateTime.class, parameters = {@Parameter(name = "databaseZone", value = "jvm")})
@Table(name = "ajastettu_tehtava")
@Entity(name = "ScheduledTask")
public class ScheduledTask implements Serializable {
    private static final long serialVersionUID = -896169749313021674L;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @SequenceGenerator(name = "ajastettu_tehtava_id_seq", sequenceName = "ajastettu_tehtava_id_seq")
    @GeneratedValue(generator = "ajastettu_tehtava_id_seq")
    private Long id;

    @Version
    @Column(name = "versio", nullable = false)
    private Long versio;
    
    @JoinColumn(name = "tehtava_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Task task;
    
    @Column(name = "luoja_oid")
    private String creatorOid;
    
    @Column(name = "luoja_organisaatio_oid")
    private String creatorOrganizationOid;
    
    @Type(type = "dateTime")
    @Column(name = "luontiaika", nullable = false)
    private DateTime created = new DateTime();
    
    @Type(type = "dateTime")
    @Column(name = "muokkausaika")
    private DateTime modified;
    
    @Column(name = "haku_oid")
    private String hakuOid;
    
    @Type(type = "dateTime")
    @Column(name = "poistettu")
    private DateTime removed;
    
    @Type(type = "dateTime")
    @Column(name = "yksittaisen_ajohetki")
    private DateTime runtimeForSingle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduledTask",
        cascade = CascadeType.PERSIST)
    private Set<ScheduledRun> runs = new HashSet<ScheduledRun>(0);

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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getCreatorOid() {
        return creatorOid;
    }

    public void setCreatorOid(String creatorOid) {
        this.creatorOid = creatorOid;
    }

    public String getCreatorOrganizationOid() {
        return creatorOrganizationOid;
    }

    public void setCreatorOrganizationOid(String creatorOrganizationOid) {
        this.creatorOrganizationOid = creatorOrganizationOid;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getModified() {
        return modified;
    }

    public void setModified(DateTime modified) {
        this.modified = modified;
    }

    public String getHakuOid() {
        return hakuOid;
    }

    public void setHakuOid(String hakuOid) {
        this.hakuOid = hakuOid;
    }

    public DateTime getRemoved() {
        return removed;
    }

    public void setRemoved(DateTime removed) {
        this.removed = removed;
    }

    public DateTime getRuntimeForSingle() {
        return runtimeForSingle;
    }

    public void setRuntimeForSingle(DateTime runtimeForSingle) {
        this.runtimeForSingle = runtimeForSingle;
    }

    public Set<ScheduledRun> getRuns() {
        return runs;
    }

    protected void setRuns(Set<ScheduledRun> runs) {
        this.runs = runs;
    }
}
