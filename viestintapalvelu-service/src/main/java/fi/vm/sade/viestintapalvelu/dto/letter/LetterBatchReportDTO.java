package fi.vm.sade.viestintapalvelu.dto.letter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import fi.vm.sade.viestintapalvelu.dto.iposti.IPostiDTO;
import fi.vm.sade.viestintapalvelu.template.Template;

public class LetterBatchReportDTO implements Serializable {
    private static final long serialVersionUID = 7920118110257531390L;
    private Long letterBatchID;
    private Template template;
    private String applicationPeriod;
    private String fetchTargetName;
    private String tag;    
    private boolean deliveryTypeIPosti;
    private Date timestamp;
    private List<LetterReceiverDTO> letterReceivers;
    private List<IPostiDTO> iPostis;
    
    public Long getLetterBatchID() {
        return letterBatchID;
    }
    
    public void setLetterBatchID(Long letterBatchID) {
        this.letterBatchID = letterBatchID;
    }
    
    public Template getTemplate() {
        return template;
    }
    
    public void setTemplate(Template template) {
        this.template = template;
    }
    
    public String getApplicationPeriod() {
        return applicationPeriod;
    }
    
    public void setApplicationPeriod(String applicationPeriod) {
        this.applicationPeriod = applicationPeriod;
    }
    
    public String getFetchTargetName() {
        return fetchTargetName;
    }
    
    public void setFetchTargetName(String fetchTargetName) {
        this.fetchTargetName = fetchTargetName;
    }
    
    public String getTag() {
        return tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public boolean isDeliveryTypeIPosti() {
        return deliveryTypeIPosti;
    }
    
    public void setDeliveryTypeIPosti(boolean deliveryTypeIPosti) {
        this.deliveryTypeIPosti = deliveryTypeIPosti;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<LetterReceiverDTO> getLetterReceivers() {
        return letterReceivers;
    }

    public void setLetterReceivers(List<LetterReceiverDTO> letterReceivers) {
        this.letterReceivers = letterReceivers;
    }

    public List<IPostiDTO> getiPostis() {
        return iPostis;
    }

    public void setiPostis(List<IPostiDTO> iPostis) {
        this.iPostis = iPostis;
    }
}
