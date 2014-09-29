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

package fi.vm.sade.viestintapalvelu.attachment.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.constants.SecurityConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterAttachmentDAO;
import fi.vm.sade.viestintapalvelu.attachment.AttachmentResource;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetterAttachment;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 15:45
 */
@Component
@PreAuthorize(SecurityConstants.USER_IS_AUTHENTICATED)
public class AttachmentResourceImpl implements AttachmentResource {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private LetterReceiverLetterAttachmentDAO letterReceiverLetterAttachmentDAO;

    @Autowired
    public AttachmentResourceImpl(LetterReceiverLetterAttachmentDAO letterReceiverLetterAttachmentDAO) {
        this.letterReceiverLetterAttachmentDAO = letterReceiverLetterAttachmentDAO;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize(SecurityConstants.SYSTEM_ACCOUNT_ATTACHMENT_DOWNLOAD)
    public EmailAttachment downloadByUri(String uriStr) {
        AttachmentUri uri = new AttachmentUri(uriStr);
        switch (uri.getType()) {
            case LetterReceiverLetterAttachment:
                return getLetterAttachment(uri.getLongParameter(0));
        default: return null;
        }
    }

    private EmailAttachment getLetterAttachment(Long attachmentId) {
        LetterReceiverLetterAttachment letter = letterReceiverLetterAttachmentDAO.read(attachmentId);
        if (letter == null) {
            return null;
        }
        EmailAttachment attachment = new EmailAttachment();
        attachment.setName(letter.getName());
        attachment.setContentType(letter.getContentType());
        attachment.setData(letter.getContents());
        return attachment;
    }

    @Override
    @Transactional
    @PreAuthorize(SecurityConstants.SYSTEM_ACCOUNT_ATTACHMENT_DOWNLOAD)
    public void deleteByUris(List<String> uris) {
        for (String uriStr : uris) {
            AttachmentUri uri = new AttachmentUri(uriStr);
            switch (uri.getType()) {
                case LetterReceiverLetterAttachment:
                    markLetterReceiverAttachmentDownloaded(uri.getLongParameter(0));
                    return;
                default:
                    // Continue to next one (not implemented / ignored):
                    logger.error("Unimplemented URI type: " + uri.getType());
                    break;
            }
        }
    }

    private void markLetterReceiverAttachmentDownloaded(Long attachmentId) {
        LetterReceiverLetterAttachment attachment = letterReceiverLetterAttachmentDAO.read(attachmentId);
        if (attachment != null) {
            letterReceiverLetterAttachmentDAO.remove(attachment);
        }
    }

}
