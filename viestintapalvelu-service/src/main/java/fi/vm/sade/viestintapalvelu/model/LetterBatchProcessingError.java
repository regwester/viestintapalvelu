package fi.vm.sade.viestintapalvelu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fi.vm.sade.generic.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jonimake on 22.9.2014.
 */
@Table(name = "kirjelahetysvirhe", schema= "kirjeet")
@Entity(name = "LetterBatchProcessingError")
public class LetterBatchProcessingError extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "kirjelahetys_id", nullable = false)
    @JsonBackReference
    private LetterBatch letterBatch;

    @ManyToOne
    @JoinColumn(name = "vastaanottaja_id", nullable = false)
    @JsonBackReference
    private LetterReceivers letterReceivers;

    @Column(name = "virheen_syy", nullable =  false)
    private String errorCause;

    @Column(name = "aika", nullable =  false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date errorTime;

    public String getErrorCause() {
        return errorCause;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }

    public LetterBatch getLetterBatch() {
        return letterBatch;
    }

    public void setLetterBatch(LetterBatch letterBatch) {
        this.letterBatch = letterBatch;
    }

    public Date getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(Date errorTime) {
        this.errorTime = errorTime;
    }

    @Override
    public String toString() {
        return "LetterBatchProcessingError{" +
                "errorCause='" + errorCause + '\'' +
                ", errorTime=" + errorTime +
                '}';
    }

    public LetterReceivers getLetterReceivers() {
        return letterReceivers;
    }

    public void setLetterReceivers(LetterReceivers letterReceivers) {
        this.letterReceivers = letterReceivers;
    }
}
