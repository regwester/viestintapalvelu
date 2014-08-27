package fi.vm.sade.ryhmasahkoposti.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
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

    @Column(name = "organisaation_oid")
    private String organizationOid;
    
    @Column(name = "lahettajan_osoite")
    private String from;
    
    @Column(name = "lahettajan_nimi")
    private String sender;

    @ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="luonnos_liite",
            joinColumns = @JoinColumn(name="luonnos_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="liite_id", referencedColumnName="id"))
    private Set<ReportedAttachment> attachments;
    
    @Column(name = "tallennettu")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    public DraftModel(){
        super();
    }
    
    private DraftModel(Builder builder) {
        this.replyTo = builder.replyTo;
        this.subject = builder.subject;
        this.body = builder.body;
        this.userOid = builder.userOid;
        this.organizationOid = builder.organizationOid;
        this.attachments = builder.attachments;
        this.isHtml = builder.isHtml;
        this.createDate = builder.createDate;
        this.from = builder.from;
        this.sender = builder.sender;
        
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

    public String getOrganizationOid() { return organizationOid; }

    public void setOrganizationOid(String oid) {
        this.organizationOid = oid;
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
    
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static class Builder {
        private String replyTo;
        private String subject;
        private String body;
        private String userOid;
        private String organizationOid;
        private Set<ReportedAttachment> attachments = new HashSet<ReportedAttachment>();
        private boolean isHtml;
        private Date createDate;
        private String sender;
        private String from;
        
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
        public Builder organizationOid(String oid) {
            this.organizationOid = oid;
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
        public Builder sender(String address) {
            this.sender = address;
            return this;
        }
        public Builder from(String from) {
            this.from = from;
            return this;
        }
        public DraftModel build() {
            return new DraftModel(this);
        }
        
    }

    @Override
    public String toString() {
        return "DraftModel{" +
                "replyTo='" + replyTo + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", isHtml=" + isHtml +
                ", userOid='" + userOid + '\'' +
                ", organizationOid='" + organizationOid + '\'' +
                ", from='" + from + '\'' +
                ", sender='" + sender + '\'' +
                ", attachments=" + attachments +
                ", createDate=" + createDate +
                '}';
    }
}
