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

package fi.vm.sade.viestintapalvelu.letter;

import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.letter.dto.LanguageCodeOptionsDto;

/**
 * User: ratamaa
 * Date: 30.9.2014
 * Time: 12:23
 */
public interface LetterEmailService {

    /**
     * @param letterBatchId id of the LetterBatch to send email(s) from
     *                      (multiple if recipients have multiple languages)
     * @throws javax.ws.rs.NotFoundException if LetterBatch not found or template not found by id
     * @throws java.lang.IllegalStateException if LetterBatch does not have templateId or processing already started
     */
    void sendEmail(long letterBatchId);

    /**
     * @param letterBatchId
     * @return language options for the given letter batch
     */
    LanguageCodeOptionsDto getLanguageCodeOptions(long letterBatchId);

    /**
     * @param letterBatchId id of the LetterBatch to get email preview from
     * @param languageCode the language to preview (if not given, Template's language will be used)
     * @return the preview of one of the emails in the message
     * @throws javax.ws.rs.NotFoundException if LetterBatch not found or template not found by id or no recipients
     *          with email that are processed
     * @throws java.lang.IllegalStateException if LetterBatch does not have templateId
     */
    String getPreview(Long letterBatchId, Optional<String> languageCode);
}
