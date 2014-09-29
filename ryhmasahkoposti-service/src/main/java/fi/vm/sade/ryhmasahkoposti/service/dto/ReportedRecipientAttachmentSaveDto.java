/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.ryhmasahkoposti.service.dto;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 29.9.2014
 * Time: 20:34
 */
public class ReportedRecipientAttachmentSaveDto implements Serializable {
    private Long reportedRecipientId;
    private String attachmentName;
    private byte[] attachment;
    private String contentType;

    public Long getReportedRecipientId() {
        return reportedRecipientId;
    }

    public void setReportedRecipientId(Long reportedRecipientId) {
        this.reportedRecipientId = reportedRecipientId;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
