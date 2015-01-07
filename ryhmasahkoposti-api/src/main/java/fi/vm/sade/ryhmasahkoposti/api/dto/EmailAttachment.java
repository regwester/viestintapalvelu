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
package fi.vm.sade.ryhmasahkoposti.api.dto;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

public class EmailAttachment {

    private byte[] data;
    private String name;
    private String contentType;

    public EmailAttachment() {
    }

    public EmailAttachment(String attachmentFile) {
        name = attachmentFile;
        DataSource ds = new FileDataSource(attachmentFile);
        contentType = ds.getContentType();
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EmailAttachment [" + "name=" + name + ", contentType=" + contentType + "]";
    }
}
