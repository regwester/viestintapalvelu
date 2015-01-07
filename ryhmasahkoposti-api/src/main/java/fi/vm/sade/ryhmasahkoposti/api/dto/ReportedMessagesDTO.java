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
