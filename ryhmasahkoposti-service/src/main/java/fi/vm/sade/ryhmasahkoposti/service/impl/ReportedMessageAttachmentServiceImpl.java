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

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.dao.ReportedAttachmentDAO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedMessageAttachmentDAO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedRecipientDAO;
import fi.vm.sade.ryhmasahkoposti.model.*;
import fi.vm.sade.ryhmasahkoposti.service.ReportedMessageAttachmentService;
import fi.vm.sade.ryhmasahkoposti.service.dto.ReportedRecipientAttachmentSaveDto;

@Service
public class ReportedMessageAttachmentServiceImpl implements ReportedMessageAttachmentService {
    private ReportedMessageAttachmentDAO reportedMessageAttachmentDAO;
    private ReportedAttachmentDAO reportedAttachmentDAO;
    private ReportedRecipientDAO reportedRecipientDAO;

    @Autowired
    public ReportedMessageAttachmentServiceImpl(ReportedMessageAttachmentDAO reportedMessageAttachmentDAO, ReportedAttachmentDAO reportedAttachmentDAO,
            ReportedRecipientDAO reportedRecipientDAO) {
        this.reportedMessageAttachmentDAO = reportedMessageAttachmentDAO;
        this.reportedAttachmentDAO = reportedAttachmentDAO;
        this.reportedRecipientDAO = reportedRecipientDAO;
    }

    @Override
    @Transactional
    public void saveReportedMessageAttachments(ReportedMessage reportedMessage, List<ReportedAttachment> reportedAttachments) {
        for (ReportedAttachment reportedAttachment : reportedAttachments) {
            ReportedMessageAttachment reportedMessageAttachment = new ReportedMessageAttachment();
            reportedMessageAttachment.setReportedMessage(reportedMessage);
            reportedMessageAttachment.setReportedAttachmentID(reportedAttachment.getId());
            reportedMessageAttachment.setTimestamp(new Date());

            reportedMessageAttachmentDAO.insert(reportedMessageAttachment);
        }
    }

    @Override
    @Transactional
    public void saveReportedRecipientAttachments(ReportedRecipient emailRecipient, List<ReportedAttachment> reportedAttachments) {
        for (ReportedAttachment reportedAttachment : reportedAttachments) {
            ReportedMessageRecipientAttachment recipientAttachment = new ReportedMessageRecipientAttachment();
            recipientAttachment.setRecipient(emailRecipient);
            recipientAttachment.setAttachment(reportedAttachment);
            recipientAttachment.setTimestamp(new Date());

            reportedAttachmentDAO.insert(recipientAttachment);
        }
    }

    @Override
    @Transactional
    public long saveReportedRecipientAttachment(ReportedRecipientAttachmentSaveDto attachmentSaveDto) {
        ReportedAttachment attachment = new ReportedAttachment();
        attachment.setAttachment(attachmentSaveDto.getAttachment());
        attachment.setContentType(attachmentSaveDto.getContentType());
        attachment.setAttachmentName(attachmentSaveDto.getAttachmentName());
        reportedAttachmentDAO.insert(attachment);

        ReportedMessageRecipientAttachment recipientAttachment = new ReportedMessageRecipientAttachment();
        recipientAttachment.setAttachment(attachment);
        recipientAttachment.setRecipient(reportedRecipientDAO.read(attachmentSaveDto.getReportedRecipientId()));
        reportedAttachmentDAO.insertAndFlush(recipientAttachment);
        return recipientAttachment.getId();
    }

}
