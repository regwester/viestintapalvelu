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

import javax.ws.rs.NotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponentImpl;
import fi.vm.sade.viestintapalvelu.letter.impl.DokumenttiIdProviderImpl;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import static org.junit.Assert.assertEquals;

/**
 * User: ratamaa
 * Date: 9.10.2014
 * Time: 14:22
 */
@RunWith(JUnit4.class)
public class DokumenttiIdProviderImplTest {
    private DokumenttiIdProviderImpl dokumenttiIdProvider = new DokumenttiIdProviderImpl();
    private Henkilo henkilo = DocumentProviderTestData.getHenkilo();
    private String testSalt="TEST-SALT";

    @Before
    public void before() {
        dokumenttiIdProvider.setCurrentUserComponent(new CurrentUserComponentImpl() {
            public Henkilo getCurrentUser() {
                return henkilo;
            }
        });
        dokumenttiIdProvider.setDokumenttiIdSalt(testSalt);
    }

    @Test
    public void testGenerateDocumentIdForLetterBatchId() {
        ShaPasswordEncoder enc = new ShaPasswordEncoder();
        String id = dokumenttiIdProvider.generateDocumentIdForLetterBatchId(1l, "prefix-t-");
        assertEquals("prefix-t-1-" + enc.encodePassword(testSalt + "prefix-t-1-" + henkilo.getOidHenkilo(), ""), id);
    }

    @Test
    public void testGenerateDocumentIdForLetterBatchIdByGivenOid() {
        ShaPasswordEncoder enc = new ShaPasswordEncoder();
        String id = dokumenttiIdProvider.generateDocumentIdForLetterBatchId(54321l, "prefix-t-", "HOID");
        assertEquals("prefix-t-54321-" + enc.encodePassword(testSalt + "prefix-t-54321-HOID", ""), id);
    }

    @Test
    public void testParseLetterBatchIdByDokumenttiId() {
        ShaPasswordEncoder enc = new ShaPasswordEncoder();
        long id = dokumenttiIdProvider.parseLetterBatchIdByDokumenttiId("prefix-t-1-"
                    + enc.encodePassword(testSalt + "prefix-t-1-" + henkilo.getOidHenkilo(), ""),
                "prefix-t-");
        assertEquals(1l, id);
    }

    @Test
    public void testParseLetterBatchIdByDokumenttiIdByGivenOid() {
        ShaPasswordEncoder enc = new ShaPasswordEncoder();
        long id = dokumenttiIdProvider.parseLetterBatchIdByDokumenttiId("prefix-t-54321-"
                        + enc.encodePassword(testSalt + "prefix-t-54321-henkiloOid", ""),
                "prefix-t-", "henkiloOid");
        assertEquals(54321l, id);
    }

    @Test(expected = NotFoundException.class)
    public void testIllegalIdByDifferingHash() {
        dokumenttiIdProvider.parseLetterBatchIdByDokumenttiId("prefix-t-54321-abcdefgh", "prefix-t-");
    }

    @Test(expected = NotFoundException.class)
    public void testIllegalIdByDifferingPrefix() {
        ShaPasswordEncoder enc = new ShaPasswordEncoder();
        dokumenttiIdProvider.parseLetterBatchIdByDokumenttiId("prefix-different-54321-"
                        + enc.encodePassword(testSalt + "prefix-t-54321-henkiloOid", ""),  "prefix-t-", "henkiloOid");
    }

    @Test(expected = NotFoundException.class)
    public void testIllegalIdByDifferingOid() {
        ShaPasswordEncoder enc = new ShaPasswordEncoder();
        dokumenttiIdProvider.parseLetterBatchIdByDokumenttiId("prefix-t-54321-"
                + enc.encodePassword(testSalt + "prefix-t-54321-oidDifferent", ""),  "prefix-t-", "henkiloOid");
    }

}
