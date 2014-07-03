package fi.vm.sade.viestintapalvelu.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonBackReference;

import fi.vm.sade.generic.model.BaseEntity;

/*
 * CREATE TABLE kirjeet.iposti
(
  id bigint NOT NULL,
  version bigint,
  kirjelahetys_id bigint,
  aineisto bytea,
  sisaltotyyppi character varying(255),
  luotu time without time zone,
  lahetetty time without time zone,
  CONSTRAINT iposti_pk PRIMARY KEY (id),
  CONSTRAINT iposti_kirjelahetys_id_key FOREIGN KEY (kirjelahetys_id)
      REFERENCES kirjeet.kirjelahetys (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */

@Table(name = "iposti", schema = "kirjeet")
@Entity(name = "IPosti")
public class IPosti extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kirjelahetys_id")
    @JsonBackReference
    private LetterBatch letterBatch;

    @Column(name = "luotu", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "lahetetty", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;
    
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "aineisto")
    private byte[] content;
    
    @Column(name = "aineiston_nimi")
    private String contentName = "";
    
    @Column(name = "sisaltotyyppi")
    private String contentType = "";

    public LetterBatch getLetterBatch() {
        return letterBatch;
    }

    public void setLetterBatch(LetterBatch letterBatch) {
        this.letterBatch = letterBatch;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date date) {
        this.createDate = date;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date date) {
        this.sentDate = date;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public String getContentName() {
        return contentName;
    }
    
    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

}
