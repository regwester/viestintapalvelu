package fi.vm.sade.viestintapalvelu.dto.letter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import fi.vm.sade.viestintapalvelu.dto.iposti.IPostiDTO;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.template.Template;

public class LetterBatchReportDTO implements Serializable {
    private static final long serialVersionUID = 7920118110257531390L;
    private Long letterBatchID;
    private Template template;
    private String applicationPeriod;
    private String fetchTarget;
    private String creatorName;
    private String tag;    
    private boolean deliveryTypeIPosti;
    private Date timestamp;
    private List<LetterReceiverDTO> letterReceivers;
    private Long numberOfReceivers; 
    private List<IPostiDTO> iPostis;
    private String organisaatioOid;
    private String status;
    private Long templateId;
    private String receiverName;
    private Long receiverLetterId;
    private String templateName;

    public LetterBatchReportDTO() {
    }

    public LetterBatchReportDTO(Long letterBatchID, Long templateId, String templateName,
                                String applicationPeriod, String fetchTarget,
                                String tag, boolean deliveryTypeIPosti, Date timestamp,
                                String organisaatioOid, LetterBatch.Status status) {
        this.letterBatchID = letterBatchID;
        this.templateId = templateId;
        this.templateName = templateName;
        this.applicationPeriod = applicationPeriod;
        this.fetchTarget = fetchTarget;
        this.tag = tag;
        this.deliveryTypeIPosti = deliveryTypeIPosti;
        this.timestamp = timestamp;
        this.organisaatioOid = organisaatioOid;
        this.status = status == null ? null : status.name();
    }

    public LetterBatchReportDTO(Long letterBatchID, Long templateId, String templateName,
                                String applicationPeriod, String fetchTarget,
                                String tag, boolean deliveryTypeIPosti, Date timestamp,
                                String organisaatioOid, LetterBatch.Status status,
                                Long receiverLetterId, String receiverName) {
        this.letterBatchID = letterBatchID;
        this.templateId = templateId;
        this.templateName = templateName;
        this.applicationPeriod = applicationPeriod;
        this.fetchTarget = fetchTarget;
        this.tag = tag;
        this.deliveryTypeIPosti = deliveryTypeIPosti;
        this.timestamp = timestamp;
        this.organisaatioOid = organisaatioOid;
        this.status = status == null ? null : status.name();
        this.receiverName = receiverName;
        this.receiverLetterId = receiverLetterId;
    }

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
    
    public String getFetchTarget() {
        return fetchTarget;
    }
    
    public void setFetchTarget(String fetchTarget) {
        this.fetchTarget = fetchTarget;
    }
    
    public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

    public Long getNumberOfReceivers() {
        return numberOfReceivers;
    }

    public void setNumberOfReceivers(Long numberOfReceivers) {
        this.numberOfReceivers = numberOfReceivers;
    }

    public List<IPostiDTO> getiPostis() {
        return iPostis;
    }

    public void setiPostis(List<IPostiDTO> iPostis) {
        this.iPostis = iPostis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrganisaatioOid(String organisaatioOid) {
        this.organisaatioOid = organisaatioOid;
    }

    public String getOrganisaatioOid() {
        return organisaatioOid;
    }

    public Long getReceiverLetterId() {
        return receiverLetterId;
    }

    public void setReceiverLetterId(Long receiverLetterId) {
        this.receiverLetterId = receiverLetterId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
