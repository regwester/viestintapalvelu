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

package fi.vm.sade.viestintapalvelu.asiointitili.ws;

import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.MessageTrustDecider;
import org.apache.cxf.transport.http.URLConnectionInfo;
import org.apache.cxf.transport.http.UntrustedURLConnectionIOException;
import org.apache.cxf.transport.https.CertConstraints;

public class IgnorantTrustDecider extends MessageTrustDecider {
    private final CertConstraints certConstraints;
    private final MessageTrustDecider orig;

    public IgnorantTrustDecider(CertConstraints certConstraints,
                             MessageTrustDecider orig) {
        this.certConstraints = certConstraints;
        this.orig = orig;
    }

    @Override
    public void establishTrust(String conduitName, URLConnectionInfo connectionInfo,
                               Message message) throws UntrustedURLConnectionIOException {
    }
}
