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

package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.AttachmentComponent;
import fi.vm.sade.ryhmasahkoposti.service.EmailAttachmentDownloader;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 14:59
 */
@Component
public class ViestintapalveluEmailAttachmentDownloaderImpl implements EmailAttachmentDownloader {
    public static final String URI_PREFIX = "viestinta://";

    @Autowired
    private AttachmentComponent attachmentComponent;

    @Override
    public boolean isApplicableForUri(String uri) {
        return uri.startsWith(URI_PREFIX);
    }

    @Override
    public EmailAttachment download(String uri) throws Exception {
        if (!isApplicableForUri(uri)) {
            throw new IllegalArgumentException("ViestintapalveluEmailAttachmentDownloaderImpl can't handle URI="+uri);
        }
        return attachmentComponent.getEmailAttachmentByUri(uri);
    }

    @Override
    public void reportDownloaded(List<String> uris) {
        attachmentComponent.markDownloaded(uris);
    }
}
