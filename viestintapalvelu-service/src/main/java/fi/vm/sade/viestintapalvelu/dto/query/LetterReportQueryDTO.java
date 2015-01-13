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
package fi.vm.sade.viestintapalvelu.dto.query;

import java.io.Serializable;
import java.util.List;

import org.joda.time.LocalDate;

public class LetterReportQueryDTO implements Serializable {
    public enum SearchTarget {
        batch, receiver
    }

    private static final long serialVersionUID = -8516467453321234990L;
    private List<String> organizationOids;
    private SearchTarget target = SearchTarget.batch;
    private String letterBatchSearchArgument;
    private String receiverSearchArgument;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String applicationPeriod;

    public List<String> getOrganizationOids() {
        return organizationOids;
    }

    public void setOrganizationOids(List<String> organizationOids) {
        this.organizationOids = organizationOids;
    }

    public String getLetterBatchSearchArgument() {
        return letterBatchSearchArgument;
    }

    public void setLetterBatchSearchArgument(String letterBatchSearchArgument) {
        this.letterBatchSearchArgument = letterBatchSearchArgument;
    }

    public String getReceiverSearchArgument() {
        return receiverSearchArgument;
    }

    public void setReceiverSearchArgument(String receiverSearchArgument) {
        this.receiverSearchArgument = receiverSearchArgument;
    }

    public SearchTarget getTarget() {
        return target;
    }

    public void setTarget(SearchTarget target) {
        this.target = target;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setApplicationPeriod(String applicationPeriod) {
        this.applicationPeriod = applicationPeriod;
    }

    public String getApplicationPeriod() {
        return applicationPeriod;
    }
}
