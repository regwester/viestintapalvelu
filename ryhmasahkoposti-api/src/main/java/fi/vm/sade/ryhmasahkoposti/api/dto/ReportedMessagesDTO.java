package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;
import java.util.List;

public class ReportedMessagesDTO implements Serializable {
    private static final long serialVersionUID = -8138474011582310278L;
    private List<OrganizationDTO> organizations;
    private Integer selectedOrganization;
    private List<ReportedMessageDTO> reportedMessages;
    private Long numberOfReportedMessages;
    
    public List<OrganizationDTO> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationDTO> organizations) {
        this.organizations = organizations;
    }

    public List<ReportedMessageDTO> getReportedMessages() {
        return reportedMessages;
    }
    
    public void setReportedMessages(List<ReportedMessageDTO> reportedMessages) {
        this.reportedMessages = reportedMessages;
    }
    
    public Integer getSelectedOrganization() {
        return selectedOrganization;
    }

    public void setSelectedOrganization(Integer selectedOrganization) {
        this.selectedOrganization = selectedOrganization;
    }

    public Long getNumberOfReportedMessages() {
        return numberOfReportedMessages;
    }
    
    public void setNumberOfReportedMessages(Long numberOfReportedMessages) {
        this.numberOfReportedMessages = numberOfReportedMessages;
    }    
}
