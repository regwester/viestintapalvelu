/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.viestintapalvelu.dto.letter;

import java.io.Serializable;
import java.util.List;

import fi.vm.sade.viestintapalvelu.dto.OrganizationDTO;

public class LetterBatchesReportDTO implements Serializable {
    private static final long serialVersionUID = 2903168349557105947L;
    private List<OrganizationDTO> organizations;
    private Integer selectedOrganization;
    private Long numberOfLetterBatches;
    private Long maxNumber;
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

    public Long getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(Long maxNumber) {
        this.maxNumber = maxNumber;
    }
}
