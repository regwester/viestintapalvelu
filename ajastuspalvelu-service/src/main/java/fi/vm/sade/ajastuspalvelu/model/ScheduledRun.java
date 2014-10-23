package fi.vm.sade.ajastuspalvelu.model;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Table(name = "ajastettu_ajo")
@Entity(name = "ScheduledRun")
public class ScheduledRun implements Serializable {
    private static final long serialVersionUID = -7836109411827939921L;

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
    
    @JoinColumn(name = "ajastettu_tehtava_id", nullable = false)
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
