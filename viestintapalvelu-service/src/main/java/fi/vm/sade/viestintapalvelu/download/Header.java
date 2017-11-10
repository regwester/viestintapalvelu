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

import org.apache.commons.lang.time.FastDateFormat;

import java.time.ZonedDateTime;

/**
 * 
 * @author Jussi Jartamo
 * 
 * @Deprecated Viestintapalvelun tiedostolistausta ja tiedostocachea ei kayteta
 *             tuotannossa. Tiedostot siirretaan valintojen dokumenttipalveluun
 *             tilapaistaltiointiin ja suojaukseen.
 */
@Deprecated
public class Header implements Comparable<Header> {
    private static final FastDateFormat FORMATTER = FastDateFormat.getInstance("dd.MM.yyyy HH.mm");
    private final String contentType;
    private final String filename;
    private final String documentId;
    private final long size;
    private final ZonedDateTime createdAt;

    public int compareTo(Header o) {
        return o.createdAt.compareTo(createdAt);
    }

    public Header(String contentType, String filename, String documentId, long size, ZonedDateTime createdAt) {
        this.contentType = contentType;
        this.filename = filename;
        this.documentId = documentId;
        this.size = size;
        this.createdAt = createdAt;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getFilename() {
        return filename;
    }

    public String getSize() {
        return humanReadableByteCount(size, true);
    }

    public String getCreatedAt() {
        return FORMATTER.format(createdAt);
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
