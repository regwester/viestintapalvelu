package fi.vm.sade.ryhmasahkoposti.externalinterface.component;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateDTO;
import fi.vm.sade.ryhmasahkoposti.exception.ExternalInterfaceException;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.TemplateResource;

/**
 * Template component
 * 
 * @author ovmol1
 *
 */
@Component
public class TemplateComponent {

    private static Logger LOGGER = LoggerFactory.getLogger(TemplateComponent.class);

    @Resource
    private TemplateResource templateResourceClient;

    /**
     * Get template content
     * 
     * @param templateName
     * @param languageCode
     * @param type
     * @return
     */
    public TemplateDTO getTemplateContent(String templateName, String languageCode, String type, String applicationPeriod) {
        try {
            return templateResourceClient.getTemplateContent(""+templateName, ""+languageCode, ""+type, ""+applicationPeriod);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ExternalInterfaceException("error.msg.gettingTemplateDataFailed", e);
        }
    }

    public TemplateDTO getTemplateContent(String templateId) {
        try {
            return templateResourceClient.getTemplateByID(templateId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ExternalInterfaceException("error.msg.gettingTemplateDataFailed", e);
        }
    }
    
    public void setTemplateResourceClient(TemplateResource templateResourceClient) {
        this.templateResourceClient = templateResourceClient;
    }
}
