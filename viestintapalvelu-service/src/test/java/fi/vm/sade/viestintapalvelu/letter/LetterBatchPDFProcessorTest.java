package fi.vm.sade.viestintapalvelu.letter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.letter.LetterService.LetterBatchProcess;
import fi.vm.sade.viestintapalvelu.letter.impl.LetterServiceImpl;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import fi.vm.sade.viestintapalvelu.util.AnswerChain;

import static fi.vm.sade.viestintapalvelu.util.AnswerChain.atFirstDoNothing;
import static fi.vm.sade.viestintapalvelu.util.AnswerChain.atFirstReturn;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LetterBatchPDFProcessorTest {

    private static final long LETTERBATCH_ID = 1l;

    @Mock
    private LetterServiceImpl service;

    @Mock
    private LetterBatchDAO letterBatchDAO;

    @Mock
    private LetterReceiverLetterDAO letterReceiverLetterDAO;

    @Mock
    private LetterBuilder builder;

    private LetterBatchPDFProcessor processor;

    @Before
    public void init() {
        processor = new LetterBatchPDFProcessor(Executors.newCachedThreadPool(), service);
        doCallRealMethod().when(service).setLetterBatchDAO(any(LetterBatchDAO.class));
        doCallRealMethod().when(service).setLetterReceiverLetterDAO(any(LetterReceiverLetterDAO.class));
        doCallRealMethod().when(service).setLetterBuilder(any(LetterBuilder.class));
        doCallRealMethod().when(service).setLogger(any(Logger.class));
        doCallRealMethod().when(service).getLetterBuilder();
        doCallRealMethod().when(service).findLetterReceiverIdsByBatch(any(long.class));
        service.setLetterBuilder(builder);
        service.setLetterBatchDAO(letterBatchDAO);
        service.setLogger(LoggerFactory.getLogger(LetterServiceImpl.class));
        service.setLetterReceiverLetterDAO(letterReceiverLetterDAO);
    }

    @Test
    public void updatesProcessStartedOnLetterBatch() {
        processor.processLetterBatch(LETTERBATCH_ID);
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
        when(letterReceiverLetterDAO.read(any(long.class))).thenReturn(DocumentProviderTestData.getLetterReceivers(1l, batch)
                .iterator().next().getLetterReceiverLetter());
        Future<Boolean> state = processor.processLetterBatch(LETTERBATCH_ID);
        assertTrue(state.get());
        verify(builder, timeout(100).times(amountOfReceivers)).constructPDFForLetterReceiverLetter(any(LetterReceivers.class), any(LetterBatch.class), any(Map.class), any(Map.class));
        verify(letterReceiverLetterDAO, timeout(100).times(amountOfReceivers)).update(any(LetterReceiverLetter.class));
    }

    @Test
    public void buildsPDFForAllReceiversWithFailure() throws Exception {
        final int amountOfReceivers = 121;
        //doCallRealMethod().when(service).runBatch(any(long.class));
        when(letterBatchDAO.findUnprocessedLetterReceiverIdsByBatch(any(long.class))).thenReturn(listOfLongsUpTo(amountOfReceivers));
        doCallRealMethod().when(service).processLetterReceiver(any(long.class));
        LetterBatch batch = DocumentProviderTestData.getLetterBatch(LETTERBATCH_ID);
        when(letterReceiverLetterDAO.read(any(long.class))).thenReturn(DocumentProviderTestData.getLetterReceivers(1l, batch)
                .iterator().next().getLetterReceiverLetter());
        int okCount = 20;
        AnswerChain<Void> processCalls = atFirstDoNothing().times(okCount).thenThrow(new IllegalStateException("Something went wrong.")),
                updateCalls = atFirstDoNothing();
        doAnswer(processCalls)
                .when(builder).constructPDFForLetterReceiverLetter(any(LetterReceivers.class), any(LetterBatch.class),
                        any(Map.class), any(Map.class));
        doAnswer(updateCalls).when(letterReceiverLetterDAO).update(any(LetterReceiverLetter.class));

        Future<Boolean> state = processor.processLetterBatch(LETTERBATCH_ID);
        assertFalse(state.get());
        assertTrue(processCalls.getTotalCallCount() > okCount); // okCount + 1 at least + possible other threads
        assertTrue(processCalls.getTotalCallCount() <= okCount + 1 + LetterBatchPDFProcessor.THREADS);
        assertEquals(okCount, updateCalls.getTotalCallCount());
    }

    private List<Long> listOfLongsUpTo(int amountOfReceivers) {
        List<Long> longs = new ArrayList<Long>();
        for (int i = 0; i < amountOfReceivers; i++) {
            longs.add(i+1l);
        }
        return longs;
    }

    @Test
    public void updatesProcessFinishedOnLetterBatch() {
        final int amountOfReceivers = 157;
        when(service.fetchById(LETTERBATCH_ID)).thenReturn(givenLetterBatchWithReceivers(amountOfReceivers));
        processor.processLetterBatch(LETTERBATCH_ID);
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

}
