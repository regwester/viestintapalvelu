package fi.vm.sade.ryhmasahkoposti.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name = "luonnos", schema = "public")
@Entity
public class DraftModel extends BaseEntity {
    
    private static final long serialVersionUID = 1503120178623163485L;
    
    @Column(name = "vastaanottajan_osoite")
    private String replyTo;
    
    @Column(name = "aihe")
    private String subject;
    
    @Column(name = "sisalto")
    private String body;
    
    @Column(name = "html")
    private boolean isHtml;
    
    @Column(name = "lahettajan_oid")
    private String userOid;
    
    @Column(name = "lahettajan_osoite")
    private String senderAddress;
    
    @JoinTable(
      name="luonnos_liite",
      joinColumns=@JoinColumn(name="luonnos_id", referencedColumnName="id"),
      inverseJoinColumns=@JoinColumn(name="liite_id", referencedColumnName="id")
    )
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = ReportedAttachment.class)
    private Set<ReportedAttachment> attachments;
    
    @Column(name = "tallennettu")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    public DraftModel(){}
    
    private DraftModel(Builder builder) {
        this.replyTo = builder.replyTo;
        this.subject = builder.subject;
        this.body = builder.body;
        this.userOid = builder.userOid;
        this.attachments = builder.attachments;
        this.isHtml = builder.isHtml;
        this.createDate = builder.createDate;
        this.senderAddress = builder.senderAddress;
    }
    
    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }

    public Set<ReportedAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<ReportedAttachment> attachments) {
        this.attachments = attachments;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static class Builder {
        private String replyTo;
        private String subject;
        private String body;
        private String userOid;
        private Set<ReportedAttachment> attachments = new HashSet<ReportedAttachment>();
        private boolean isHtml;
        private Date createDate;
        private String senderAddress;
        
        public Builder() {}
        
        public Builder replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }
        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }
        public Builder body(String body) {
            this.body = body;
            return this;
        }
        public Builder userOid(String oid) {
            this.userOid = oid;
            return this;
        }
        public Builder addAttachment(ReportedAttachment a) {
            this.attachments.add(a);
            return this;
        }
        public Builder setAttachments(Set<ReportedAttachment> attachments) {
            this.attachments = attachments;
            return this;
        }
        public Builder isHtml(boolean isHtml) {
            this.isHtml = isHtml;
            return this;
        }
        public Builder createDate(Date date) {
            this.createDate = date;
            return this;
        }
        public Builder senderAddress(String address) {
            this.senderAddress = address;
            return this;
        }
        public DraftModel build() {
            return new DraftModel(this);
        }
        
    }
}
