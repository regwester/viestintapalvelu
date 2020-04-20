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
package fi.vm.sade.ryhmasahkoposti.resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.*;
import fi.vm.sade.ryhmasahkoposti.api.resource.EmailResource;
import fi.vm.sade.ryhmasahkoposti.common.util.InputCleaner;
import fi.vm.sade.ryhmasahkoposti.service.EmailService;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;
import fi.vm.sade.ryhmasahkoposti.util.CallingProcess;

@Component("EmailResourceImpl")
public class EmailResourceImpl extends GenericResourceImpl implements EmailResource {
    private final static Logger log = LoggerFactory.getLogger(fi.vm.sade.ryhmasahkoposti.resource.EmailResourceImpl.class);

    @Value("${ryhmasahkoposti.from}")
    private String globalFromAddress;
    @Value("${ryhmasahkoposti.default.template.name}")
    private String defaultTemplateName;
    @Value("${ryhmasahkoposti.default.template.language}")
    private String defaultTemplateLanguage;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GroupEmailReportingService groupEmailReportingService;

    @SuppressWarnings("unchecked")
    @Override
    public String addAttachment(HttpServletRequest request, HttpServletResponse response)
        throws IOException, URISyntaxException, ServletException {

        log.debug("Adding attachment: {}", request.getMethod());

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        AttachmentResponse result = null;

        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem item : items) {
                    result = storeAttachment(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            response.setStatus(400);
            response.getWriter().append("Not a multipart request");
        }
        log.debug("Added attachment: {}", result);
        JSONObject json = new JSONObject(result.toMap());
        return json.toString();
    }

    @Override
    public Response ok() {
        return Response.ok("ok").build();
    }

    @Override
    public Response sendEmail(EmailData emailData, boolean sanitize) throws Exception {
        prepareSendEmail(emailData, sanitize);
        System.out.println("emailData.getEmail().getSubject()" + emailData.getEmail().getSubject());
        System.out.println("emailData.getEmail().getBody()" + emailData.getEmail().getBody());
        String sendId = Long.toString(groupEmailReportingService.addSendingGroupEmail(emailData));
        emailService.checkEmailQueues();
        log.debug("DB index is {}", sendId);
        return Response.ok(new EmailSendId(sendId)).build();
    }

    private void prepareSendEmail(EmailData emailData, boolean sanitize) {
    /*
     *  Select source address
     */
        overrideFromAddress(emailData);
        /*
         *  Select source address and template
         */
        chooseTemplate(emailData);
        /*
         * Check if request includes attachment
         * validate it and add to database
         */
        attachIncludedAttachments(emailData);

        /* Sanitize body content */
        if (sanitize) {
            sanitizeInput(emailData);
        }
    }

    @Override
    public Response sendEmailBehindFirewall(EmailData emailData, boolean sanitize) throws Exception {
        log.info("Sending {} emails behind firewall!", emailData.getRecipient().size());
        return sendEmail(emailData, sanitize);
    }

    @Override
    public Response sendEmailAsync(EmailData emailData, boolean sanitize) throws Exception {
        prepareSendEmail(emailData, sanitize);
        ReportedMessage message =  groupEmailReportingService.createSendingGroupEmail(emailData);
        Thread recipientProcessor = new Thread(new AsyncRecipientProcessor(message, emailData.getRecipient()));
        recipientProcessor.start();
        return Response.ok(new EmailSendId(Long.toString(message.getId()))).build();
    }

    @Override
    public Response sendEmailAsyncBehindFirewall(EmailData emailData, boolean sanitize) throws Exception {
        log.info("Sending {} emails asynchroniosly behind firewall!", emailData.getRecipient().size());
        return sendEmailAsync(emailData, sanitize);
    }

    @Override
    public Response getStatus(String sendId) {
        log.debug("getStatus called with ID: {}", sendId);
        SendingStatusDTO sendingStatusDTO = groupEmailReportingService.getSendingStatus(Long.valueOf(sendId));
        return Response.ok(sendingStatusDTO).build();
    }

    @Override
    public Response getPreview(EmailData emailData, boolean sanitize) throws Exception {
        log.debug("getPreview called with EmailData: {}", emailData);
        overrideFromAddress(emailData);
        chooseTemplate(emailData);
        if (sanitize) {
            sanitizeInput(emailData);
        }
        log.debug("getPreview called with EmailData: after template choosing {}", emailData);
        String email = emailService.getEML(emailData, "vastaanottaja@example.com");
        return Response.ok(email).header("Content-Disposition", "attachment; filename=\"preview.eml\"").build();
    }

    @Override
    public Response getPreviewBehindFirewall(EmailData emailData, boolean sanitize) throws Exception {
        log.info("Preview email behind firewall!");
        return getPreview(emailData, sanitize);
    }

    @Override
    public Response getResult(String sendId) {
        log.debug("getResult called with ID: {}", sendId);
        ReportedMessageDTO reportedMessageDTO = groupEmailReportingService.getReportedMessage(Long.valueOf(sendId));
        return Response.ok(reportedMessageDTO).build();
    }

    @Override
    public Response getCount() throws Exception {
        log.debug("Retrieving the count for emails");
        Long count = emailService.getCount(getCurrentUserOid());
        String response = "{\"count\":" + count.toString() + "}";
        return Response.ok(response).build();
    }

    private void overrideFromAddress(EmailData emailData) {
        // Replace whatever from address we got from the client with the global one
        emailData.getEmail().setFrom(globalFromAddress);
    }

    private void sanitizeInput(EmailData emailData) {
        if (emailData.getEmail().getBody() != null) {
            log.debug("Sanitizing email body (before): " + emailData.getEmail().getBody());
            emailData.getEmail().setBody(InputCleaner.cleanHtmlDocument(emailData.getEmail().getBody()));
            log.debug("Sanitizing email body (after): " + emailData.getEmail().getBody());
        } else if (emailData.getEmail().getTemplateId() == null
                        && emailData.getEmail().getTemplateName() == null) {
            throw new BadRequestException("Email without a body or template.");
        }
    }
    private AttachmentResponse storeAttachment(FileItem item) throws Exception {
        AttachmentResponse result = new AttachmentResponse();

        if (!item.isFormField()) {
            Long id = groupEmailReportingService.saveAttachment(item);
            String fileName = item.getName();
            String contentType = item.getContentType();
            byte[] data = item.get();
            result.setFileName(fileName);
            result.setContentType(contentType);
            result.setFileSize(data.length);
            result.setUuid(id.toString());
        }
        return result;
    }

    private void attachIncludedAttachments(EmailData emailData) {
        if (emailData.getEmail() != null) {
            attachIncludedAttachments(emailData.getEmail());
        }
        if (emailData.getRecipient() != null) {
            for (EmailRecipient recipient : emailData.getRecipient()) {
                attachIncludedAttachments(recipient);
            }
        }
    }
    
    private void attachIncludedAttachments(AttachmentContainer container) {
        if (hasAttachments(container)) {
            for (EmailAttachment emailAttachment : container.getAttachments()) {
                AttachmentResponse attachmentResponse = groupEmailReportingService.saveAttachment(emailAttachment);
                container.addAttachInfo(attachmentResponse);
            }
        }
    }
    
    private boolean hasAttachments(AttachmentContainer container) {
        if (container != null) {
            List<? extends EmailAttachment> attachmentList = container.getAttachments();
            return (attachmentList != null && attachmentList.size() > 0); 
        } else {
            return false;
        }
    }
    /*
     * Choose default templates
     */
    // TODO: define default templates by calling process 
    private void chooseTemplate(EmailData emailData) {
        CallingProcess callingProcess = CallingProcess.getByName(emailData.getEmail().getCallingProcess());
        // backwards compatibility 
        // TODO possible to define defaults by process 
        // and make sure that there is default template which  all services can use 
        if (callingProcess != null && callingProcess == CallingProcess.OSOITETIETOJARJESTELMA) {
            // Calling service hasn't given template name. Use default template.
            if (emailData.getEmail().getTemplateName() == null || emailData.getEmail().getTemplateName().isEmpty()) {
                emailData.getEmail().setTemplateName(defaultTemplateName);
            }
            // Calling service hasn't given template language. Use default language.        
            if (emailData.getEmail().getLanguageCode() == null || emailData.getEmail().getLanguageCode().isEmpty()) {
                emailData.getEmail().setLanguageCode(defaultTemplateLanguage);
            }
        }
    }
    private class AsyncRecipientProcessor implements Runnable {

        private final ReportedMessage message;
        private final List<EmailRecipient> recipients;

        protected AsyncRecipientProcessor(ReportedMessage message, List<EmailRecipient> recipients) {
            this.message = message;
            this.recipients = recipients;
        }

        @Override
        public void run() {
            try {
                groupEmailReportingService.processRecipients(message, recipients);
                emailService.checkEmailQueues();
            } catch (Exception e) {
                log.error("Recipient processing failed", e);
            }
        }
    }
}
