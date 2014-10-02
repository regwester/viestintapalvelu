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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: ratamaa
 * Date: 2.10.2014
 * Time: 10:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-application-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class ExecutorsIT {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Executor executor;

    @Autowired
    private ExecutorService executorService;

    @Test
    public void testExecutorServiceIsAnAdapterToExecutor() throws Exception {
        assertTrue(executorService instanceof ExecutorServiceAdapter);
        assertEquals(readProperty(executorService, "taskExecutor"), executor);
    }

    @Test
    public void testExecutorNumberOfThreadsLimited() throws Exception {
        assertTrue(executor instanceof ThreadPoolTaskExecutor);
        ThreadPoolExecutor actualJavaExecutor = readProperty(executor, "threadPoolExecutor");
        int maxPoolSize = readProperty(executor, "maxPoolSize"),
            corePoolSize = readProperty(executor, "corePoolSize"),
            actualMaxPoolSize = readProperty(actualJavaExecutor, "maximumPoolSize"),
            actualCorePoolSize = readProperty(actualJavaExecutor, "corePoolSize");
        logger.info("corePoolSize = {} / maxPoolSize = {}", corePoolSize, maxPoolSize);
        assertTrue(maxPoolSize <= 50);
        assertTrue(corePoolSize <= maxPoolSize);
        assertEquals(actualMaxPoolSize, maxPoolSize);
        assertEquals(actualCorePoolSize, corePoolSize);
    }

    @Test
    public void testConcurrentTasks() throws Exception {
        int maxPoolSize = this.<Integer>readProperty(executor, "maxPoolSize"),
            numberOfTasks = maxPoolSize*100;
        ThreadPoolExecutor threadPoolExecutor = readProperty(executor, "threadPoolExecutor");
        List<ReleasableCallable> callables = new ArrayList<ReleasableCallable>();
        List<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
        for (int i = 0; i < numberOfTasks; ++i) {
            ReleasableCallable callable = new ReleasableCallable();
            callables.add(callable);
            futures.add(executorService.submit(callable));
        }
        // Nasty read:
        HashSet<?> workers = readProperty(threadPoolExecutor, "workers");
        assertEquals(maxPoolSize, workers.size());

        BlockingQueue<Runnable> queue = readProperty(threadPoolExecutor, "workQueue");
        assertEquals(99*maxPoolSize, queue.size());

        for (int i = 0; i < maxPoolSize; ++i) {
            callables.get(i).release();
            futures.get(i).get();
        }
        Thread.sleep(10l);
        assertEquals(maxPoolSize, workers.size());
        assertEquals(98*maxPoolSize, queue.size());
    }

    private<T> T readProperty(Object obj, String name) throws Exception {
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return (T) field.get(obj);
    }

    protected static class ReleasableCallable implements Callable<Boolean> {
        private final AtomicBoolean waiting = new AtomicBoolean(true);
        public void release() {
            this.waiting.set(false);
        }
        @Override
        public Boolean call() throws Exception {
            while (waiting.get()) Thread.sleep(1);
            return waiting.get();
        }
    }

}
