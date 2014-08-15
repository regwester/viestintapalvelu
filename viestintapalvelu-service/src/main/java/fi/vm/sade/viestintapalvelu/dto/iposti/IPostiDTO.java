package fi.vm.sade.viestintapalvelu.dto.iposti;

import java.io.Serializable;
import java.util.Date;

public class IPostiDTO implements Serializable {
    private static final long serialVersionUID = -4256107205893408283L;
    private Long id;
    private String contentName;
    private byte[] content;
    private String contentType;
    private Date sentDate;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getContentName() {
        return contentName;
    }
    
    public void setContentName(String contentName) {
        this.contentName = contentName;
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

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
}
