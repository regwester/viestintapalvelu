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

import java.util.Date;
import java.util.List;

public class EmailMessageDTO extends EmailMessage {
    private Long messageID;
    private Date startTime;
    private Date endTime;
    private boolean isVirusChecked;
    private boolean isInfected;
    private String type;
    private List<ReplacementDTO> messageReplacements;

    public boolean isVirusChecked() {
        return isVirusChecked;
    }

    public void setVirusChecked(boolean virusChecked) {
        this.isVirusChecked = virusChecked;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean isInfected) {
        this.isInfected = isInfected;
    }

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ReplacementDTO> getMessageReplacements() {
        return messageReplacements;
    }

    public void setMessageReplacements(List<ReplacementDTO> messageReplacements) {
        this.messageReplacements = messageReplacements;
    }

}
