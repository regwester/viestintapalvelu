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

import java.util.ArrayList;
import java.util.List;

public class ReportedMessageDTO extends EmailMessageDTO {
    private String senderName;
    private List<EmailRecipientDTO> emailRecipients;
    private SendingStatusDTO sendingStatus;
    private String statusReport;
    private String sendingReport;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public List<EmailRecipientDTO> getEmailRecipients() {
        return emailRecipients;
    }

    public void setEmailRecipients(List<EmailRecipientDTO> emailRecipients) {
        this.emailRecipients = emailRecipients;
    }

    public SendingStatusDTO getSendingStatus() {
        return sendingStatus;
    }

    public void setSendingStatus(SendingStatusDTO sendingStatus) {
        this.sendingStatus = sendingStatus;
    }

    public String getStatusReport() {
        return statusReport;
    }

    public void setStatusReport(String statusReport) {
        this.statusReport = statusReport;
    }

    public String getSendingReport() {
        return sendingReport;
    }

    public void setSendingReport(String sendingReport) {
        this.sendingReport = sendingReport;
    }

    /*
    Retract access tokens from private/personal URLs in the message body so that these can be presented in the officer UI
     */
    @Override
    public String getBody() {
        List<String> regexps = new ArrayList<>();
        regexps.add("(https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]/(token|emailregister)/)[A-Za-z0-9]+");

        String result = super.getBody();
        for (String regexp : regexps) {
            result = result.replaceAll(regexp, "$1[RETRACTED]");
        }
        return result;
    }
}
