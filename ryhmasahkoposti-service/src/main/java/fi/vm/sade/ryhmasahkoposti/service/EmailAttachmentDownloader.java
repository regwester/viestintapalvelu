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
package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;

/**
 * Interface for components implementing a handler for source service specific
 * attachment downloads.
 *
 * An email message may contain special replacements that refer to URI (or URIs)
 * that identify the attachment within the source service. The URI should not depend
 * on the actual location of the service.
 *
 * @see #isApplicableForUri(String) which is called to identify the implementation
 * of EmailAttachmentDownloader to check if it is cabable of handling given URIs.
 * (The user side of this interface should be kept unaware of the meanings of the URIs)
 *
 * Any two implmentations of this interface MUST NOT be applicable for the same URI.
 * Source services should therefore use differing URI schemes to specify their URIs.
 * (E.g. {@link fi.vm.sade.ryhmasahkoposti.service.impl.ViestintapalveluEmailAttachmentDownloaderImpl]
 * is applicable for URIs with viestinta://-scheme)
 *
 * @see #download(String) may be called multiple times for the same URI and should
 * return the same result. If source service has no further use for the attachments,
 * the downloaded URIs may be removed in patch call:
 * @see #reportDownloaded(java.util.List)
 *
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 14:58
 */
public interface EmailAttachmentDownloader {

    /**
     * @param uri
     * @return true iff this downloader is capable of handling given URIs (e.g. by scheme)
     */
    boolean isApplicableForUri(String uri);

    /**
     * @param uri given for the attachment by the source system (NOTE: not an URL but a system specific identifier)
     * @return the downloaded email attachment or null if not found by URI
     * @throws Exception if problem encountered when trying to download the attachment, e.g. an IOException
     * or a malformed URI
     */
    EmailAttachment download(String uri);

    /**
     * No-throw..
     *
     * @param uris list of URIs that were successfully downloaded and could be removed from the source service
     */
    void reportDownloaded(List<String> uris);

}
