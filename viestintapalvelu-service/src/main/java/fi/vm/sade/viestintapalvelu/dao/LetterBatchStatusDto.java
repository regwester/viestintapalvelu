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

package fi.vm.sade.viestintapalvelu.dao;

import fi.vm.sade.viestintapalvelu.model.LetterBatch;

/**
 * User: ratamaa
 * Date: 17.9.2014
 * Time: 16:12
 */
public class LetterBatchStatusDto {

    private Long letterBatchId;
    private Integer sent;
    private Integer total;

    /**
     * A free form text field for telling a more verbose status of the batch.
     * For example, if the batch processing status is in error, you might want
     * to indicate here what the actual error is.
     */
    private String message;
    private LetterBatch.Status processingStatus;

    public LetterBatchStatusDto(Long letterBatchId, Number sent, Number total, LetterBatch.Status status) {
        this.letterBatchId = letterBatchId;
        this.setSent(sent);
        this.setTotal(total);
        this.processingStatus = status;

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

    /**
     * {@link fi.vm.sade.viestintapalvelu.dao.LetterBatchStatusDto#message}
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link fi.vm.sade.viestintapalvelu.dao.LetterBatchStatusDto#message}
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public LetterBatch.Status getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(LetterBatch.Status processingStatus) {
        this.processingStatus = processingStatus;
    }
}
