package fi.vm.sade.viestintapalvelu.message.processor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import fi.vm.sade.viestintapalvelu.dao.dto.LetterBatchStatusDto;
import fi.vm.sade.viestintapalvelu.letter.LetterBatchProcessor;
import fi.vm.sade.viestintapalvelu.letter.LetterEmailService;
import fi.vm.sade.viestintapalvelu.letter.LetterService;
import fi.vm.sade.viestintapalvelu.model.LetterBatch.Status;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageDataProcessorTest {

    private static final long BATCH_ID = 1l;

    @Spy
    private ExecutorService executor = new DummyExecutorService();
    
    @Mock
    private LetterService letterService;
    
    @Mock
    private LetterEmailService emailService;
    
    @Mock
    private LetterBatchProcessor batchProcessor;
       
    @InjectMocks
    private MessageDataProcessor processor = new MessageDataProcessor();
    
    @Before
    public void init() {
        
        doCallRealMethod().when(executor).execute(any(Runnable.class));
    }
    
    @Test
    public void doesNotSendEmailWhenBatchProcessingEndsOnErrorStatus() throws Exception {
        processBatchWithGivenStatus(Status.error);
        verify(emailService, never()).sendEmail(BATCH_ID);        
    }
    
    @Test
    public void doesNotSendEmailWhenBatchProcessingEndsOnIllegalStatus() throws Exception {
        processBatchWithGivenStatus(Status.waiting_for_ipost_processing);
        verify(emailService, never()).sendEmail(BATCH_ID);        
    }
    
    @Test
    public void sendsEmailOnceProcessingHasBeenFinished() throws Exception {
        processBatchWithGivenStatus(Status.ready);
        verify(emailService).sendEmail(BATCH_ID);   
    }

    private void processBatchWithGivenStatus(Status status) {
        when(letterService.getBatchStatus(BATCH_ID)).thenReturn(new LetterBatchStatusDto(BATCH_ID, 0, 0, status, 0));
        processor.processAndSendEmailBatch(BATCH_ID);
    }
    
    private static class DummyExecutorService implements ExecutorService {

        @Override
        public void execute(Runnable command) {
            command.run();
        }

        @Override
        public void shutdown() {
        }

        @Override
        public List<Runnable> shutdownNow() {
            return null;
        }

        @Override
        public boolean isShutdown() {
            return false;
        }

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return null;
        }

        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            return null;
        }

        @Override
        public Future<?> submit(Runnable task) {
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
                TimeoutException {
            return null;
        }
        
    }
}
