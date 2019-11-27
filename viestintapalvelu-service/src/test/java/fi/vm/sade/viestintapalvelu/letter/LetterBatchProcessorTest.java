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

import static fi.vm.sade.viestintapalvelu.util.AnswerChain.atFirstDoNothing;
import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.google.common.base.Optional;

import fi.vm.sade.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiversDAO;
import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;
import fi.vm.sade.viestintapalvelu.letter.impl.LetterServiceImpl;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import fi.vm.sade.viestintapalvelu.util.AnswerChain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

@RunWith(MockitoJUnitRunner.class)
public class LetterBatchProcessorTest {

    private static final long LETTERBATCH_ID = 1l;

    @Mock
    private LetterServiceImpl service;

    @Mock
    private LetterBatchDAO letterBatchDAO;

    @Mock
    private LetterReceiverLetterDAO letterReceiverLetterDAO;

    @Mock
    private LetterReceiversDAO letterReceiversDAO;

    @Mock
    private LetterBuilder builder;

    private LetterBatchProcessor processor;

    @Before
    public void init() {
        processor = new LetterBatchProcessor(Executors.newFixedThreadPool(4),
                Executors.newFixedThreadPool(10), service);
        doCallRealMethod().when(service).setLetterBatchDAO(any(LetterBatchDAO.class));
        doCallRealMethod().when(service).setLetterReceiverLetterDAO(any(LetterReceiverLetterDAO.class));
        doCallRealMethod().when(service).setLetterBuilder(any(LetterBuilder.class));
        doCallRealMethod().when(service).setLetterReceiversDAO(any(LetterReceiversDAO.class));
        doCallRealMethod().when(service).setObjectMapperProvider(any(ObjectMapperProvider.class));
        doCallRealMethod().when(service).getLetterBuilder();
        doCallRealMethod().when(service).findUnprocessedLetterReceiverIdsByBatch(any(long.class));
        service.setLetterBuilder(builder);
        service.setLetterBatchDAO(letterBatchDAO);
        service.setLetterReceiverLetterDAO(letterReceiverLetterDAO);
        service.setLetterReceiversDAO(letterReceiversDAO);
        service.setObjectMapperProvider(new ObjectMapperProvider());
    }

    @Test
    public void updatesProcessStartedOnLetterBatch() throws Exception {
        waitFor(processor.processLetterBatch(LETTERBATCH_ID));
        verify(service, times(1)).updateBatchProcessingStarted(1l, LetterBatchProcess.LETTER);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void buildsPDFForAllReceivers() throws Exception {
        final int amountOfReceivers = 121;
        //doCallRealMethod().when(service).runBatch(any(long.class));
        when(letterBatchDAO.findUnprocessedLetterReceiverIdsByBatch(any(long.class))).thenReturn(listOfLongsUpTo(amountOfReceivers));
        doCallRealMethod().when(service).processLetterReceiver(any(long.class));
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(LETTERBATCH_ID);
        when(letterReceiversDAO.read(any(long.class))).thenReturn(DocumentProviderTestData.getLetterReceivers(1l, batch)
                .iterator().next());
        when(service.updateBatchProcessingFinished(eq(LETTERBATCH_ID), eq(LetterBatchProcess.LETTER)))
                .thenReturn(Optional.<LetterBatchProcess>absent());
        Future<Boolean> state = processor.processLetterBatch(LETTERBATCH_ID);
        assertTrue(state.get(10, SECONDS));
        verify(builder, timeout(100).times(amountOfReceivers)).constructPagesForLetterReceiverLetter(any(LetterReceivers.class), any(LetterBatch.class), any(Map.class), any(Map.class));
        verify(letterReceiverLetterDAO, timeout(100).times(amountOfReceivers)).update(any(LetterReceiverLetter.class));
    }

    @Test
    public void buildsPDFForAllReceiversWithFailure() throws Exception {
        final int amountOfReceivers = 121;
        //doCallRealMethod().when(service).runBatch(any(long.class));
        when(letterBatchDAO.findUnprocessedLetterReceiverIdsByBatch(any(long.class))).thenReturn(listOfLongsUpTo(amountOfReceivers));
        doCallRealMethod().when(service).processLetterReceiver(any(long.class));
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(LETTERBATCH_ID);
        when(letterReceiversDAO.read(any(long.class))).thenReturn(DocumentProviderTestData.getLetterReceivers(1l, batch)
                .iterator().next());
        int okCount = 20;
        AnswerChain<Void> processCalls = atFirstDoNothing().times(okCount).thenThrow(new IllegalStateException("Something went wrong.")),
                updateCalls = atFirstDoNothing();
        doAnswer(processCalls)
                .when(builder).constructPagesForLetterReceiverLetter(any(LetterReceivers.class), any(LetterBatch.class),
                any(Map.class), any(Map.class));
        doAnswer(updateCalls).when(letterReceiverLetterDAO).update(any(LetterReceiverLetter.class));

        Future<Boolean> state = processor.processLetterBatch(LETTERBATCH_ID);
        assertFalse(state.get(10, SECONDS));
        assertTrue(processCalls.getTotalCallCount() > okCount); // okCount + 1 at least + possible other threads
        assertTrue(processCalls.getTotalCallCount() <= okCount + 1 + new LetterBatchProcessor().getLetterBatchJobThreadCount());
        assertEquals(okCount, updateCalls.getTotalCallCount());
    }
    
    @Test (expected = ConcurrentModificationException.class)
    public void preventsProcessingSameBatchSimultaneously() throws InterruptedException, ExecutionException, TimeoutException {
        long id = 5l;
        Future<Boolean> f1 = processor.processLetterBatch(id);
        Future<Boolean> f2 = processor.processLetterBatch(id);
        waitFor(f2);
        waitFor(f1);
    }

    @Test
    public void storesLetterBatchIdBeingProcessedIntoQueue() throws InterruptedException, ExecutionException, TimeoutException {
        long id = 7l;
        Future<Boolean> f = processor.processLetterBatch(id);
        assertTrue(processor.isProcessingLetterBatch(id));
        waitFor(f);
    }
    
    @Test(timeout=10000)
    public void removesLetterBatchIdFromQueueAfterProcessing() throws Exception {
        when(service.updateBatchProcessingFinished(eq(LETTERBATCH_ID), eq(LetterBatchProcess.LETTER)))
                .thenReturn(Optional.<LetterBatchProcess>absent());
        waitFor(processor.processLetterBatch(LETTERBATCH_ID));
    }

    private List<Long> listOfLongsUpTo(int amountOfReceivers) {
        List<Long> longs = new ArrayList<Long>();
        for (int i = 0; i < amountOfReceivers; i++) {
            longs.add(i+1l);
        }
        return longs;
    }

    @Test
    public void updatesProcessFinishedOnLetterBatch() throws Exception {
        final int amountOfReceivers = 157;
        // TODO Miksi tätä ei kutsuta???
        // when(service.fetchById(LETTERBATCH_ID)).thenReturn(givenLetterBatchWithReceivers(amountOfReceivers));
        waitFor(processor.processLetterBatch(LETTERBATCH_ID));
        verify(service, timeout(100)).updateBatchProcessingFinished(LETTERBATCH_ID, LetterBatchProcess.LETTER);
    }

    private LetterBatch givenLetterBatchWithReceivers(final int amountOfReceivers) {
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(LETTERBATCH_ID);
        Set<LetterReceivers> receivers = batch.getLetterReceivers();
        for (int i = 0; i < amountOfReceivers; i++) {
            receivers.addAll(DocumentProviderTestData.getLetterReceivers(Long.valueOf(i), batch));
        }
        batch.setLetterReceivers(receivers);
        return batch;
    }

    private void waitFor(Future<Boolean> f) throws InterruptedException, ExecutionException, TimeoutException {
        f.get(10, SECONDS);
    }
}
