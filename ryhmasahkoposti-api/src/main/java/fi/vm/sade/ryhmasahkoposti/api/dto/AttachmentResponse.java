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
package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.HashMap;
import java.util.Map;

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
    
    public Map<String, String> toMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("uuid", this.uuid);
        result.put("fileName", fileName);
        result.put("contentType", contentType);
        result.put("fileSize", ""+fileSize);
        return result;
    }
    
    @Override
    public String toString() {
        return "AttachmentResponse [uuid=" + uuid + ", fileName=" + fileName
                + ", contentType=" + contentType + ", fileSize=" + fileSize
                + "]";
    }
}
