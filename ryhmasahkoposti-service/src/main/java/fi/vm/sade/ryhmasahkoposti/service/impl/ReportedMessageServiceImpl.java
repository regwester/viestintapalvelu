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
package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageDAO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.organisaatio.OrganisaatioService;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.service.ReportedMessageService;

@Service
public class ReportedMessageServiceImpl implements ReportedMessageService {
    private ReportedMessageDAO reportedMessageDAO;

    @Autowired
    private OrganisaatioService organisaatioService;

    @Value("${viestintapalvelu.rekisterinpitajaOID:}")
    private String rootOrganizationOID;

    @Autowired
    public ReportedMessageServiceImpl(ReportedMessageDAO reportedMessageDAO) {
        this.reportedMessageDAO = reportedMessageDAO;
    }

    @Override
    public Long getNumberOfReportedMessages(String organizationOid) {
        List<String> oids = rootOrganizationOID.equals(organizationOid) ? null : organisaatioService.findHierarchyOids(organizationOid);
        return reportedMessageDAO.findNumberOfReportedMessages(oids);
    }

    @Override
    public Long getNumberOfReportedMessages(ReportedMessageQueryDTO query) {
        resolveOrganizationHierarchyOids(query);
        return reportedMessageDAO.findNumberOfReportedMessage(query);
    }

    private void resolveOrganizationHierarchyOids(ReportedMessageQueryDTO query) {
        query.setOrganizationOids(null);
        if (query.getOrganizationOid() != null) {
            if (rootOrganizationOID.equals(query.getOrganizationOid())) {
                query.setOrganizationOid(null);
            } else {
                List<String> oids = organisaatioService.findHierarchyOids(query.getOrganizationOid());
                query.setOrganizationOids(oids);
            }
        }
    }

    @Override
    public List<ReportedMessage> getReportedMessages(String organizationOid, PagingAndSortingDTO pagingAndSorting) {
        List<String> oids = rootOrganizationOID.equals(organizationOid) ? null : organisaatioService.findHierarchyOids(organizationOid);
        return reportedMessageDAO.findByOrganizationOids(oids, pagingAndSorting);
    }

    @Override
    public List<ReportedMessage> getReportedMessages(ReportedMessageQueryDTO query,
                                                     PagingAndSortingDTO pagingAndSorting) {
        resolveOrganizationHierarchyOids(query);
        return reportedMessageDAO.findBySearchCriteria(query, pagingAndSorting);
    }

    @Override
    public List<ReportedMessage> getUserMessages(String senderOid, PagingAndSortingDTO pagingAndSorting) {
        return reportedMessageDAO.findBySenderOid(senderOid, pagingAndSorting);
    }

    @Override
    public List<ReportedMessage> getUserMessages(String senderOid, String process, PagingAndSortingDTO pagingAndSortingDTO) {
        return reportedMessageDAO.findBySenderOidAndProcess(senderOid, process, pagingAndSortingDTO);
    }

    @Override
    public ReportedMessage getReportedMessage(Long id) {
        return reportedMessageDAO.read(id);
    }

    @Override
    public void updateReportedMessage(ReportedMessage reportedMessage) {
        reportedMessageDAO.update(reportedMessage);
    }

    @Override
    public ReportedMessage saveReportedMessage(ReportedMessage reportedMessage) {
        return reportedMessageDAO.insert(reportedMessage);
    }

    public void setOrganisaatioService(OrganisaatioService organisaatioService) {
        this.organisaatioService = organisaatioService;
    }

    public void setRootOrganizationOID(String rootOrganizationOID) {
        this.rootOrganizationOID = rootOrganizationOID;
    }
}
