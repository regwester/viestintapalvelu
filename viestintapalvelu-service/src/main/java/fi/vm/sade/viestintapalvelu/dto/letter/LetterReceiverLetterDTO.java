package fi.vm.sade.viestintapalvelu.dto.letter;

import java.io.Serializable;

public class LetterReceiverLetterDTO implements Serializable {
    private static final long serialVersionUID = -8935604452908089376L;
    private Long id;
    private String templateName;
    private byte[] letter;
    private String contentType;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public byte[] getLetter() {
        return letter;
    }
    
    public void setLetter(byte[] letter) {
        this.letter = letter;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }   
}
