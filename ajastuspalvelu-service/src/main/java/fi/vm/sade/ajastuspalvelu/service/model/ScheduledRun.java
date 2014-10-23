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
@Table(name = "ajastettu_ajo")
@Entity(name = "ScheduledRun")
public class ScheduledRun extends ScheduleBaseEntity {
    
    public enum State {
        STARTED, FINISHED, ERROR;
    }
    
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
