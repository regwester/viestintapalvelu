package fi.vm.sade.viestintapalvelu.dto.letter;

import java.io.Serializable;
import java.util.List;

import fi.vm.sade.viestintapalvelu.dto.OrganizationDTO;


public class LetterBatchesReportDTO implements Serializable {
    private static final long serialVersionUID = 2903168349557105947L;
    private List<OrganizationDTO> organizations;
    private Integer selectedOrganization;
    private Long numberOfLetterBatches;
    private List<LetterBatchReportDTO> letterBatchReports;
    
    public List<OrganizationDTO> getOrganizations() {
        return organizations;
    }
    
    public void setOrganizations(List<OrganizationDTO> organizations) {
        this.organizations = organizations;
    }
    
    public Integer getSelectedOrganization() {
        return selectedOrganization;
    }
    
    public void setSelectedOrganization(Integer selectedOrganization) {
        this.selectedOrganization = selectedOrganization;
    }
    
    public Long getNumberOfLetterBatches() {
        return numberOfLetterBatches;
    }
    
    public void setNumberOfLetterBatches(Long numberOfLetterBatches) {
        this.numberOfLetterBatches = numberOfLetterBatches;
    }
    
    public List<LetterBatchReportDTO> getLetterBatchReports() {
        return letterBatchReports;
    }
    
    public void setLetterBatchReports(List<LetterBatchReportDTO> letterBatchReports) {
        this.letterBatchReports = letterBatchReports;
    }
}
