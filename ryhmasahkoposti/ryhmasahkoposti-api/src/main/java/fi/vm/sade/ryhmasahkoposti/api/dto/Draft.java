package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.joda.time.DateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Draft {
    private String from;
    private String sender;
    private String replyTo;
    private String subject;
    private String body;
    private String organizationOid;
    private List<AttachmentResponse> attachInfo = new LinkedList<AttachmentResponse>();
    private boolean isHtml;
    //Pattern conforms to ISO 8601 ( http://en.wikipedia.org/wiki/ISO_8601 )
    //Example 1989-08-14T15:20:50Z
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    private DateTime createDate = new DateTime(); //set the default value to now
    
    public Draft() {}
    
    private Draft(Builder builder) {
        this.from = builder.from;
        this.sender = builder.sender;
        this.replyTo = builder.replyTo;
        this.subject = builder.subject;
        this.body = builder.body;
        this.organizationOid = builder.organizationOid;
        this.attachInfo = builder.attachInfo;
        this.isHtml = builder.isHtml;
        this.createDate = builder.createDate;
    }

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
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
    public String getOrganizationOid() {
        return organizationOid;
    }
    public void setOrganizationOid(String organizationOid) {
        this.organizationOid = organizationOid;
    }
    public List<AttachmentResponse> getAttachInfo() {
        return attachInfo;
    }
    public void setAttachInfo(List<AttachmentResponse> attachInfo) {
        this.attachInfo = attachInfo;
    }
    public void addAttachment(AttachmentResponse attachment) {
        this.attachInfo.add(attachment);
    }
    public boolean isHtml() {
        return isHtml;
    }
    public void setHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }
    public DateTime getCreateDate() {
        return createDate;
    }
    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }

    public static class Builder {
        private String from;
        private String sender;
        private String replyTo;
        private String subject;
        private String body;
        private String organizationOid;
        private List<AttachmentResponse> attachInfo = new ArrayList<AttachmentResponse>();
        private boolean isHtml;
        private DateTime createDate;
        
        public Builder() {}
        
        public Builder from(String from) {
            this.from = from;
            return this;
        }
        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }
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
        public Builder organizationOid(String oid) {
            this.organizationOid = oid;
            return this;
        }
        public Builder addAttachment(AttachmentResponse a) {
            this.attachInfo.add(a);
            return this;
        }
        public Builder setAttachments(List<AttachmentResponse> attachInfo) {
            this.attachInfo = attachInfo;
            return this;
        }
        public Builder isHtml(boolean isHtml) {
            this.isHtml = isHtml;
            return this;
        }
        public Builder createDate(DateTime date) {
            this.createDate = date;
            return this;
        }
        public Draft build() {
            return new Draft(this);
        }
        
    }

    @Override
    public String toString() {
        return "Draft [replyTo=" + replyTo + ", subject=" + subject + ", body=" + body + ", organizationOid=" + organizationOid
                + ", attachInfo=" + attachInfo + ", isHtml=" + isHtml + ", createDate=" + createDate + "]";
    }

}
