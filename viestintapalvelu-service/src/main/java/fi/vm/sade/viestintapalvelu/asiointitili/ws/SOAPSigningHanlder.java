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

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

import org.jcp.xml.dsig.internal.dom.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * User: ratamaa
 * Date: 10.10.2014
 * Time: 15:34
 */
public class SOAPSigningHanlder implements LogicalHandler<LogicalMessageContext> {
    private String keystoreInstance;
    private String keystoreFile;
    private String keystorePw;
    private String keystoreAlias;

    public SOAPSigningHanlder(String keystoreInstance, String keystoreFile, String keystorePw, String keystoreAlias) {
        this.keystoreInstance = keystoreInstance;
        this.keystoreFile = keystoreFile;
        this.keystorePw = keystorePw;
        this.keystoreAlias = keystoreAlias;
    }

    @Override
    public boolean handleMessage(LogicalMessageContext smc) {
        try {
            if ((Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
                Source source = smc.getMessage().getPayload();
                Node root = ((DOMSource) source).getNode();

                XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");

                Reference ref = signatureFactory.newReference("",
                        signatureFactory.newDigestMethod(DigestMethod.SHA1, null),
                            Collections.singletonList(signatureFactory.newTransform(Transform.ENVELOPED,
                                    (TransformParameterSpec) null)), null, null);
                SignedInfo signedInfo = signatureFactory.newSignedInfo(
                        signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
                            (C14NMethodParameterSpec) null), signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                        Collections.singletonList(ref));
                KeyStore keyStore = KeyStore.getInstance(keystoreInstance);
                keyStore.load(new FileInputStream(keystoreFile), keystorePw.toCharArray());
                KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry)
                        keyStore.getEntry(keystoreAlias, new KeyStore.PasswordProtection(keystorePw.toCharArray()));
                X509Certificate cert = (X509Certificate) keyEntry.getCertificate();

                KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
                List<Object> x509Content = new ArrayList<Object>();
                x509Content.add(cert.getSubjectX500Principal().getName());
                x509Content.add(cert);
                X509Data x509data = keyInfoFactory.newX509Data(x509Content);
                KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509data));

                Element header = DOMUtils.getFirstChildElement(root);
                DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), header);
                XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, keyInfo);
                signature.sign(dsc);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to sign asiointitili request: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean handleFault(LogicalMessageContext context) {
        return false;
    }

    @Override
    public void close(MessageContext context) {
    }
}
