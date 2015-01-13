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
package fi.vm.sade.viestintapalvelu.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fi.vm.sade.viestintapalvelu.letter.LetterBatchStatusLegalityChecker;
import fi.vm.sade.viestintapalvelu.model.LetterBatch.Status;

@RunWith(JUnit4.class)
public class LetterBatchStatusLegalityCheckerTest {
    private LetterBatchStatusLegalityChecker checker = new LetterBatchStatusLegalityChecker();

    @Test
    public void testLegalStateChanges() {
        checker.ensureLegalStateChange(1l, Status.created, Status.processing);
        checker.ensureLegalStateChange(1l, Status.processing, Status.ready);
        checker.ensureLegalStateChange(1l, Status.processing, Status.error);
        checker.ensureLegalStateChange(1l, Status.processing, Status.waiting_for_ipost_processing);
        checker.ensureLegalStateChange(1l, Status.waiting_for_ipost_processing, Status.processing_ipost);
        checker.ensureLegalStateChange(1l, Status.processing_ipost, Status.ready);
        checker.ensureLegalStateChange(1l, Status.processing_ipost, Status.error);
    }

    @Test(expected = IllegalStateException.class)
    public void testChangeFromErrorNotAllowed() throws Exception {
        checker.ensureLegalStateChange(1l, Status.error, Status.processing);
    }

    @Test(expected = IllegalStateException.class)
    public void testChangeFromProcessingNotAllowed() throws Exception {
        checker.ensureLegalStateChange(1l, Status.ready, Status.processing);
    }

    @Test(expected = IllegalStateException.class)
    public void testChangeFromWaitingIpostProcessingToProcessingNotAllowed() throws Exception {
        checker.ensureLegalStateChange(1l, Status.waiting_for_ipost_processing, Status.processing);
    }
}