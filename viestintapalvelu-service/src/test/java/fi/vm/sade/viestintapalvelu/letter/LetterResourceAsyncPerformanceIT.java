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

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import fi.vm.sade.viestintapalvelu.category.PerformanceTest;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.model.*;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

import static org.junit.Assert.assertTrue;

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

    private static final long MILLIS_IN_SECOND = 1000l;
    private static final int RECEIVERS_COUNT = 3000;
    private static final long MAX_DURATION = 60l*MILLIS_IN_SECOND;

    @Autowired
    private LetterResource letterResource;

    @Autowired
    private TransactionalActions transactionalActions;

    @Autowired
    private LetterBatchPDFProcessor batchPDFProcessor;

    @Test
    public void processesXLettersUnderMinute() throws InterruptedException, ExecutionException {
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<Runnable>();
        ThreadPoolExecutor exec = new ThreadPoolExecutor(10,10,MAX_DURATION, TimeUnit.MILLISECONDS, queue);
        logger.info("Starting processes{}LettersUnderMinute. Populating data...", RECEIVERS_COUNT);

        final long batchModelId = exec.submit(new Callable<Long>() {
            public Long call() throws Exception {
                return transactionalActions.createTestBatch(RECEIVERS_COUNT);
            }
        }).get();
        logger.info("Populated {} receivers to batch {}", RECEIVERS_COUNT, batchModelId);

        logger.info("Starting processLetterBatch");
        long start = System.currentTimeMillis();

        exec.submit(new Runnable() {
            @Override
            public void run() {
                batchPDFProcessor.processLetterBatch(batchModelId);
                logger.info("Processes started.");
            }
        });

        while (isProcessing(batchModelId)) {
            assertTrue("Test took more than " + MAX_DURATION / MILLIS_IN_SECOND + "s.",
                    System.currentTimeMillis() - start < MAX_DURATION);
            Thread.sleep(500l);
        }
        long duration = System.currentTimeMillis()-start;
        exec.shutdown();

        assertTrue("Test took more than " + MAX_DURATION / MILLIS_IN_SECOND + "s.", duration < MAX_DURATION);
        logger.info("Done processLetterBatch in {} s < {} s. OK.",
                Math.round((double) duration / (double) MILLIS_IN_SECOND * 10d) / 10d,
                MAX_DURATION / MILLIS_IN_SECOND);
    }

    protected boolean isProcessing(long id) {
        Response response = letterResource.letterBatchStatus(id);
        Map<String,Integer> entity = (Map<String, Integer>) response.getEntity();
        logger.info("Status: {} / {}", entity.get("sent"), entity.get("total"));
        return entity.get("sent").compareTo(entity.get("total")) < 0;
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
        private LetterBatchDAO letterBatchDAO;

        @Autowired
        private TemplateDAO templateDAO;

        public long createTestBatch(int count) {
            Template template = DocumentProviderTestData.getTemplate(null);
            for (TemplateContent content : template.getContents()) {
                content.setContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD " +
                        "XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Testi</title></head>" +
                        "<body>Testi</body></html>");
            }
            templateDAO.insert(template);

            LetterBatch batch = DocumentProviderTestData.getLetterBatch(null, count);
            for (LetterReceivers receiver : batch.getLetterReceivers()) {
                receiver.getLetterReceiverLetter().setLetter(null);
            }
            List<IPosti> posti = DocumentProviderTestData.getIPosti(null, batch);
            batch.getIposti().addAll(posti);
            batch.setTemplateId(template.getId());
            letterBatchDAO.insert(batch);

            return batch.getId();
        }
    }

}
