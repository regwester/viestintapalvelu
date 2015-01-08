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
package fi.vm.sade.viestintapalvelu.asiontitili.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 27.10.2014
 * Time: 9:58
 */
public class AsiointitiliAsyncResponseDto implements Serializable {
    private static final long serialVersionUID = -288624984771979673L;

    private String id;
    private int statusCode;
    private String description;
    private String messageId;
    private List<AsiointitiliReceiverStatusDto> receiverStatuses = new ArrayList<AsiointitiliReceiverStatusDto>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public List<AsiointitiliReceiverStatusDto> getReceiverStatuses() {
        return receiverStatuses;
    }

    public void setReceiverStatuses(List<AsiointitiliReceiverStatusDto> receiverStatuses) {
        this.receiverStatuses = receiverStatuses;
    }
}
