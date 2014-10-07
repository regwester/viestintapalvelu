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

package fi.vm.sade.viestintapalvelu.attachment;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.viestintapalvelu.attachment.dto.LetterReceiverLEtterAttachmentSaveDto;

/**
 * User: ratamaa
 * Date: 29.9.2014
 * Time: 17:38
 */
public interface AttachmentService {

    /**
     * @param dto to save
     * @return id of the saved LetterReceiverLetterAttachment
     */
    long saveReceiverAttachment(LetterReceiverLEtterAttachmentSaveDto dto);

    /**
     * @param letterReceiverLetterAttachmentId the id of the LetterReceiverLetterAttachment to be removed
     */
    void markLetterReceiverAttachmentDownloaded(Long letterReceiverLetterAttachmentId);

    /**
     * @param letterReceiverLetterAttachmentId
     * @return the attachment DTO for the given receiver attachment
     */
    EmailAttachment getLetterAttachment(Long letterReceiverLetterAttachmentId);
}
