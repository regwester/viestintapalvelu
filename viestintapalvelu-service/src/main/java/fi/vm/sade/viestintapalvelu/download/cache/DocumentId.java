package fi.vm.sade.viestintapalvelu.download.cache;

public class DocumentId{

    private String documentId;

    public DocumentId(String documentId) {
        if(documentId == null) {
            this.documentId = "";
        } else {
            this.documentId = documentId;
        }

    }

    public String getDocumentId() {
        return documentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentId that = (DocumentId) o;

        return documentId.equals(that.documentId);
    }

    @Override
    public int hashCode() {
        return documentId.hashCode();
    }
}
