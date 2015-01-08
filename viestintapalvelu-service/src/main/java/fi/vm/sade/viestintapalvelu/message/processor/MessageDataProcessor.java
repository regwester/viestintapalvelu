/**
 * Copyright (c) 2015 The Finnish National Board of Education - Opetushallitus
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
package fi.vm.sade.viestintapalvelu.message.processor;

import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.letter.LetterBatchProcessor;
import fi.vm.sade.viestintapalvelu.letter.LetterEmailService;
import fi.vm.sade.viestintapalvelu.letter.LetterService;
import fi.vm.sade.viestintapalvelu.model.LetterBatch.Status;

/**
 * @author risal1
 *
 */
@Component
@Singleton
public class MessageDataProcessor {

    private static final int SLEEP_TIME = 2000;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDataProcessor.class);

    @Autowired
    private LetterBatchProcessor letterPDFProcessor;

    @Autowired
    private LetterService letterService;

    @Autowired
    private LetterEmailService emailService;

    @Resource(name = "otherAsyncResourceJobsExecutorService")
    protected ExecutorService executor;

    public void processAndSendEmailBatch(final Long batchId) {
        letterPDFProcessor.processLetterBatch(batchId);
        executor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    while (handleLetterEmailStatus(batchId)) {
                        Thread.sleep(SLEEP_TIME);
                    }
                } catch (Exception e) {
                    LOGGER.error("No emails could be sent for batch " + batchId + " due to: " + e.getMessage());
                }
            }

            private boolean handleLetterEmailStatus(final Long batchId) throws Exception {
                Status status = letterService.getBatchStatus(batchId).getStatus();
                switch (status) {
                case ready:
                    emailService.sendEmail(batchId);
                    return false;
                case error:
                case waiting_for_ipost_processing:
                    throw new IllegalStateException("Processing finished on illegal " + status  + " status");
                default:
                    break;
                }
                return true;
            }
        });

    }

}
