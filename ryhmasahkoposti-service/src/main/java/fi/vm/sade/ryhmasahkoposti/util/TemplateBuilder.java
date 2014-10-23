package fi.vm.sade.ryhmasahkoposti.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import fi.vm.sade.ryhmasahkoposti.common.util.InputCleaner;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.*;
import fi.vm.sade.ryhmasahkoposti.service.TemplateService;

@Component
public class TemplateBuilder {
    public static final String EMAIL_BODY_TEMPLATE_CONTENT = "email_body";
    public static final String VARIABLE_NAME_INLINE_STYLES = "tyylit";
    public static final String VARIABLE_NAME_LETTER_DATE = "letterDate";

    private static Logger LOGGER = LoggerFactory.getLogger(TemplateBuilder.class);

    private VelocityEngine templateEngine = new VelocityEngine();

    @Autowired
    private TemplateService templateService;

    private static final int MAX_CACHE_ENTRIES = 10;
    private Map<String, TemplateDTO> templateCacheById = new LinkedHashMap<String, TemplateDTO>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, TemplateDTO> eldest) {
            return size() > MAX_CACHE_ENTRIES;
        }
    };

    public TemplateDTO getTemplate(EmailRecipientMessage message) {
        TemplateDTO result = null;
        if (null != message.getTemplateId()) {
            // Is is necessary to fetch template for every receiver?
            // Templates won't change (new versions will have new id), so this little optimization here:
            result = templateCacheById.get(message.getTemplateId());
            if (result != null) {
                return result;
            }
            result = getTemplate(message.getTemplateId());
            templateCacheById.put(message.getTemplateId(), result);
        } else if (null != message.getTemplateName()) {
            result = getTemplate(message.getTemplateName(), message.getLanguageCode(), TemplateDTO.TYPE_EMAIL, message.getHakuOid());
            if (result != null && result.getId() != null) {
                message.setTemplateId(""+result.getId());
                templateCacheById.put(message.getTemplateId(), result);
            }
        }
        return result;
    }

    public TemplateDTO getTemplate(EmailData emailData) {
        return getTemplate(emailData.getEmail().getTemplateName(), emailData.getEmail().getLanguageCode(), TemplateDTO.TYPE_EMAIL, emailData.getEmail()
                .getHakuOid());
    }

    private TemplateDTO getTemplate(String id) {
        TemplateDTO templateDTO = null;
        try {
            templateDTO = templateService.getTemplate(id);
            LOGGER.debug("Loaded template with id: {}", id);
        } catch (Exception e) {
            LOGGER.error("Failed to load template for with id : {}", id, e);
        }
        if (templateDTO != null) {
            LOGGER.debug("Template found: {}", templateDTO);
        }
        return templateDTO;
    }

    private TemplateDTO getTemplate(String templateName, String languageCode, String type, String hakuOid) {
        TemplateDTO templateDTO = null;
        try {
            templateDTO = templateService.getTemplate(templateName, languageCode, TemplateDTO.TYPE_EMAIL, hakuOid);
            LOGGER.debug("Loaded template: {} for {}", templateDTO, templateName);
        } catch (Exception e) {
            LOGGER.error("Failed to load template for templateName: {}, languageCode={}", templateName, languageCode, e);
        }
        if (templateDTO != null) {
            LOGGER.debug("Template found: {}", templateDTO);
        }
        return templateDTO;
    }

    /**
     * Build template content without any replacements
     * 
     * @param template
     * @return
     */
    public String buildTemplate(TemplateDTO template, EmailData emailData) {

        if (template == null) {
            LOGGER.error("Template is null");
            return null;
        }

        // Create a list to sort
        List<TemplateContentDTO> contentList = new ArrayList<TemplateContentDTO>();
        for (TemplateContentDTO templateContent : template.getContents()) {
            contentList.add(templateContent);
        }

        // Sort
        Collections.sort(contentList);

        // Create page
        StringBuffer result = new StringBuffer();
        for (TemplateContentDTO tc : contentList) {
//            String page = createPage(template, emailData, tc.getContent().getBytes());
            result.append(tc.getContent());
        }

        return result.toString();
    }

    public EmailRecipientMessage applyTemplate(EmailRecipientMessage message) {
        TemplateDTO template = getTemplate(message);
        if (template != null) {
            String content = null;
            for (TemplateContentDTO c : template.getContents()) {
               if (EMAIL_BODY_TEMPLATE_CONTENT.equalsIgnoreCase(c.getName())) {
                   content = c.getContent();
                   break;
               }
            }
            // is there anything to do
            if (content == null) {
                LOGGER.warn("No {} part in template {}", EMAIL_BODY_TEMPLATE_CONTENT, template.getId());
                return message;
            }
            Map<String,Object> dataContext = createDataContext(template, message);
            
            dataContext.put(VARIABLE_NAME_INLINE_STYLES, template.getStyles());
            dataContext.put(VARIABLE_NAME_LETTER_DATE, new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
            // Replace sisalto parameter with Email's body if there is one:
            if (message.getBody() != null) {
                dataContext.put(ReplacementDTO.NAME_EMAIL_BODY, InputCleaner.cleanHtmlFragment(message.getBody()));
            }
            // Replace Email's subject with otsikko parameter if there is one (always replaced)
            if (dataContext.get(ReplacementDTO.NAME_EMAIL_SUBJECT) != null) {
                message.setSubject(dataContext.get(ReplacementDTO.NAME_EMAIL_SUBJECT).toString());
            }
            // reply-to-personal overrides possible reply-to, default to email's replyTo
            if (dataContext.get(ReplacementDTO.NAME_EMAIL_REPLY_TO_PERSONAL) != null) {
                message.setReplyTo(dataContext.get(ReplacementDTO.NAME_EMAIL_REPLY_TO_PERSONAL).toString());
            } else if (dataContext.get(ReplacementDTO.NAME_EMAIL_REPLY_TO) != null) {
                message.setReplyTo(dataContext.get(ReplacementDTO.NAME_EMAIL_REPLY_TO).toString());
            }
            if (dataContext.get(ReplacementDTO.NAME_EMAIL_SENDER_NAME_PERSONAL) != null) {
                message.setSender(dataContext.get(ReplacementDTO.NAME_EMAIL_SENDER_NAME_PERSONAL).toString());
            }
            if (dataContext.get(ReplacementDTO.NAME_EMAIL_SENDER_FROM) != null) {
                message.setFrom(dataContext.get(ReplacementDTO.NAME_EMAIL_SENDER_FROM).toString());
            }
            if (dataContext.get(ReplacementDTO.NAME_EMAIL_REPLY_TO_PERSONAL) != null) {
                message.setReplyTo(dataContext.get(ReplacementDTO.NAME_EMAIL_REPLY_TO_PERSONAL).toString());
            } else if (dataContext.get(ReplacementDTO.NAME_EMAIL_REPLY_TO) != null) {
                message.setReplyTo(dataContext.get(ReplacementDTO.NAME_EMAIL_REPLY_TO).toString());
            }

            if (message.getSourceRegister() != null) {
                Map<String, Boolean> sourceRegisters = new HashMap<String, Boolean>();

                for (SourceRegister sourceRegister : message.getSourceRegister()) {
                    sourceRegisters.put(sourceRegister.getName(), true);
                }

                dataContext.put("sourceregisters", sourceRegisters);
            }
            
            StringWriter writer = new StringWriter();
            templateEngine.evaluate(new VelocityContext(dataContext), writer, "LOG",
                    new InputStreamReader(new ByteArrayInputStream(content.getBytes())));
            message.setBody(writer.toString());
        } else {
            LOGGER.warn("No template used for message with templateId={}", message.getTemplateId());
        }
        return message;
    }

    /**
     * Build template content 
     * @param messageReplacements
     * @param recipientReplacements
     * @return
     */
    public String buildTempleMessage(String message, List<ReplacementDTO> messageReplacements, List<ReportedRecipientReplacementDTO> recipientReplacements) {
        Map<String, Object> replacements = new HashMap<String, Object>();

        // Message replacements exist
        if (messageReplacements != null) {
            for (ReplacementDTO repl : messageReplacements) {
                replacements.put(repl.getName(), repl.getDefaultValue());
            }
        }

        // Place user replacements
        if (recipientReplacements != null) {
            for (ReportedRecipientReplacementDTO repl : recipientReplacements) {
                replacements.put(repl.getName(), repl.getEffectiveValue());
            }
        }

        return createContent(message, replacements);
    }

   
    
    /**
     * Create message content
     * 
     * @param message
     * @param replacements
     * @return
     */
    private String createContent(String message, Map<String, Object> replacements) {

        @SuppressWarnings("unchecked")
        Map<String, Object> dataContext = createDataContext(replacements);

        StringWriter writer = new StringWriter();
        templateEngine.evaluate(new VelocityContext(dataContext), writer, "LOG", new InputStreamReader(new ByteArrayInputStream(message.getBytes())));
        return writer.toString();
    }

    private Map<String, Object> createDataContext(TemplateDTO template, EmailRecipientMessage message) {
        Map<String, Object> replacements = new HashMap<String, Object>();
        for (ReplacementDTO r : template.getReplacements()) {
            replacements.put(r.getName(), r.getDefaultValue());
        }
        for (ReplacementDTO r : message.getMessageReplacements()) {
            replacements.put(r.getName(), r.getDefaultValue());
        }
        if (message.getRecipient().getRecipientReplacements() != null) {
            for (ReportedRecipientReplacementDTO r : message.getRecipient().getRecipientReplacements()){
                replacements.put(r.getName(), r.getValue());
            }
        }
        return createDataContext(replacements);
    }
    
    /**
     * Create data context
     * 
     * @param replacementsList
     * @return
     */
    private Map<String, Object> createDataContext(Map<String, Object>... replacementsList) {
        Map<String, Object> data = new HashMap<String, Object>();
        for (Map<String, Object> replacements : replacementsList) {
            if (replacements == null) {
                continue;
            }

            for (String key : replacements.keySet()) {
                if (replacements.get(key) instanceof String) {
                    data.put(key, InputCleaner.cleanHtmlFragment((String) replacements.get(key)));
                } else {
                    data.put(key, replacements.get(key));
                }
            }
        }
        
        data.put("letterDate", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        return data;
    }
    
    /**
     * Create page
     * 
     * @param template
     * @param pageContent
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws DocumentException
     */
    private String createPage(TemplateDTO template, EmailData emailData, byte[] pageContent) {
        Map<String, Object> dataContext = new HashMap<String, Object>();
        String styles = template.getStyles();

        if (styles == null) {
            styles = "";
        }

        dataContext.put("tyylit", styles);
        dataContext.put("letterDate", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));

        if (emailData.getEmail().getBody() != null) {
            dataContext.put("sisalto", InputCleaner.cleanHtmlDocument(emailData.getEmail().getBody()));
        }

        if (emailData.getEmail().getSourceRegister() != null) {
            Map<String, Boolean> sourceRegisters = new HashMap<String, Boolean>();

            for (SourceRegister sourceRegister : emailData.getEmail().getSourceRegister()) {
                sourceRegisters.put(sourceRegister.getName(), true);
            }

            dataContext.put("sourceregisters", sourceRegisters);
        }

        StringWriter writer = new StringWriter();
        templateEngine.evaluate(new VelocityContext(dataContext), writer, "LOG", new InputStreamReader(new ByteArrayInputStream(pageContent)));
        return writer.toString();

    }
}
