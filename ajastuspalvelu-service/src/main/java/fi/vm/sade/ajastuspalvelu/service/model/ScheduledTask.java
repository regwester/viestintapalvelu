package fi.vm.sade.ajastuspalvelu.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;

@TypeDef(name = "dateTime", typeClass = org.jadira.usertype.dateandtime.joda.PersistentDateTime.class, parameters = {@Parameter(name = "databaseZone", value = "jvm")})
@Table(name = "ajastettu_tehtava")
@Entity(name = "ScheduledTask")
public class ScheduledTask {
    
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
    
}
