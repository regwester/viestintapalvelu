package fi.vm.sade.ryhmasahkoposti.resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailSendId;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import fi.vm.sade.ryhmasahkoposti.api.resource.EmailResource;
import fi.vm.sade.ryhmasahkoposti.service.EmailService;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;
import fi.vm.sade.ryhmasahkoposti.util.CallingProcess;

@Component
public class EmailResourceImpl extends GenericResourceImpl implements EmailResource {
    private final static Logger log = LoggerFactory.getLogger(fi.vm.sade.ryhmasahkoposti.resource.EmailResourceImpl.class);

    @Value("${ryhmasahkoposti.from}")
    private String globalFromAddress;
    @Value("ryhmasahkoposti.default.template.name")
    private String defaultTemplateName;
    @Value("ryhmasahkoposti.default.template.language")
    private String defaultTemplateLanguage;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GroupEmailReportingService groupEmailReportingService;

    @SuppressWarnings("unchecked")
    @Override
    public String addAttachment(@Context HttpServletRequest request, @Context HttpServletResponse response)
        throws IOException, URISyntaxException, ServletException {

        log.info("Adding attachment " + request.getMethod());

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        AttachmentResponse result = null;

        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List<FileItem> items = upload.parseRequest(request);
                Iterator<FileItem> iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    result = storeAttachment(item);
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            response.setStatus(400);
            response.getWriter().append("Not a multipart request");
        }
        log.info("Added attachment: " + result);
        JSONObject json = new JSONObject(result.toMap());
        return json.toString();
    }

    @Override
    public Response ok() {
        Response response = Response.ok("ok").build();
        return response;
    }

    // TODO: Validate the values we get from the client (are empty subject/body/recipients ok?)
    @Override
<<<<<<< HEAD
    public Response sendEmail(EmailData emailData) {
        /*
         *  Select source address
         */
        handleFromAddress(emailData);
        /*
         *  Select source address and template
         */
        chooseTemplate(emailData);
        /*
         * Check if request includes attachment 
         * validate it and add to database
         */
        handleIncludedAttachments(emailData);

        try {
            String sendId = Long.toString(groupEmailReportingService.addSendingGroupEmail(emailData));
            log.info("DB index is " + sendId);
            return Response.ok(new EmailSendId(sendId)).build();
        } catch (ExternalInterfaceException e) {
            log.error("Problems in getting data from external interfaces, " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (Exception e) {
            log.error("Problems in writing send data info to DB, " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(RestConstants.INTERNAL_SERVICE_ERROR).build();
        }
    }

    @Override
    public Response getStatus(String sendId) {
        log.error("getStatus called with ID: " + sendId + ".");
        try {
            SendingStatusDTO sendingStatusDTO = groupEmailReportingService.getSendingStatus(Long.valueOf(sendId));
            return Response.ok(sendingStatusDTO).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(RestConstants.INTERNAL_SERVICE_ERROR).build();
        }
    }

    @Override
    public Response getResult(String sendId) {
        log.info("getResult called with ID: " + sendId + ".");
        try {
            ReportedMessageDTO reportedMessageDTO = groupEmailReportingService.getReportedMessage(Long.valueOf(sendId));
            return Response.ok(reportedMessageDTO).build();
        } catch (Exception e) {
            log.error("Problems in getting group email data from DB, " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(RestConstants.INTERNAL_SERVICE_ERROR).build();
        }
=======
    public Response sendEmail(EmailData emailData) throws Exception {
        String sendId = Long.toString(groupEmailReportingService.addSendingGroupEmail(emailData));
        log.info("DB index is " + sendId);
        return Response.ok(new EmailSendId(sendId)).build();
    }

    @Override
    public Response sendEmailStatus(String sendId) throws Exception {
        log.error("sendEmailStatus called with ID: " + sendId + ".");
        SendingStatusDTO sendingStatusDTO = groupEmailReportingService.getSendingStatus(Long.valueOf(sendId));
        return Response.ok(sendingStatusDTO).build();
    }

    // TODO: Validate the values we get from the client (are empty subject/body/recipients ok?)
    @Override
    public Response sendEmailWithTemplate(EmailData emailData) throws Exception {
        // Replace whatever from address we got from the client with the global one
        emailData.getEmail().setFrom(globalFromAddress);
        
        // Calling service hasn't given template name. Use default template.
        if (emailData.getEmail().getTemplateName() == null || emailData.getEmail().getTemplateName().isEmpty()) {
            emailData.getEmail().setTemplateName(defaultTemplateName);
        }
        // Calling service hasn't given template language. Use default language.        
        if (emailData.getEmail().getLanguageCode() == null || emailData.getEmail().getLanguageCode().isEmpty()) {
            emailData.getEmail().setLanguageCode(defaultTemplateLanguage);
        }

        String sendId = Long.toString(groupEmailReportingService.addSendingGroupEmail(emailData));
        log.info("DB index is " + sendId);
        return Response.ok(new EmailSendId(sendId)).build();
    }

    @Override
    public Response sendPdfByEmail(EmailData emailData) throws Exception {
        EmailAttachment emailAttachment = emailData.getEmail().getAttachments().get(0);
        AttachmentResponse attachmentResponse = groupEmailReportingService.saveAttachment(emailAttachment);
        emailData.getEmail().addAttachInfo(attachmentResponse);
        String sendId = Long.toString(groupEmailReportingService.addSendingGroupEmail(emailData));
        return Response.ok(new EmailSendId(sendId)).build();
    }

    @Override
    public Response sendResult(String sendId) {
        log.info("sendResult called with ID: " + sendId + ".");
        ReportedMessageDTO reportedMessageDTO = groupEmailReportingService.getReportedMessage(Long.valueOf(sendId));
        return Response.ok(reportedMessageDTO).build();
>>>>>>> 361468a60de09b0f40acbc5584c018bb962f647f
    }

    @Override
    public Response getCount() throws Exception {
        log.debug("Retrieving the count for emails");
        Long count = emailService.getCount(getCurrentUserOid());
        String response = "{\"count\":" + count.toString() + "}";
        return Response.ok(response).build();
    }

    private void handleFromAddress(EmailData emailData) {
        // Replace whatever from address we got from the client with the global one
        emailData.getEmail().setFrom(globalFromAddress);
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
    
    private void handleIncludedAttachments(EmailData emailData) {
        if (hasAttachments(emailData)) {
            for (EmailAttachment emailAttachment : emailData.getEmail().getAttachments()) {
                AttachmentResponse attachmentResponse = groupEmailReportingService.saveAttachment(emailAttachment);
                emailData.getEmail().addAttachInfo(attachmentResponse);
            }
        }
    }
    
    private boolean hasAttachments(EmailData emailData) {
        if (emailData.getEmail() != null) {
            List<? extends EmailAttachment> attachmentList = emailData.getEmail().getAttachments();
            return (attachmentList != null && attachmentList.size() > 0); 
        } else  {
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
}
