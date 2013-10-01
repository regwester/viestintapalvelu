package fi.vm.sade.viestintapalvelu.download;

import java.util.Date;

public class Download {
    private final String contentType;
    private final String filename;
    private final byte[] binaryDocument;
    private final Date timestamp;

    public Download(String contentType, String filename, byte[] binaryDocument) {
        this.contentType = contentType;
        this.filename = filename;
        this.binaryDocument = binaryDocument;
        this.timestamp = new Date();
    }

    public Date getTimestamp() {
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
