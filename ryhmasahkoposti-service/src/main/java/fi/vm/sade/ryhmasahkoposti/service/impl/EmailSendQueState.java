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
package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 17:02
 */
public class EmailSendQueState {
    private List<String> downloadedAttachmentUris = new ArrayList<>();
    private boolean saveAttachments = true;

    public void addDownloadedAttachmentUri(String uri) {
        this.downloadedAttachmentUris.add(uri);
    }

    public List<String> getDownloadedAttachmentUris() {
        return downloadedAttachmentUris;
    }

    public boolean isSaveAttachments() {
        return saveAttachments;
    }

    public EmailSendQueState withoutSavingAttachments() {
        this.saveAttachments = false;
        return this;
    }
}
