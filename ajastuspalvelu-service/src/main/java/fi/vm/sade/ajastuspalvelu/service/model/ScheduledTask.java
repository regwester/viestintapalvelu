package fi.vm.sade.ajastuspalvelu.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.joda.time.DateTime;

@TypeDef(name = "dateTime", typeClass = org.jadira.usertype.dateandtime.joda.PersistentDateTime.class, parameters = {@Parameter(name = "databaseZone", value = "jvm")})
@Table(name = "ajastettu_tehtava")
@Entity(name = "ScheduledTask")
public class ScheduledTask extends ScheduleBaseEntity {
    
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
    
}
