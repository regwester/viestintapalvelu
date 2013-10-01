package fi.vm.sade.viestintapalvelu.download;

import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;

public class Header implements Comparable<Header> {
    private static final FastDateFormat FORMATTER = FastDateFormat.getInstance("dd.MM.yyyy HH.mm");
    private final String contentType;
    private final String filename;
    private final String documentId;
    private final long size;
    private final Date createdAt;

    public int compareTo(Header o) {
        return o.createdAt.compareTo(createdAt);
    }

    public Header(String contentType, String filename, String documentId, long size, Date createdAt) {
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
