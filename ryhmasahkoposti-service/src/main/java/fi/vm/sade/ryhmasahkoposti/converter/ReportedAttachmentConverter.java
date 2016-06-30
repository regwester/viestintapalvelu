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
package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.Date;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;

@Component
public class ReportedAttachmentConverter {

    public ReportedAttachment convert(FileItem fileItem) {
        ReportedAttachment liite = new ReportedAttachment();

        String filename = fileItem.getName();
        liite.setAttachmentName(FilenameUtils.getName(filename));
        liite.setContentType(fileItem.getContentType());

        liite.setAttachment(fileItem.get());
        liite.setTimestamp(new Date());

        return liite;
    }

    public ReportedAttachment convert(EmailAttachment emailAttachment) {
        ReportedAttachment attachment = new ReportedAttachment();

        attachment.setAttachmentName(emailAttachment.getName());
        attachment.setContentType(emailAttachment.getContentType());
        attachment.setAttachment(emailAttachment.getData());
        attachment.setTimestamp(new Date());

        return attachment;
    }

    public EmailAttachment convert(ReportedAttachment ra) {
        EmailAttachment attachment = new EmailAttachment();

        attachment.setName(ra.getAttachmentName());
        attachment.setContentType(ra.getContentType());
        attachment.setData(ra.getAttachment());

        return attachment;
    }
}
