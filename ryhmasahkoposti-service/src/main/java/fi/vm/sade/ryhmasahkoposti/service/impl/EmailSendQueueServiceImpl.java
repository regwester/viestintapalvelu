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
package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fi.vm.sade.viestintapalvelu.externalinterface.common.ObjectMapperProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachmentDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedRecipientReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.converter.EmailMessageDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.EmailRecipientDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedRecipientReplacementConverter;
import fi.vm.sade.ryhmasahkoposti.dao.RecipientReportedAttachmentQueryResult;
import fi.vm.sade.ryhmasahkoposti.dao.SendQueueDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipientReplacement;
import fi.vm.sade.ryhmasahkoposti.model.SendQueue;
import fi.vm.sade.ryhmasahkoposti.model.SendQueueState;
import fi.vm.sade.ryhmasahkoposti.service.EmailSendQueueService;
import fi.vm.sade.ryhmasahkoposti.service.dto.EmailQueueDtoConverter;
import fi.vm.sade.ryhmasahkoposti.service.dto.EmailQueueHandleDto;
import fi.vm.sade.viestintapalvelu.common.util.CollectionHelper;

/**
 * User: ratamaa
 * Date: 15.9.2014
 * Time: 10:49
 */
@Service
public class EmailSendQueueServiceImpl implements EmailSendQueueService {
    public static final int SAFE_MARGIN_MILLIS = 5000;
    private static Logger log = LoggerFactory.getLogger(GroupEmailReportingServiceImpl.class);

    private SendQueueDAO sendQueueDao;
    private EmailQueueDtoConverter emailQueueDtoConverter;
    private EmailRecipientDTOConverter emailRecipientDTOConverter;
    private EmailMessageDTOConverter emailMessageDTOConverter;
    private ReportedRecipientReplacementConverter reportedRecipientReplacementConverter;
    private ObjectMapperProvider objectMapperProvider;

    // 3*60*1000 = 180000 (3 minutes default) after single sending of an email will be assumed stopped
    @Value("${ryhmasahkoposti.max.email.recipient.handle.time.millis:180000}")
    private long maxEmailRecipientHandleTimeMillis;

    @Autowired
    public EmailSendQueueServiceImpl(SendQueueDAO sendQueueDao, EmailQueueDtoConverter emailQueueDtoConverter,
                                     EmailRecipientDTOConverter emailRecipientDTOConverter,
                                     EmailMessageDTOConverter emailMessageDTOConverter,
                                     ReportedRecipientReplacementConverter reportedRecipientReplacementConverter,
                                     ObjectMapperProvider objectMapperProvider) {
        this.sendQueueDao = sendQueueDao;
        this.emailQueueDtoConverter = emailQueueDtoConverter;
        this.emailRecipientDTOConverter = emailRecipientDTOConverter;
        this.emailMessageDTOConverter = emailMessageDTOConverter;
        this.reportedRecipientReplacementConverter = reportedRecipientReplacementConverter;
        this.objectMapperProvider = objectMapperProvider;
    }

    @Override
    @Transactional(readOnly = true)
    public int getNumberOfUnhandledQueues() {
        return sendQueueDao.getNumberOfUnhandledQueues();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public EmailQueueHandleDto reserveNextQueueToHandle() {
        SendQueue queue = sendQueueDao.findNextAvailableSendQueue();
        if (queue == null) {
            return null;
        }
        log.info("Reserving SendQueue={} into PROCESSING state for scheduled task. Queue: {}.",
                queue.getId(), queue);
        queue.setLastHandledAt(new Date());
        queue.setState(SendQueueState.PROCESSING);
        queue.setLastHandledAt(new Date());
        // Queue contains a @javax.persistence.Version column through BaseEntity
        // -> Hibernate will update it by id and version and increase the version
        // (or else this transaction should fail)
        sendQueueDao.update(queue);

        List<ReportedRecipient> unhanled = sendQueueDao.findUnhandledRecipeientsInQueue(queue.getId());
        List<EmailRecipientDTO> recipientDtos = getRecipientDtos(unhanled);
        EmailQueueHandleDto queueDto = emailQueueDtoConverter.convert(queue, new EmailQueueHandleDto(), recipientDtos);

        log.info("SendQueue {} has total of {} unhandled recipients.",
                queueDto.getId(), queueDto.getRecipients().size());
        return queueDto;
    }

    private List<EmailRecipientDTO> getRecipientDtos(List<ReportedRecipient> recipients) {
        List<Long> recipientIds = CollectionHelper.extractIds(recipients);

        // Find recipient specific attachment for each unhandled recipients (in a single query):
        List<RecipientReportedAttachmentQueryResult> recipientAttachments = sendQueueDao
                .findRecipientAttachments(recipientIds);
        // Find recipient specific replacements for each unhandled recipient (in a single query):
        List<ReportedRecipientReplacement> recipientReplacements = sendQueueDao.findRecipientReplacements(recipientIds);

        // Convert recipients:
        List<EmailRecipientDTO> recipientDtos = emailRecipientDTOConverter.convert(recipients);
        // RecipientDTOs by recipient id:
        Map<Long, EmailRecipientDTO> unhandledByIds = CollectionHelper.map(recipientDtos, new Function<EmailRecipientDTO, Long>() {
            public Long apply(EmailRecipientDTO reportedRecipient) {
                return reportedRecipient == null ? null : reportedRecipient.getRecipientID();
            }
        });
        // Map and convert recipientAttachments to DTOs:
        for (RecipientReportedAttachmentQueryResult attachment : recipientAttachments) {
            unhandledByIds.get(attachment.getReportedRecipientId()).getAttachments().add(
                    emailMessageDTOConverter.convert(attachment.getAttachment(), new EmailAttachmentDTO()));
        }
        // Map and convert recipient specific replacements to DTOs:
        ObjectMapper mapper = objectMapperProvider.getContext(ReportedRecipientReplacementConverter.class);
        for (ReportedRecipientReplacement replacement : recipientReplacements) {
            Long recipientId = replacement.getReportedRecipient().getId(); // should not trigger additional query
            EmailRecipientDTO recipient = unhandledByIds.get(recipientId);
            if (recipient.getRecipientReplacements() == null) {
                recipient.setRecipientReplacements(new ArrayList<ReportedRecipientReplacementDTO>());
            }
            try {
                recipient.getRecipientReplacements().add(this.reportedRecipientReplacementConverter
                        .convert(replacement, new ReportedRecipientReplacementDTO(), mapper));
            } catch (IOException e) {
                throw new IllegalStateException("Error parsing JSON in ReportedRecipientReplacement="+replacement.getId()
                        + " for ReportedRecipient="+recipientId + ", jsonValue="+replacement.getJsonValue()
                        + ", value="+replacement.getValue(), e);
            }
        }
        return recipientDtos;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkForStoppedProcesses() {
        if (maxEmailRecipientHandleTimeMillis > 0) {
            Date now = new Date();
            for (SendQueue queue : sendQueueDao.findActiveQueues()) {
                if (queue.getFinishedAt() == null && queue.getLastHandledAt() != null
                        && queue.getLastHandledAt().getTime() + maxEmailRecipientHandleTimeMillis  < now.getTime()) {
                    log.info("Processing single email sending in SendQueue={} (state: {}) has taken longer than {} ms. " +
                            "Assuming processing has stopped unexpectedly. Setting the SendQueue ready for reprocessing" +
                            "in WAITING_FOR_HANDLER state..", queue.getId(), queue, maxEmailRecipientHandleTimeMillis);
                    queue.setState(SendQueueState.WAITING_FOR_HANDLER);
                    sendQueueDao.update(queue);
                }
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean continueQueueHandling(EmailQueueHandleDto queueDto) {
        log.debug("continueQueueHandling: queue={0}, version={1}", queueDto.getId(), queueDto.getVersion());
        SendQueue queue = sendQueueDao.getQueue(queueDto.getId(), queueDto.getVersion());
        if (queue == null) {
            // Could not be found by given id and version. Queue handling should not be continued:
            // This should not happen.
            queue = sendQueueDao.read(queueDto.getId());
            log.error("Discontinuing sending processor of SendQueue={} due to concurrent modification." +
                    " Queue handler had version {} and current is {}", queueDto.getId(), queueDto.getVersion(),
                    queue != null ? queue.getVersion() : null);
            return false;
        }
        if (queue.getFinishedAt() != null || queue.getState() != SendQueueState.PROCESSING) {
            log.error("Illegal state {} for SendQueue={} to continueQueueHandling. Logical error.",
                    queue.getId(), queue);
            return false;
        }

        Date now = new Date();
        long safeLimit = Math.max(0, maxEmailRecipientHandleTimeMillis - SAFE_MARGIN_MILLIS); // 5s safe marginal
        if (maxEmailRecipientHandleTimeMillis > 0 && queue.getLastHandledAt() != null
                && queue.getLastHandledAt().getTime() + safeLimit  < now.getTime()) {
            // The process consumed too much time and the queue may be (or soon be) reattached to another processes
            log.error("Discontinuing sending processor of SendQueue={} due to lastModification={} not being within " +
                    " safeLimit={} ms", queue.getId(), queue.getLastHandledAt(), safeLimit);
            return false;
        }

        log.debug("continueQueueHandling: setting queue {} lastHandledAt {} -> {}", queue.getId(),
                queue.getLastHandledAt(), now);
        queue.setLastHandledAt(now);
        sendQueueDao.update(queue);
        queueDto.setVersion(queue.getVersion());
        log.debug("Continuing queue {} handling. Current version={}", queueDto.getId(), queueDto.getVersion());
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void queueHandled(long queueId) {
        SendQueue queue = sendQueueDao.read(queueId);
        if (queue.getState() != SendQueueState.PROCESSING) {
            log.error("Called queueHandled for SendQueue={} which is not in PROCESSING state: {}", queueId, queue);
            throw new IllegalStateException("Called queueHandled for SendQueue="+queueId
                    +" which is not in PROCESSING state: "+queue);
        }
        log.info("Setting SendQueue={} READY (state: {})", queueId, queue);
        queue.setFinishedAt(new Date());
        queue.setState(SendQueueState.READY);
        sendQueueDao.update(queue);
    }

    public long getMaxEmailRecipientHandleTimeMillis() {
        return maxEmailRecipientHandleTimeMillis;
    }

    public void setMaxEmailRecipientHandleTimeMillis(long maxEmailRecipientHandleTimeMillis) {
        this.maxEmailRecipientHandleTimeMillis = maxEmailRecipientHandleTimeMillis;
    }

    public void setSendQueueDao(SendQueueDAO sendQueueDao) {
        this.sendQueueDao = sendQueueDao;
    }
}
