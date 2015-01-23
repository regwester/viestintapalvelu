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
package fi.vm.sade.viestintapalvelu.attachment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fi.vm.sade.viestintapalvelu.attachment.impl.AttachmentUri;

import static org.junit.Assert.assertEquals;

/**
 * User: ratamaa
 * Date: 24.9.2014
 * Time: 18:18
 */
@RunWith(JUnit4.class)
public class AttachmentUriTest {

    @Test
    public void testLetterReceiverLetterAttachmentUri() {
        String uriStr = "viestinta://letterReceiverLetterAttachment/20";
        AttachmentUri uri = new AttachmentUri(uriStr);
        assertEquals(uriStr, uri.getUri());
        assertEquals(uriStr, uri.toString());
        assertEquals("letterReceiverLetterAttachment/20", uri.getContent());
        assertEquals(AttachmentUri.TargetType.LetterReceiverLetterAttachment, uri.getType());
        assertEquals("/20", uri.getPayload());
        assertEquals(1, uri.getSegments().length);
        assertEquals("20", uri.getSegments()[0]);
        assertEquals(1, uri.getParameters().length);
        assertEquals(Long.valueOf(20l), uri.getLongParameter(0));
    }

    @Test
    public void testLetterReceiverLetterAttachmentUriStatic() {
        AttachmentUri uri = AttachmentUri.getLetterReceiverLetterAttachment(1l);
        assertEquals("viestinta://letterReceiverLetterAttachment/1", uri.toString());
        assertEquals(Long.valueOf(1l), uri.getLongParameter(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOtherScheme() {
        new AttachmentUri("file://letterReceiverLetterAttachment/20");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOtherContent() {
        new AttachmentUri("viestinta://other/20");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalLongValue() {
        new AttachmentUri("viestinta://letterReceiverLetterAttachment/foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalNumberOfParameters() {
        new AttachmentUri("viestinta://letterReceiverLetterAttachment/20/30");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpty() {
        new AttachmentUri("viestinta://");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpty2() {
        new AttachmentUri("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalLongParamNumber() {
        AttachmentUri.getLetterReceiverLetterAttachment(1l).getLongParameter(1);
    }

    @Test(expected = NullPointerException.class)
    public void testNull() {
        new AttachmentUri(null);
    }

}
