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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.joda.time.DateTime;

@TypeDef(name = "dateTime", typeClass = org.jadira.usertype.dateandtime.joda.PersistentDateTime.class, parameters = {@Parameter(name = "databaseZone", value = "jvm")})
@Table(name = "ajastettu_ajo")
@Entity(name = "ScheduledRun")
public class ScheduledRun {
    
    public enum State {
        STARTED, FINISHED, ERROR;
    }
    
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @SequenceGenerator(name = "ajastettu_ajo_id_seq", sequenceName = "ajastettu_ajo_id_seq")
    @GeneratedValue(generator = "ajastettu_ajo_id_seq")
    private Long id;
    
    @Version
    @Column(name = "versio", nullable = false)
    private Long versio;
    
    @JoinColumn(name = "ajastettu_tehtava", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ScheduledTask scheduledTask;
    
    @Type(type = "dateTime")
    @Column(name = "ajo_aloitettu", nullable = false)
    private DateTime started = new DateTime();
    
    @Type(type = "dateTime")
    @Column(name = "ajo_paattynyt")
    private DateTime finished;
    
    @Column(name = "tila")
    private State state = State.STARTED;
    
    @Column(name = "virheviesti")
    private String errorMessage;
    
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
    
    public ScheduledTask getScheduledTask() {
        return scheduledTask;
    }
    
    public void setScheduledTask(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }
    
    public DateTime getStarted() {
        return started;
    }
    
    public void setStarted(DateTime started) {
        this.started = started;
    }
    
    public DateTime getFinished() {
        return finished;
    }
    
    public void setFinished(DateTime finished) {
        this.finished = finished;
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
}
