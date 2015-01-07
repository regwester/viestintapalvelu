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

import java.io.Serializable;
import java.util.Date;

public class SendingStatusDTO implements Serializable {
    private static final long serialVersionUID = 6159889196702219857L;

    private Long messageID;
    private Date sendingStarted;
    private Date sendingEnded;
    private Long numberOfRecipients;
    private Long numberOfSuccessfulSendings;
    private Long numberOfFailedSendings;

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public Date getSendingStarted() {
        return sendingStarted;
    }

    public void setSendingStarted(Date sendingStarted) {
        this.sendingStarted = sendingStarted;
    }

    public Date getSendingEnded() {
        return sendingEnded;
    }

    public void setSendingEnded(Date sendingEnded) {
        this.sendingEnded = sendingEnded;
    }

    public Long getNumberOfRecipients() {
        return numberOfRecipients;
    }

    public void setNumberOfRecipients(Long numberOfRecipients) {
        this.numberOfRecipients = numberOfRecipients;
    }

    public Long getNumberOfSuccessfulSendings() {
        return numberOfSuccessfulSendings;
    }

    public void setNumberOfSuccessfulSendings(Long numberOfSuccessfulSendings) {
        this.numberOfSuccessfulSendings = numberOfSuccessfulSendings;
    }

    public Long getNumberOfFailedSendings() {
        return numberOfFailedSendings;
    }

    public void setNumberOfFailedSendings(Long numberOfFailedSendings) {
        this.numberOfFailedSendings = numberOfFailedSendings;
    }
}
