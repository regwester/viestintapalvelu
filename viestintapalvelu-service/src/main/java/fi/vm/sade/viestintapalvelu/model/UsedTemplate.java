package fi.vm.sade.viestintapalvelu.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name = "kaytetytpohjat", schema= "kirjeet")
@Entity(name = "UsedTemplate")
public class UsedTemplate extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name = "kirjelahetys_id", nullable = false)
    private LetterBatch letterBatch;
    
    @ManyToOne
    @JoinColumn(name = "kirjepohja_id", nullable = false)
    private Template template;
    
    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    
    public LetterBatch getLetterBatch() {
        return letterBatch;
    }
    
    public void setLetterBatch(LetterBatch letterBatch) {
        this.letterBatch = letterBatch;
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
    
    @PrePersist
    protected void onCreate() {
        onUpdate();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "UsedTemplate [letterBatch=" + letterBatch + ", template=" + template + ", timestamp=" + timestamp + "]";
    }

}
