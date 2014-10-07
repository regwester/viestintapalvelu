/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.viestintapalvelu.dao.dto;

import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterBatchProcessingError;

import java.util.List;

/**
 * User: ratamaa
 * Date: 17.9.2014
 * Time: 16:12
 */
public class LetterBatchStatusDto {
    private Long letterBatchId;
    private Integer sent;
    private Integer total;
    private Integer emailsProcessed;
    private List<LetterBatchStatusErrorDto> errors;
    private LetterBatch.Status status;

    public LetterBatchStatusDto(Long letterBatchId,
                                Number sent, Number total,
                                LetterBatch.Status status,
                                Number emailsProcessed) {
        this.letterBatchId = letterBatchId;
        this.setSent(sent);
        this.setTotal(total);
        this.status = status;
        this.setEmailsProcessed(emailsProcessed);
    }

    public Long getLetterBatchId() {
        return letterBatchId;
    }

    public void setLetterBatchId(Long letterBatchId) {
        this.letterBatchId = letterBatchId;
    }

    public Integer getSent() {
        return sent;
    }

    public void setSent(Number sent) {
        this.sent = (sent != null) ? sent.intValue() : null;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = (total != null) ? total.intValue() : null;
    }

    public List<LetterBatchStatusErrorDto> getErrors() {
        return errors;
    }

    public void setErrors(List<LetterBatchStatusErrorDto> errors) {
        this.errors = errors;
    }

    public LetterBatch.Status getStatus() {
        return status;
    }

    public void setStatus(LetterBatch.Status status) {
        this.status = status;
    }

    public Integer getEmailsProcessed() {
        return emailsProcessed;
    }

    public void setEmailsProcessed(Number emailsProcessed) {
        this.emailsProcessed = emailsProcessed != null ? emailsProcessed.intValue() : null;
    }

    public boolean isEmailReviewable() {
        return this.emailsProcessed != null && this.emailsProcessed > 0;
    }
}
