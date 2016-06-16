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

import java.util.ConcurrentModificationException;
import java.util.List;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.letter.LetterBatchProcessor;
import fi.vm.sade.viestintapalvelu.letter.LetterService;

@Singleton
@Component
@RecovererPriority(1000)
public class LetterPDFRecoverer implements Recoverer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LetterPDFRecoverer.class);

    @Autowired
    private LetterBatchProcessor letterPDFProcessor;

    @Autowired
    private LetterService letterService;

    @Override
    public Runnable getTask() {
        return new Runnable() {

            @Override
            public void run() {
                List<Long> unfinishedLetterBatchIds = letterService.findUnfinishedLetterBatches();
                LOGGER.info("Recovery process for unfinished letterbatches starting for " + unfinishedLetterBatchIds.size() + " letterbatches");
                for (Long letterBatchId : unfinishedLetterBatchIds) {
                    processLetterBatch(letterBatchId);
                }

            }

            private void processLetterBatch(Long letterBatchId) {
                try {
                    letterPDFProcessor.processLetterBatch(letterBatchId);
                } catch (ConcurrentModificationException e) {
                    LOGGER.warn("Attempted to recover processing of LetterBatch " + letterBatchId + " that was already being processed", e);
                } catch (Exception e) {
                    LOGGER.error("Unable to recover processing of letterbatch id=" + letterBatchId, e);
                }
            }

        };
    }
}
