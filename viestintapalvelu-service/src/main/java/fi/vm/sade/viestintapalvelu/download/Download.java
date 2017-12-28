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
package fi.vm.sade.viestintapalvelu.download;

import java.time.ZonedDateTime;

public class Download {
    private final String contentType;
    private final String filename;
    private final byte[] binaryDocument;
    private final ZonedDateTime timestamp;

    public Download(String contentType, String filename, byte[] binaryDocument) {
        this.contentType = contentType;
        this.filename = filename;
        this.binaryDocument = binaryDocument;
        this.timestamp = ZonedDateTime.now();
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] toByteArray() {
        return binaryDocument;
    }

    public String getContentType() {
        return contentType;
    }
}
