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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.category.PerformanceTest;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchStatusDto;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchDto;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchLetterDto;
import fi.vm.sade.viestintapalvelu.letter.impl.LetterServiceImpl;
import fi.vm.sade.viestintapalvelu.model.*;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * User: ratamaa
 * Date: 17.9.2014
 * Time: 14:21
 */
@Category(PerformanceTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LetterResourceAsyncPerformanceIT.Config.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class LetterResourceAsyncPerformanceIT {
    protected static final Logger logger = LoggerFactory.getLogger(LetterResourceAsyncPerformanceIT.class);

    private static final long MILLIS_IN_SECOND = 1000;
    private static final int RECEIVERS_COUNT = 200;
    private static final int CONCURRENT_BATCHES = 3;
    private static final long MAX_DURATION = 60 * MILLIS_IN_SECOND;

    @Autowired
    private LetterResource letterResource;

    @Autowired
    private TransactionalActions transactionalActions;

    @Autowired
    private LetterService letterService;

    @Before
    public void before() throws Exception {
        final Henkilo testHenkilo = DocumentProviderTestData.getHenkilo();
        Field currentUserComponent = LetterServiceImpl.class.getDeclaredField("currentUserComponent");
        currentUserComponent.setAccessible(true);
        currentUserComponent.set(((Advised)letterService).getTargetSource().getTarget(),
                new CurrentUserComponent() {
                    @Override
                    public Henkilo getCurrentUser() {
                        return testHenkilo;
                    }
                });
    }

    @Test
    public void processesSingleBatchWith4kLettersUnderMinute() throws Exception {
        final Integer letterCount = 4000;
        final long templateId = transactionalActions.createTemplate();
        long id = asyncLetter(createLetterBatch(templateId, letterCount));
        long start = System.currentTimeMillis();
        while (isProcessing(id, false)) {
            long currentDuration = System.currentTimeMillis() - start;
            if (currentDuration > MAX_DURATION) {
                fail("Test took " + roundSeconds(currentDuration) + " s > " + roundSeconds(MAX_DURATION) + "s.");
            }
            Thread.sleep(500);
        }
        long duration = System.currentTimeMillis()-start;
        if (duration > MAX_DURATION) {
            fail("Test took "+ roundSeconds(duration) + " s > " + roundSeconds(MAX_DURATION) + "s.");
        }
        long througput = Math.round( (double)(letterCount) / roundSeconds(duration) );
        logger.info("Done processLetterBatch in {} s < {} s, throughput: "+ througput +" messages / s. OK.",
                roundSeconds(duration), roundSeconds(MAX_DURATION));
    }

    @Test
    public void processesThreeBatchesWith2kLettersUnderMinute() throws InterruptedException, ExecutionException {
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<Runnable>();
        ThreadPoolExecutor exec = new ThreadPoolExecutor(CONCURRENT_BATCHES,CONCURRENT_BATCHES+2,
                MAX_DURATION, TimeUnit.MILLISECONDS, queue);
        logger.info("Starting processes{}LettersUnderMinute. Populating data...", RECEIVERS_COUNT);

        // Use the same template:
        final long templateId = transactionalActions.createTemplate();

        // Create batches concurrently:
        List<Future<Long>> futures = new ArrayList<Future<Long>>();
        logger.info("Calling asyncLetters");
        for (int i = 0; i < CONCURRENT_BATCHES; ++i) {
            futures.add(exec.submit(new Callable<Long>() {
                public Long call() throws Exception {
                    long id = asyncLetter(createLetterBatch(templateId, RECEIVERS_COUNT));
                    logger.info("Letter batch {} created.", id);
                    return id;
                }
            }));
        }
        // Saving josbs concrurently, starting runtime monitoring:
        long start = System.currentTimeMillis();
        // Sync job ids:
        long batchModelIds[] = new long[CONCURRENT_BATCHES];
        for (int i = 0; i < CONCURRENT_BATCHES; ++i) {
            batchModelIds[i] = futures.get(i).get();
        }

        logger.info("Populated {} receivers to {} batches", RECEIVERS_COUNT, CONCURRENT_BATCHES);
        // Starting concurrent monitoring:
        List<Future<Boolean>> monitors = new ArrayList<Future<Boolean>>();
        for (int i = 0; i < CONCURRENT_BATCHES; ++i) {
            monitors.add(exec.submit(new ProcessMonitor(batchModelIds[i], true)));
        }

        // Wait for all monitors to be done and meanwhile check that max time is not exceeded:
        while (!allDone(monitors)) {
            long currentDuration = System.currentTimeMillis() - start;
            if (currentDuration > MAX_DURATION) {
                exec.shutdown();
                fail("Test took " + roundSeconds(currentDuration) + " s > " + roundSeconds(MAX_DURATION) + "s.");
            }
            Thread.sleep(500l);
        }
        // Assert that all monitors exited with false return value (not processing):
        for (Future<Boolean> monitor : monitors) {
            assertFalse(monitor.get());
        }

        long duration = System.currentTimeMillis()-start;
        exec.shutdown();

        if (duration > MAX_DURATION) {
            fail("Test took "+roundSeconds(duration)+" s > " + roundSeconds(MAX_DURATION) + "s.");
        }
        long througput = Math.round( (double)(CONCURRENT_BATCHES * RECEIVERS_COUNT) / roundSeconds(duration) );
        logger.info("Done processLetterBatch in {} s < {} s, throughput: "+througput+" messages / s. OK.",
                roundSeconds(duration), roundSeconds(MAX_DURATION));
    }

    private double roundSeconds(long millis) {
        return Math.round((double) millis / (double) MILLIS_IN_SECOND * 10d) / 10d;
    }

    private<T> boolean allDone(List<Future<T>> futures) {
        int doneCount = 0;
        for (Future<T> future : futures) {
            if (future.isDone()) {
                ++doneCount;
            }
        }
        logger.info("> All jobs status: {} / {} ready", doneCount, futures.size());
        return doneCount == futures.size();
    }

    protected AsyncLetterBatchDto createLetterBatch(long templateId, int letterCount) {
        AsyncLetterBatchDto batch = new AsyncLetterBatchDto();
        batch.setTemplateName("Test template");
        fi.vm.sade.viestintapalvelu.template.Template template = new fi.vm.sade.viestintapalvelu.template.Template();
        template.setId(templateId);
        batch.setTemplate(template);
        batch.setLanguageCode("FI");
        batch.setIposti(true);
        batch.setTemplateId(templateId);
        List<AsyncLetterBatchLetterDto> letters = new ArrayList<AsyncLetterBatchLetterDto>();
        for (int i = 0; i < letterCount; ++i) {
            AsyncLetterBatchLetterDto letter = new AsyncLetterBatchLetterDto();
            letter.setLanguageCode("FI");
            AddressLabel label = new AddressLabel("Joku", "Jokunen",
                    "Sitruunakuja 6", "", "",
                    "99999", "KORVATUNTURI",
                    "Lappi", "Finland", "FI");
            letter.setAddressLabel(label);
            letter.setTemplateReplacements(new HashMap<String, Object>());
            letters.add(letter);
        }
        batch.setLetters(letters);
        batch.setTemplateReplacements(new HashMap<String, Object>());
        return batch;
    }

    protected long asyncLetter(AsyncLetterBatchDto letterBatchDto) {
        Response response = letterResource.asyncLetter(letterBatchDto);
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new IllegalStateException("Response status not OK: " + response.getStatus());
        }
        return (Long) response.getEntity();
    }

    protected class ProcessMonitor implements Callable<Boolean> {
        private long batchId;
        private boolean waitForDone=false;

        public ProcessMonitor(long batchId) {
            this.batchId = batchId;
        }

        public ProcessMonitor(long batchId, boolean waitForDone) {
            this.batchId = batchId;
            this.waitForDone = waitForDone;
        }

        @Override
        public Boolean call() throws Exception {
            while (isProcessing(this.batchId, this.waitForDone)) {
                Thread.sleep(500l);
            }
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    protected boolean isProcessing(long id, boolean waitForDone) {
        Response response = letterResource.letterBatchStatus(id);
        LetterBatchStatusDto entity = (LetterBatchStatusDto) response.getEntity();
        logger.info("  > Batch "+id+" status: {} / {}",
                entity.getSent(), entity.getTotal());
        if (waitForDone) {
            return entity.getStatus() != LetterBatch.Status.ready;
        }
        return entity.getSent().compareTo(entity.getTotal()) < 0;
    }

    @Configuration
    @ImportResource(value = "classpath:test-application-context.xml")
    public static class Config {
        @Bean
        TransactionalActions transactionlActions() {
            return new TransactionalActions();
        }
    }

    @Service
    @Transactional
    public static class TransactionalActions {
        @Autowired
        private TemplateDAO templateDAO;

        public long createTemplate() {
            Template template = DocumentProviderTestData.getTemplate(null);
            for (TemplateContent content : template.getContents()) {
                content.setContent(testHtmlContent());
            }
            templateDAO.insert(template);
            return template.getId();
        }
    }

    protected static String testHtmlContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD " +
                "XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Testi</title></head>" +
                "<body>Testi</body></html>";
    }

}
