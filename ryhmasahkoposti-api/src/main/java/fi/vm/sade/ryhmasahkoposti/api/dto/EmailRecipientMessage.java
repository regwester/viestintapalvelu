/**
 * Copyright (c) 2012 The Finnish Board of Education - Opetushallitus
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

import java.util.List;


public class EmailRecipientMessage extends EmailMessage {
    private boolean isVirusChecked;
    private boolean isInfected;
    private String type;
    private EmailRecipientDTO recipient;
    private List<ReplacementDTO> messageReplacements;

    public List<ReplacementDTO> getMessageReplacements() {
        return messageReplacements;
    }

    public void setMessageReplacements(List<ReplacementDTO> messageReplacements) {
        this.messageReplacements = messageReplacements;
    }

    public void setRecipient(EmailRecipientDTO recipient) {
        this.recipient = recipient;
    }

    public boolean isVirusChecked() {
        return isVirusChecked;
    }

    public void setVirusChecked(boolean isVirusChecked) {
        this.isVirusChecked = isVirusChecked;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean isInfected) {
        this.isInfected = isInfected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EmailRecipientDTO getRecipient() {
        return recipient;
    }

    @Override
    public String toString() {
        return "EmailRecipientMessage [isVirusChecked=" + isVirusChecked + ", isInfected=" + isInfected + ", type=" + type + ", recipient=" + recipient
                + ", messageReplacements=" + messageReplacements + "]"+ super.toString();
    }

}
