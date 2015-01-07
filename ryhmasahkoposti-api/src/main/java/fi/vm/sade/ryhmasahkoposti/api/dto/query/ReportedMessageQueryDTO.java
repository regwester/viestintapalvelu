/**
 * Copyright (c) 2012 The Finnish Board of Education - Opetushallitus
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
package fi.vm.sade.ryhmasahkoposti.api.dto.query;

import java.util.List;

public class ReportedMessageQueryDTO {
    private String searchArgument;
    private String organizationOid;
    private List<String> organizationOids;
    private ReportedRecipientQueryDTO reportedRecipientQuery;

    public String getSearchArgument() {
        return searchArgument;
    }

    public void setSearchArgument(String searchArgument) {
        this.searchArgument = searchArgument;
    }

    public String getOrganizationOid() {
        return organizationOid;
    }

    public void setOrganizationOid(String organizationOid) {
        this.organizationOid = organizationOid;
    }

    public ReportedRecipientQueryDTO getReportedRecipientQueryDTO() {
        return reportedRecipientQuery;
    }

    public void setReportedRecipientQueryDTO(ReportedRecipientQueryDTO reportedRecipientQuery) {
        this.reportedRecipientQuery = reportedRecipientQuery;
    }

    public List<String> getOrganizationOids() {
        return organizationOids;
    }

    public void setOrganizationOids(List<String> organizationOids) {
        this.organizationOids = organizationOids;
    }
}
