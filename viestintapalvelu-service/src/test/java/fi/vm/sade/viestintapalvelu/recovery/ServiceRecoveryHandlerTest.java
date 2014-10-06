package fi.vm.sade.viestintapalvelu.recovery;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceRecoveryHandlerTest {
    
    @Mock
    private ExecutorService executor;
    
    @Mock
    private Recoverer recoverer;
    
    private ServiceRecoveryHandler handler;
    
    @Before
    public void init() {
        handler = new ServiceRecoveryHandler(executor, Arrays.asList(recoverer));
    }
    
    @Test
    public void executesTasksProvidedByRecoverer() {
        Runnable runnable = Mockito.mock(Runnable.class);
        when(recoverer.getTask()).thenReturn(runnable);
        handler.recover();
        verify(recoverer).getTask();
        verify(executor).execute(runnable);
    }
}
