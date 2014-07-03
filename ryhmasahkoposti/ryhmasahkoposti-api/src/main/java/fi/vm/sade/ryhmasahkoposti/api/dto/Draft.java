package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = Draft.Builder.class)
public class Draft {
    
    private String replyTo;
    private String subject;
    private String body;
    private String userOid;
    private Set<EmailAttachment> attachments = new HashSet<EmailAttachment>();
    private boolean isHtml;
    private Date createDate;
    
    public Draft() {}
    
    private Draft(Builder builder) {
        this.replyTo = builder.replyTo;
        this.subject = builder.subject;
        this.body = builder.body;
        this.userOid = builder.userOid;
        this.attachments = builder.attachments;
        this.isHtml = builder.isHtml;
        this.createDate = builder.createDate;
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
    public Set<EmailAttachment> getAttachments() {
        return attachments;
    }
    public void setAttachments(Set<EmailAttachment> attachments) {
        this.attachments = attachments;
    }
    public void addAttachment(EmailAttachment attachment) {
        this.attachments.add(attachment);
    }
    public boolean isHtml() {
        return isHtml;
    }
    public void setHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public static class Builder {
        private String replyTo;
        private String subject;
        private String body;
        private String userOid;
        private Set<EmailAttachment> attachments = new HashSet<EmailAttachment>();
        private boolean isHtml;
        private Date createDate;
        
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
        public Builder addAttachment(EmailAttachment a) {
            this.attachments.add(a);
            return this;
        }
        public Builder setAttachments(Set<EmailAttachment> attachments) {
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
        public Draft build() {
            return new Draft(this);
        }
        
    }

    @Override
    public String toString() {
        return "Draft [replyTo=" + replyTo + ", subject=" + subject + ", body=" + body + ", userOid=" + userOid
                + ", attachments=" + attachments + ", isHtml=" + isHtml + ", createDate=" + createDate + "]";
    }


}
