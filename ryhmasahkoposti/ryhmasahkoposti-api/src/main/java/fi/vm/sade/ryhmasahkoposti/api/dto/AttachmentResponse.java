package fi.vm.sade.ryhmasahkoposti.api.dto;

public class AttachmentResponse {

    private String uuid;
    private String fileName;
    private String contentType;
    private int fileSize;
    
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public int getFileSize() {
        return fileSize;
    }
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
    @Override
    public String toString() {
        return "AttachmentResponse [uuid=" + uuid + ", fileName=" + fileName
                + ", contentType=" + contentType + ", fileSize=" + fileSize
                + "]";
    }
}
