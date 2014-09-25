package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.io.IOException;
import java.util.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.api.constants.GroupEmailConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.*;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.converter.*;
import fi.vm.sade.ryhmasahkoposti.dao.SendQueueDAO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.OrganizationComponent;
import fi.vm.sade.ryhmasahkoposti.model.*;
import fi.vm.sade.ryhmasahkoposti.service.*;
import fi.vm.sade.ryhmasahkoposti.util.TemplateBuilder;

@Service
@Transactional(readOnly = true)
public class GroupEmailReportingServiceImpl implements GroupEmailReportingService {
    private static Logger log = LoggerFactory.getLogger(GroupEmailReportingServiceImpl.class);

    private ReportedMessageService reportedMessageService;
    private ReportedRecipientService reportedRecipientService;
    private ReportedAttachmentService reportedAttachmentService;
    private ReportedMessageAttachmentService reportedMessageAttachmentService;
    private ReportedMessageConverter reportedMessageConverter;
    private ReportedRecipientConverter reportedRecipientConverter;
    private ReportedAttachmentConverter reportedAttachmentConverter;
    private AttachmentResponseConverter attachmentResponseConverter;
    private EmailMessageDTOConverter emailMessageDTOConverter;
    private EmailRecipientDTOConverter emailRecipientDTOConverter;
    private ReportedMessageDTOConverter reportedMessageDTOConverter;
    private CurrentUserComponent currentUserComponent;
    private OrganizationComponent organizationComponent;
    private TemplateService templateService;
    private ReportedMessageReplacementConverter reportedMessageReplacementConverter;
    private ReportedMessageReplacementService reportedMessageReplacementService;
    private ReportedRecipientReplacementConverter reportedRecipientReplacementConverter;
    private ReportedRecipientReplacementService reportedRecipientReplacementService;
    private SendQueueDAO sendQueueDao;

    @Value("${ryhmasahkoposti.queue.handle.size:100}")
    private Integer queueSize = 100;

    @Autowired
    public GroupEmailReportingServiceImpl(ReportedMessageService reportedMessageService,
            ReportedRecipientService reportedRecipientService, ReportedAttachmentService reportedAttachmentService,
            ReportedMessageAttachmentService reportedMessageAttachmentService,
            ReportedMessageConverter reportedMessageConverter, ReportedRecipientConverter reportedRecipientConverter,
            ReportedAttachmentConverter reportedAttachmentConverter, AttachmentResponseConverter attachmentResponseConverter,
            EmailMessageDTOConverter emailMessageDTOConverter, EmailRecipientDTOConverter emailRecipientDTOConverter,
            ReportedMessageDTOConverter reportedMessageDTOConverter, CurrentUserComponent currentUserComponent,
            OrganizationComponent organizationComponent, TemplateService templateService,
            ReportedMessageReplacementConverter reportedMessageReplacementConverter,
            ReportedMessageReplacementService reportedMessageReplacementService,
            ReportedRecipientReplacementConverter reportedRecipientReplacementConverter,
            ReportedRecipientReplacementService reportedRecipientReplacementService,
            SendQueueDAO sendQueueDao) {
        this.reportedMessageService = reportedMessageService;
        this.reportedRecipientService = reportedRecipientService;
        this.reportedAttachmentService = reportedAttachmentService;
        this.reportedMessageAttachmentService = reportedMessageAttachmentService;
        this.reportedMessageConverter = reportedMessageConverter;
        this.reportedRecipientConverter = reportedRecipientConverter;
        this.reportedAttachmentConverter = reportedAttachmentConverter;
        this.attachmentResponseConverter = attachmentResponseConverter;
        this.emailMessageDTOConverter = emailMessageDTOConverter;
        this.emailRecipientDTOConverter = emailRecipientDTOConverter;
        this.reportedMessageDTOConverter = reportedMessageDTOConverter;
        this.currentUserComponent = currentUserComponent;
        this.organizationComponent = organizationComponent;
        this.templateService = templateService;
        this.reportedMessageReplacementConverter = reportedMessageReplacementConverter;
        this.reportedMessageReplacementService = reportedMessageReplacementService;
        this.reportedRecipientReplacementConverter = reportedRecipientReplacementConverter;
        this.reportedRecipientReplacementService = reportedRecipientReplacementService;
        this.sendQueueDao = sendQueueDao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Long addSendingGroupEmail(EmailData emailData) throws IOException {
        log.debug("addSendingGroupEmail called");

        // Check email template is used
        String templateContent = null;
        TemplateDTO templateDTO = null;
        ReplacementDTO templateSubject = null;
        ReplacementDTO templateSenderFromAddress = null;
        ReplacementDTO templateSenderFromPersonal = null;
        ReplacementDTO templateReplyToAddress = null;
        ReplacementDTO templateReplyToPersonal = null;

        if (!StringUtils.isEmpty(emailData.getEmail().getTemplateName())) {
            String languageCode = TemplateDTO.DEFAULT_LANG_CODE;
            if (!StringUtils.isEmpty(emailData.getEmail().getLanguageCode()))
                languageCode = emailData.getEmail().getLanguageCode();

            // Template is used
            try {               
                templateDTO = templateService.getTemplate(emailData.getEmail().getTemplateName(),
                    languageCode, TemplateDTO.TYPE_EMAIL);
                log.debug("Loaded template: {} for {}", templateDTO, emailData.getEmail().getTemplateName());
            } catch (Exception e) {
                log.error("Failed to load template for templateName: {}, languageCode={}",
                        emailData.getEmail().getTemplateName(), emailData.getEmail().getLanguageCode(), e);
            }

            if (templateDTO != null) {
                log.debug("Template found, processing: {}", templateDTO);

                // Convert template
                TemplateBuilder templateBuilder = new TemplateBuilder();
                templateContent = templateBuilder.buildTemplate(templateDTO, emailData);

                log.debug("Template content: {}", templateContent);

                // Get sender replacements
                templateSenderFromAddress = reportedMessageReplacementConverter.getEmailFieldFromReplacements(
                        templateDTO.getReplacements(), emailData.getReplacements(), ReplacementDTO.NAME_EMAIL_SENDER_FROM);
                log.debug("Sender from address: {}", templateSenderFromAddress);
                templateSenderFromPersonal = reportedMessageReplacementConverter.getEmailFieldFromReplacements(
                        templateDTO.getReplacements(), emailData.getReplacements(), ReplacementDTO.NAME_EMAIL_SENDER_FROM_PERSONAL);
                log.debug("Sender from address personal: {}", templateSenderFromPersonal);

                // Get reply-to replacements
                templateReplyToAddress = reportedMessageReplacementConverter.getEmailFieldFromReplacements(
                        templateDTO.getReplacements(), emailData.getReplacements(), ReplacementDTO.NAME_EMAIL_REPLY_TO);
                log.debug("Reply-to from address: {}", templateReplyToAddress);
                templateReplyToPersonal = reportedMessageReplacementConverter.getEmailFieldFromReplacements(
                        templateDTO.getReplacements(), emailData.getReplacements(), ReplacementDTO.NAME_EMAIL_REPLY_TO_PERSONAL);
                log.debug("Reply-to address personal: {}", templateReplyToPersonal);

                // Subject
                templateSubject = reportedMessageReplacementConverter.getEmailFieldFromReplacements(
                        templateDTO.getReplacements(), emailData.getReplacements(), ReplacementDTO.NAME_EMAIL_SUBJECT);

                log.debug("Subject: {}", templateSubject);

            }
        }

        log.debug("Converting email to reportedMessage");
        ReportedMessage reportedMessage = reportedMessageConverter.convert(emailData.getEmail(),
                templateSenderFromAddress, templateSenderFromPersonal, templateReplyToAddress, templateReplyToPersonal,
                templateSubject, templateContent);
        log.debug("Saving message to db");
        ReportedMessage savedReportedMessage = reportedMessageService.saveReportedMessage(reportedMessage);

        if (templateDTO != null) {
            // Get template replacements from email data
            List<ReplacementDTO> emailReplacements = emailData.getReplacements();
            // Convert message replacements
            List<ReportedMessageReplacement> messageReplacements = reportedMessageReplacementConverter.convert(
                    savedReportedMessage, templateDTO.getReplacements(), emailReplacements);

            log.debug("Saving message replacements");
            for (ReportedMessageReplacement replacement : messageReplacements) {
                reportedMessageReplacementService.saveReportedMessageReplacement(replacement);
            }
        }

        List<ReportedAttachment> reportedAttachments = reportedAttachmentService.getReportedAttachments(
                emailData.getEmail().getAttachInfo());
        log.debug("Saving reportedMessageAttachments");
        reportedMessageAttachmentService.saveReportedMessageAttachments(savedReportedMessage, reportedAttachments);

        processRecipients(savedReportedMessage, emailData.getRecipient());

        return savedReportedMessage.getId();
    }

    private void processRecipients(ReportedMessage savedReportedMessage, List<EmailRecipient> emailRecipients) throws IOException {
        log.debug("Processing emailRecipients");
        List<ReportedRecipient> recipients = new ArrayList<ReportedRecipient>();
        for (EmailRecipient emailRecipient : emailRecipients) {
            log.debug("Converting emailRecipient to reportedRecipient");
            ReportedRecipient reportedRecipient = reportedRecipientConverter.convert(savedReportedMessage,
                    emailRecipient);
            log.debug("Saving reportedRecipient");
            reportedRecipientService.saveReportedRecipient(reportedRecipient);
            recipients.add(reportedRecipient);

            log.debug("Processing recipient specific replacements");
            if (emailRecipient.getRecipientReplacements() != null) {
                List<ReportedRecipientReplacementDTO> emailRecipientReplacements =
                        emailRecipient.getRecipientReplacements();
                List<ReportedRecipientReplacement> reportedRecipientReplacements =
                        reportedRecipientReplacementConverter.convert(reportedRecipient, emailRecipientReplacements);
                log.debug("Saving reportedRecipientReplacements");
                reportedRecipientReplacementService.saveReportedRecipientReplacements(reportedRecipientReplacements);
            }

            log.debug("Processing recipient specific attachments");
            if (emailRecipient.getAttachments() != null) {
                List<ReportedAttachment> reportedAttachments = reportedAttachmentService.getReportedAttachments(emailRecipient.getAttachInfo());
                log.debug("Saving ReportedMessageRecipientAttachments");
                reportedMessageAttachmentService.saveReportedRecipientAttachments(reportedRecipient, reportedAttachments);
            }
        }
        createSendQueues(recipients);
    }

    private List<SendQueue> createSendQueues(Collection<ReportedRecipient> recipients) {
        log.debug("Creating send queue for {} recipients", recipients.size());
        Iterator<ReportedRecipient> it = recipients.iterator();
        List<SendQueue> queues = new ArrayList<SendQueue>();
        while (it.hasNext()) {
            SendQueue queue = new SendQueue();
            for (int i = 0; i < queueSize && it.hasNext(); ++i) {
                ReportedRecipient recipient = it.next();
                if (recipient == null) {
                    // For tests to work:
                    continue;
                }
                recipient.setQueue(queue);
                queue.getRecipients().add(recipient);
            }
            queue.setState(SendQueueState.WAITING_FOR_HANDLER);
            sendQueueDao.insert(queue);
            queues.add(queue);
            log.debug("Creates SendQueue={} with {} recipients ", queue.getId(), queue.getRecipients().size());
        }
        log.debug("Total of {} send queues created.", queues.size());
        return queues;
    }

    @Override
    public List<ReportedRecipientReplacementDTO> getRecipientReplacements(long recipientId) throws IOException {
        log.info("getRecipientReplacements(" + recipientId + ") called");

        ReportedRecipient recipient = reportedRecipientService.getReportedRecipient(recipientId);

        List<ReportedRecipientReplacement> recipientReplacements = reportedRecipientReplacementService
                .getReportedRecipientReplacements(recipient);

        if (recipientReplacements == null)
            return null;

        return reportedRecipientReplacementConverter.convertDTO(recipientReplacements);

    }

    @Override
    public EmailMessageDTO getMessage(Long messageID) {
        log.info("getMessage(" + messageID + ") called");

        ReportedMessage reportedMessage = reportedMessageService.getReportedMessage(messageID);
        List<ReportedAttachment> reportedAttachments = reportedAttachmentService.getReportedAttachments(reportedMessage
                .getReportedMessageAttachments());

        List<ReportedMessageReplacement> reportedMessageReplacements = null;
        if (StringUtils.equals(ReportedMessage.TYPE_TEMPLATE, reportedMessage.getType())) {
            reportedMessageReplacements = reportedMessageReplacementService
                    .getReportedMessageReplacements(reportedMessage);
        }

        return emailMessageDTOConverter.convert(reportedMessage, reportedAttachments, reportedMessageReplacements);
    }

    @Override
    public ReportedMessageDTO getReportedMessage(Long messageID) {
        log.info("getReportedMessage(" + messageID + ") called");

        ReportedMessage reportedMessage = reportedMessageService.getReportedMessage(messageID);

        SendingStatusDTO sendingStatus = reportedRecipientService.getSendingStatusOfRecipients(messageID);

        List<ReportedAttachment> reportedAttachments = reportedAttachmentService.getReportedAttachments(reportedMessage
                .getReportedMessageAttachments());

        ReportedMessageDTO reportedMessageDTO = reportedMessageDTOConverter.convert(reportedMessage,
                reportedAttachments, sendingStatus);
        reportedMessageDTO.setEndTime(sendingStatus.getSendingEnded());

        return reportedMessageDTO;
    }

    @Override
    public ReportedMessageDTO getReportedMessageAndRecipients(Long messageID, PagingAndSortingDTO pagingAndSorting) {
        log.info("getReportedMessageAndRecipients(" + messageID + ") called");

        ReportedMessage reportedMessage = reportedMessageService.getReportedMessage(messageID);

        List<ReportedRecipient> reportedRecipients = reportedRecipientService.getReportedRecipients(messageID,
                pagingAndSorting);

        SendingStatusDTO sendingStatus = reportedRecipientService.getSendingStatusOfRecipients(messageID);
        sendingStatus.setSendingStarted(reportedMessage.getSendingStarted());

        List<ReportedAttachment> reportedAttachments = reportedAttachmentService.getReportedAttachments(reportedMessage
                .getReportedMessageAttachments());

        ReportedMessageDTO reportedMessageDTO = reportedMessageDTOConverter.convert(reportedMessage,
                reportedRecipients, reportedAttachments, sendingStatus);
        reportedMessageDTO.setEndTime(sendingStatus.getSendingEnded());

        return reportedMessageDTO;
    }

    @Override
    public ReportedMessageDTO getReportedMessageAndRecipientsSendingUnsuccessful(Long messageID,
                                                                                 PagingAndSortingDTO pagingAndSorting) {
        log.info("getReportedMessageAndRecipientsSendingUnsuccesful(" + messageID + ") called");

        ReportedMessage reportedMessage = reportedMessageService.getReportedMessage(messageID);

        List<ReportedRecipient> reportedRecipients = reportedRecipientService
                .getReportedRecipientsByStatusSendingUnsuccesful(messageID, pagingAndSorting);

        SendingStatusDTO sendingStatus = reportedRecipientService.getSendingStatusOfRecipients(messageID);
        sendingStatus.setSendingStarted(reportedMessage.getSendingStarted());

        List<ReportedAttachment> reportedAttachments = reportedAttachmentService.getReportedAttachments(reportedMessage
                .getReportedMessageAttachments());

        ReportedMessageDTO reportedMessageDTO = reportedMessageDTOConverter.convert(reportedMessage,
                reportedRecipients, reportedAttachments, sendingStatus);
        reportedMessageDTO.setEndTime(sendingStatus.getSendingEnded());

        return reportedMessageDTO;
    }

    @Override
    public ReportedMessagesDTO getReportedMessagesByOrganizationOid(String organizationOid,
                                                                    PagingAndSortingDTO pagingAndSorting) {
        log.info("getReportedMessagesByOrganizationOid(String, PagingAndSortingDTO) called");

        ReportedMessagesDTO reportedMessagesDTO = new ReportedMessagesDTO();

        List<ReportedMessage> reportedMessages = reportedMessageService.getReportedMessages(organizationOid,
                pagingAndSorting);
        Long numberOfReportedMessages = reportedMessageService.getNumberOfReportedMessages(organizationOid);

        Map<Long, SendingStatusDTO> sendingStatuses = new HashMap<Long, SendingStatusDTO>();
        for (ReportedMessage reportedMessage : reportedMessages) {
            SendingStatusDTO sendingStatus = reportedRecipientService.getSendingStatusOfRecipients(reportedMessage
                    .getId());
            sendingStatuses.put(reportedMessage.getId(), sendingStatus);
        }

        List<ReportedMessageDTO> listOfReportedMessageDTO = reportedMessageDTOConverter.convert(reportedMessages,
                sendingStatuses);

        reportedMessagesDTO.setReportedMessages(listOfReportedMessageDTO);
        reportedMessagesDTO.setNumberOfReportedMessages(numberOfReportedMessages);

        return reportedMessagesDTO;
    }

    @Override
    public ReportedMessagesDTO getReportedMessagesBySenderOid(String senderOid, String process, PagingAndSortingDTO pagingAndSorting) {
        log.info("getReportedMessagesBySenderOid(String, String, PagingAndSortingDTO) called");

        List<ReportedMessage> reportedMessages;
        if(process == null) {
            reportedMessages = reportedMessageService.getUserMessages(senderOid, pagingAndSorting);
        } else {
            reportedMessages = reportedMessageService.getUserMessages(senderOid, process, pagingAndSorting);
        }
        List<ReportedMessageDTO> reportedMessageDTOs = reportedMessageDTOConverter.convert(reportedMessages);

        ReportedMessagesDTO reportedMessagesDTO = new ReportedMessagesDTO();
        reportedMessagesDTO.setReportedMessages(reportedMessageDTOs);
        return reportedMessagesDTO;
    }

    @Override
    public ReportedMessagesDTO getReportedMessages(ReportedMessageQueryDTO query, PagingAndSortingDTO pagingAndSorting) {
        log.info("getReportedMessages(ReportedMessageQueryDTO query, PagingAndSortingDTO pagingAndSorting) called");

        List<ReportedMessage> reportedMessages = reportedMessageService.getReportedMessages(query, pagingAndSorting);
        Long numberOfReportedMessages = reportedMessageService.getNumberOfReportedMessages(query);

        Map<Long, SendingStatusDTO> sendingStatuses = new HashMap<Long, SendingStatusDTO>();
        for (ReportedMessage reportedMessage : reportedMessages) {
            SendingStatusDTO sendingStatus = reportedRecipientService.getSendingStatusOfRecipients(reportedMessage.getId());
            sendingStatuses.put(reportedMessage.getId(), sendingStatus);
        }

        List<ReportedMessageDTO> listOfReportedMessageDTO = reportedMessageDTOConverter.convert(reportedMessages,
                sendingStatuses);

        ReportedMessagesDTO reportedMessagesDTO = new ReportedMessagesDTO();
        reportedMessagesDTO.setReportedMessages(listOfReportedMessageDTO);
        reportedMessagesDTO.setNumberOfReportedMessages(numberOfReportedMessages);

        return reportedMessagesDTO;
    }

    @Override
    public SendingStatusDTO getSendingStatus(Long messageID) {
        log.info("getSendingStatus(" + messageID + ") called");

        ReportedMessage reportedMessage = reportedMessageService.getReportedMessage(messageID);

        SendingStatusDTO sendingStatus = reportedRecipientService.getSendingStatusOfRecipients(messageID);
        sendingStatus.setMessageID(messageID);
        sendingStatus.setSendingStarted(reportedMessage.getSendingStarted());

        return sendingStatus;
    }

    @Override
    public List<EmailRecipientDTO> getUnhandledMessageRecipients(int listSize) {
        log.info("getUnhandledMessageRecipients(" + listSize + ") called");

        List<ReportedRecipient> reportedRecipients = reportedRecipientService.getUnhandledReportedRecipients(listSize);
        return emailRecipientDTOConverter.convert(reportedRecipients);
    }

    @Override
    public String getCurrentUserOid() {
        return currentUserComponent.getCurrentUser().getOidHenkilo();
    }

    @Override
    public List<OrganizationDTO> getUserOrganizations() {
        List<OrganizationDTO> organizations = new ArrayList<OrganizationDTO>();
        List<OrganisaatioHenkilo> organisaatioHenkiloList = currentUserComponent.getCurrentUserOrganizations();

        for (OrganisaatioHenkilo organisaatioHenkilo : organisaatioHenkiloList) {
            OrganizationDTO organization = new OrganizationDTO();

            OrganisaatioRDTO organisaatioRDTO = organizationComponent.getOrganization(organisaatioHenkilo
                    .getOrganisaatioOid());
            String organizationName = organizationComponent.getNameOfOrganisation(organisaatioRDTO);

            organization.setOid(organisaatioRDTO.getOid());
            organization.setName(organizationName);

            organizations.add(organization);
        }

        return organizations;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean recipientHandledFailure(EmailRecipientDTO recipient, String result) {
        log.info("recipientHandledFailure(" + recipient.getRecipientID() + ") called");

        ReportedRecipient reportedRecipient = reportedRecipientService.getReportedRecipient(recipient.getRecipientID());
        reportedRecipient.setFailureReason(result);
        reportedRecipient.setSendingSuccesful(GroupEmailConstants.SENDING_FAILED);
        reportedRecipient.setSendingEnded(new Date());
        reportedRecipientService.updateReportedRecipient(reportedRecipient);
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean recipientHandledSuccess(EmailRecipientDTO recipient, String result) {
        log.info("recipientHandledSuccess(" + recipient.getRecipientID() + ") called");

        ReportedRecipient reportedRecipient = reportedRecipientService.getReportedRecipient(recipient.getRecipientID());
        reportedRecipient.setSendingSuccesful(GroupEmailConstants.SENDING_SUCCESFUL);
        reportedRecipient.setSendingEnded(new Date());

        reportedRecipientService.updateReportedRecipient(reportedRecipient);

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Long saveAttachment(FileItem fileItem) throws IOException {
        log.info("saveAttachment(" + fileItem.getName() + ") called");

        ReportedAttachment reportedAttachment = reportedAttachmentConverter.convert(fileItem);
        return reportedAttachmentService.saveReportedAttachment(reportedAttachment);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AttachmentResponse saveAttachment(EmailAttachment emailAttachment) {
        log.info("saveAttachment(" + emailAttachment.getName() + ") called");

        ReportedAttachment reportedAttachment = reportedAttachmentConverter.convert(emailAttachment);
        Long id = reportedAttachmentService.saveReportedAttachment(reportedAttachment);
        return attachmentResponseConverter.convert(id, reportedAttachment);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean startSending(EmailRecipientDTO recipient) {
        log.info("startSending(" + recipient.getEmail() + ") called");

        ReportedRecipient reportedRecipient = reportedRecipientService.getReportedRecipient(recipient.getRecipientID());

        if (reportedRecipient.getSendingStarted() != null) {
            return false;
        }

        reportedRecipient.setSendingStarted(new Date());
        reportedRecipientService.updateReportedRecipient(reportedRecipient);

        return true;
    }

    @Override
    public ReportedAttachment getAttachment(Long attachmentID) {
        log.info("getAttachment(" + attachmentID + ") called");

        ReportedAttachment reportedAttachment = reportedAttachmentService.getReportedAttachment(attachmentID);
        return reportedAttachment;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }
}
