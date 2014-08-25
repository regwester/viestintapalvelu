package fi.vm.sade.viestintapalvelu.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import fi.vm.sade.generic.model.BaseEntity;


@Table(name = "vastaanottajaemail", schema= "kirjeet")
@Entity()
public class LetterReceiverEmail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @OneToOne()
    @JoinColumn(name = "vastaanottaja_id")
    @JsonBackReference
    private LetterReceivers letterReceivers;

    @Column(name = "luotu", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "lahetetty", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;
    
    @Column(name = "email", nullable = false)
    private String email;

    public LetterReceivers getLetterReceivers() {
        return letterReceivers;
    }

    public void setLetterReceivers(LetterReceivers letterReceivers) {
        this.letterReceivers = letterReceivers;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "LetterReceiverEmail [letterReceivers=" + letterReceivers + ", createDate=" + createDate + ", sentDate=" + sentDate + ", email=" + email + "]";
    }
}
