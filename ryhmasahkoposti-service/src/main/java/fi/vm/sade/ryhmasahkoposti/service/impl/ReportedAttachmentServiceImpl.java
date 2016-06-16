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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedAttachmentDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageAttachment;
import fi.vm.sade.ryhmasahkoposti.service.ReportedAttachmentService;

@Service
public class ReportedAttachmentServiceImpl implements ReportedAttachmentService {
    private ReportedAttachmentDAO reportedAttachmentDAO;

    @Autowired
    public ReportedAttachmentServiceImpl(ReportedAttachmentDAO reportedAttachmentDAO) {
        this.reportedAttachmentDAO = reportedAttachmentDAO;
    }

    @Override
    public ReportedAttachment getReportedAttachment(Long attachmentID) {
        return reportedAttachmentDAO.read(attachmentID);
    }

    @Override
    public List<ReportedAttachment> getReportedAttachments(Set<ReportedMessageAttachment> reportedMessageAttachments) {
        List<ReportedAttachment> reportedAttachments = new ArrayList<>();

        for (ReportedMessageAttachment reportedMessageAttachment : reportedMessageAttachments) {
            ReportedAttachment liite = reportedAttachmentDAO.read(reportedMessageAttachment.getReportedAttachmentID());
            reportedAttachments.add(liite);
        }

        return reportedAttachments;
    }

    @Override
    public List<ReportedAttachment> getReportedAttachments(List<AttachmentResponse> attachmentResponses) {
        List<ReportedAttachment> reportedAttachments = new ArrayList<>();

        for (AttachmentResponse attachmentResponse : attachmentResponses) {
            Long liitteenID = new Long(attachmentResponse.getUuid());
            ReportedAttachment reportedAttachment = reportedAttachmentDAO.read(liitteenID);
            reportedAttachments.add(reportedAttachment);
        }

        return reportedAttachments;
    }

    @Override
    public Long saveReportedAttachment(ReportedAttachment reportedAttachment) {
        reportedAttachment = reportedAttachmentDAO.insert(reportedAttachment);
        return reportedAttachment.getId();
    }

}
