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
package fi.vm.sade.viestintapalvelu.letter;

import javax.ws.rs.NotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.letter.impl.DokumenttiIdProviderImpl;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * User: ratamaa
 * Date: 9.10.2014
 * Time: 14:22
 */
@RunWith(MockitoJUnitRunner.class)
public class DokumenttiIdProviderImplTest {
    private DokumenttiIdProviderImpl dokumenttiIdProvider = new DokumenttiIdProviderImpl();
    private String testSalt="TEST-SALT";

    @Mock
    private LetterBatchDAO letterBatchDAO;

    @Before
    public void before() {
        dokumenttiIdProvider.setDokumenttiIdSalt(testSalt);
        dokumenttiIdProvider.setLetterBatchDAO(letterBatchDAO);
    }

    @Test
    public void testGenerateDocumentIdForLetterBatchId() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(1l);
        when(letterBatchDAO.read(eq(Long.valueOf(1l)))).thenReturn(letterBatch);

        ShaPasswordEncoder enc = new ShaPasswordEncoder();
        String id = dokumenttiIdProvider.generateDocumentIdForLetterBatchId(1l, "prefix-t-");
        assertEquals("prefix-t-1-" + enc.encodePassword(testSalt + "prefix-t-1-"
                +letterBatch.getTimestamp().getTime(), ""), id);
    }

    @Test
    public void testParseLetterBatchIdByDokumenttiId() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(1l);
        when(letterBatchDAO.read(eq(Long.valueOf(1l)))).thenReturn(letterBatch);

        ShaPasswordEncoder enc = new ShaPasswordEncoder();
        long id = dokumenttiIdProvider.parseLetterBatchIdByDokumenttiId("prefix-t-1-"
                    + enc.encodePassword(testSalt + "prefix-t-1-"+letterBatch.getTimestamp().getTime(), ""), "prefix-t-");
        assertEquals(1l, id);
    }

    @Test(expected = NotFoundException.class)
    public void testIllegalIdByDifferingHash() {
        when(letterBatchDAO.read(eq(Long.valueOf(54321l)))).thenReturn(DocumentProviderTestData.getLetterBatch(54321l));
        dokumenttiIdProvider.parseLetterBatchIdByDokumenttiId("prefix-t-54321-abcdefgh", "prefix-t-");
    }

    @Test(expected = NotFoundException.class)
    public void testIllegalIdByDifferingPrefix() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(54321l);
        when(letterBatchDAO.read(eq(Long.valueOf(54321l)))).thenReturn(letterBatch);
        ShaPasswordEncoder enc = new ShaPasswordEncoder();
        dokumenttiIdProvider.parseLetterBatchIdByDokumenttiId("prefix-different-54321-"
                        + enc.encodePassword(testSalt + "prefix-t-54321-"+letterBatch.getTimestamp().getTime(), ""),  "prefix-t-");
    }

    @Test(expected = NotFoundException.class)
    public void testLetterBatchNotFound() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(54321l);
        when(letterBatchDAO.read(eq(Long.valueOf(54321l)))).thenReturn(null);
        ShaPasswordEncoder enc = new ShaPasswordEncoder();
        dokumenttiIdProvider.parseLetterBatchIdByDokumenttiId("prefix-t-54321-"
                + enc.encodePassword(testSalt + "prefix-t-54321-"+letterBatch.getTimestamp().getTime(), ""),  "prefix-t-");
    }
}
