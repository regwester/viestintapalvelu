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
package fi.vm.sade.viestintapalvelu.document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;

public class PdfDocument {
    private final String language;
    private AddressLabel addressLabel;
    private byte[] frontPage;
    private byte[] attachment;
    private final List<byte[]> contents;

    public PdfDocument(AddressLabel addressLabel) {
        this(addressLabel, null, null, null);
    }

    public PdfDocument(
            AddressLabel addressLabel,
            String language,
            byte[] frontPage,
            byte[] attachment,
            byte[]... contents) {
        this.addressLabel = addressLabel;
        this.language = language;
        this.frontPage = frontPage;
        this.attachment = attachment;
        this.contents = new ArrayList<>();
        Collections.addAll(this.contents, Optional.ofNullable(contents).orElse(new byte[][] {}));
    }

    public AddressLabel getAddressLabel() {
        return addressLabel;
    }

    public int getContentSize() {
        return (contents != null) ? contents.size() : -1;
    }

    public InputStream getContentStream(int index) {
        return new ByteArrayInputStream(contents.get(index));
    }

    public void addContent(byte[] content) {
        contents.add(content);
    }

    public InputStream getFrontPage() {
        if (frontPage != null) {
            return new ByteArrayInputStream(frontPage);
        }
        return null;
    }

    public InputStream getAttachment() {
        // There are no attachments in e.g. koekutsukirje
        if (attachment != null) {
            return new ByteArrayInputStream(attachment);
        }
        return null;

    }

    public String getLanguage() {
        return language;
    }
}
