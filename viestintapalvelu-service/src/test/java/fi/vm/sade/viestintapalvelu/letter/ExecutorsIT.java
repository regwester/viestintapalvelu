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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import static org.junit.Assert.*;

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
    private static final Logger logger = LoggerFactory.getLogger(ExecutorsIT.class);

    @Resource(name="receiverExecutor")
    private Executor receiverExecutor;

    @Resource(name="batchJobExecutor")
    private Executor batchJobExecutor;

    @Resource(name="letterReceiverExecutorService")
    private ExecutorService letterReceiverExecutorService;

    @Resource(name="batchJobExecutorService")
    private ExecutorService batchJobExecutorService;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testExecutorServiceIsAnAdapterToExecutor() throws Exception {
        assertTrue(letterReceiverExecutorService instanceof ExecutorServiceAdapter);
        assertEquals(this.<Object>readProperty(letterReceiverExecutorService, "taskExecutor"), receiverExecutor);

        assertTrue(batchJobExecutorService instanceof ExecutorServiceAdapter);
        assertEquals(this.<Object>readProperty(batchJobExecutorService, "taskExecutor"), batchJobExecutor);

        assertNotEquals(batchJobExecutor, receiverExecutor);
    }

    @Test
    public void testExecutorNumberOfThreadsLimitedWithBatchJobExecutor() throws Exception {
        assertTrue(receiverExecutor instanceof ThreadPoolTaskExecutor);
        ThreadPoolExecutor actualJavaExecutor = this.<ThreadPoolExecutor>readProperty(batchJobExecutor, "threadPoolExecutor");
        int maxPoolSize = this.<Integer>readProperty(batchJobExecutor, "maxPoolSize"),
                corePoolSize = this.<Integer>readProperty(batchJobExecutor, "corePoolSize"),
                actualMaxPoolSize = this.<Integer>readProperty(actualJavaExecutor, "maximumPoolSize"),
                actualCorePoolSize = this.<Integer>readProperty(actualJavaExecutor, "corePoolSize");
        logger.info("batchJobExecutor corePoolSize = {} / maxPoolSize = {}", corePoolSize, maxPoolSize);
        assertTrue(maxPoolSize <= 10);
        assertTrue(corePoolSize <= maxPoolSize);
        assertEquals(actualMaxPoolSize, maxPoolSize);
        assertEquals(actualCorePoolSize, corePoolSize);
    }

    @Test
    public void testExecutorNumberOfThreadsLimitedWithReceiverExecutor() throws Exception {
        assertTrue(receiverExecutor instanceof ThreadPoolTaskExecutor);
        ThreadPoolExecutor actualJavaExecutor = this.<ThreadPoolExecutor>readProperty(receiverExecutor, "threadPoolExecutor");
        int maxPoolSize = this.<Integer>readProperty(receiverExecutor, "maxPoolSize"),
            corePoolSize = this.<Integer>readProperty(receiverExecutor, "corePoolSize"),
            actualMaxPoolSize = this.<Integer>readProperty(actualJavaExecutor, "maximumPoolSize"),
            actualCorePoolSize = this.<Integer>readProperty(actualJavaExecutor, "corePoolSize");
        logger.info("receiverExecutor corePoolSize = {} / maxPoolSize = {}", corePoolSize, maxPoolSize);
        assertTrue(maxPoolSize <= 50);
        assertTrue(corePoolSize <= maxPoolSize);
        assertEquals(actualMaxPoolSize, maxPoolSize);
        assertEquals(actualCorePoolSize, corePoolSize);
    }

    @Test
    public void testConcurrentTasks() throws Exception {
        int maxPoolSize = this.<Integer>readProperty(receiverExecutor, "maxPoolSize"),
            numberOfTasks = maxPoolSize*100;
        ThreadPoolExecutor threadPoolExecutor = this.<ThreadPoolExecutor>readProperty(receiverExecutor, "threadPoolExecutor");
        List<ReleasableCallable> callables = new ArrayList<ReleasableCallable>();
        List<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
        for (int i = 0; i < numberOfTasks; ++i) {
            ReleasableCallable callable = new ReleasableCallable();
            callables.add(callable);
            futures.add(letterReceiverExecutorService.submit(callable));
        }
        // Nasty read:
        HashSet<?> workers = this.<HashSet<?>>readProperty(threadPoolExecutor, "workers");
        assertEquals(maxPoolSize, workers.size());

        BlockingQueue<Runnable> queue = this.<BlockingQueue<Runnable>>readProperty(threadPoolExecutor, "workQueue");
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
