package fi.vm.sade.viestintapalvelu.download;

public class Header {
    private final String contentType;
    private final String filename;
    private final String documentId;
    private final String createdAt;

    public Header(String contentType, String filename, String documentId, String createdAt) {
        this.contentType = contentType;
        this.filename = filename;
        this.documentId = documentId;
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

    public String getCreatedAt() {
        return createdAt;
    }
}
