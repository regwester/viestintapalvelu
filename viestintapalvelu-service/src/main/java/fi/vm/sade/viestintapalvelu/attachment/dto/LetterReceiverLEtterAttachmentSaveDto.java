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
package fi.vm.sade.viestintapalvelu.attachment.dto;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 29.9.2014
 * Time: 17:39
 */
public class LetterReceiverLEtterAttachmentSaveDto implements Serializable {
    private Long letterReceiverLetterId;
    private String contentType;
    private byte[] contents;
    private String name;

    public Long getLetterReceiverLetterId() {
        return letterReceiverLetterId;
    }

    public void setLetterReceiverLetterId(Long letterReceiverLetterId) {
        this.letterReceiverLetterId = letterReceiverLetterId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
