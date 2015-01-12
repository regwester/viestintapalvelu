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
