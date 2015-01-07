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

public class EmailResponse {
    private EmailRecipient header;
    private String status;
    private String subject;
    private String nbrOfAttachements;

    public EmailResponse() {
        status = "";
        subject = "";
    }

    public EmailResponse(String status, String subject) {
        super();
        this.header = new EmailRecipient("");
        this.status = status;
        this.subject = subject;
        this.nbrOfAttachements = "0";
    }

    public EmailResponse(EmailRecipient header, String status, String subject, String nbrOfAttachements) {
        super();
        this.header = header;
        this.status = status;
        this.subject = subject;
        this.nbrOfAttachements = nbrOfAttachements;
    }

    public EmailRecipient getHeader() {
        return header;
    }

    public String getStatus() {
        return status;
    }

    public String getSubject() {
        return subject;
    }

    public String getNbrOfAttachements() {
        return nbrOfAttachements;
    }

    @Override
    public String toString() {
        return "EmailResponse [status=" + status + ", subject=" + subject + ", nbrOfAttachements=" + nbrOfAttachements + "]";
    }
}
