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
package fi.vm.sade.viestintapalvelu.recovery;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fi.vm.sade.viestintapalvelu.letter.LetterBatchProcessor;
import fi.vm.sade.viestintapalvelu.letter.LetterService;

@RunWith(MockitoJUnitRunner.class)
public class LetterPDFRecovererTest {

    @Mock
    private LetterBatchProcessor processor;
    
    @Mock
    private LetterService service;
    
    @InjectMocks
    private LetterPDFRecoverer recoverer = new LetterPDFRecoverer();
    
    @Test
    public void retrievesUnfinishedLetterBatches() throws InterruptedException {
        recoverer.getTask().run();
        verify(service).findUnfinishedLetterBatches();
    }
    
    @Test
    public void passesUnfinishedLetterBatchesForProcessing() throws InterruptedException {
        Long batchId = 3l;
        when(service.findUnfinishedLetterBatches()).thenReturn(Arrays.asList(batchId));
        recoverer.getTask().run();
        verify(processor).processLetterBatch(batchId);
    }
    
    @Test
    public void catchesAnyExceptionThrownByProcessor() throws InterruptedException {
        when(service.findUnfinishedLetterBatches()).thenReturn(Arrays.asList((Long)7l));
        when(processor.processLetterBatch(any(Long.class))).thenThrow(new IllegalArgumentException());
        recoverer.getTask().run();
    }
    
}
