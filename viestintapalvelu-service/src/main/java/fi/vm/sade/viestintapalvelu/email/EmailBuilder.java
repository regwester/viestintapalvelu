package fi.vm.sade.viestintapalvelu.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;

@Component
public class EmailBuilder {

    /**
     * Builds email message for given <code>EmailDataSource</code>
     * @param EmailSourceData
     * @return
     * @throws Exception
     */
    
    private static Logger LOGGER = LoggerFactory.getLogger(EmailBuilder.class);
    
    public EmailData buildEmailData(EmailSourceData source) throws Exception {
        LOGGER.debug("Trying to construct emailData for " + source);
        if (source == null) {
            return null;
        }
        List<EmailRecipient> recipients = source.getRecipients();
        
        EmailData emailData = new EmailData();
        emailData.setRecipient(recipients);
        EmailMessage emailMessage = getEmailMessage(source);
        emailData.setEmail(emailMessage);
        return emailData;
    }

    public EmailMessage getEmailMessage(EmailSourceData source) throws Exception {

        EmailMessage message = new EmailMessage();
        Map<TemplateEmailField, Object> emailContext = source.getEmailContext();
        message.setBody((String)emailContext.get(TemplateEmailField.BODY));
        message.setSubject(StringEscapeUtils.unescapeHtml((String)emailContext.get(TemplateEmailField.SUBJECT)));
        
        Map<String, byte[]> attachments = source.getAttachmentData();
        if (attachments != null) {
            Map<String, String> attachmentContentTypes = source.getAttachmentContentType();
            List<EmailAttachment> attachmentList = new ArrayList<EmailAttachment>();
            for (String key : attachments.keySet()) {
                EmailAttachment attachment = new EmailAttachment();
                attachment.setContentType(attachmentContentTypes.get(key));
                attachment.setData(attachments.get(key));
                attachment.setName(key);
                attachmentList.add(attachment);
            }
            message.setAttachments(attachmentList);
        }
        return message;
    }
}