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
package fi.vm.sade.viestintapalvelu.letter;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author migar1
 *
 */
public class LetterContent {

    @ApiModelProperty(value = "Lähetetyn kirjeen sisältö")
    private byte[] content;

    @ApiModelProperty(value = "Lähetetyn kirjeen tyyppi (application/pdf, application/zip)")
    private String contentType = "";

    @ApiModelProperty(value = "Aikaleima")
    private Date timestamp;

    public LetterContent() {
    }

    public LetterContent(byte[] content, String contentType, Date timestamp) {
        super();
        this.content = content;
        this.contentType = contentType;
        this.timestamp = timestamp;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
