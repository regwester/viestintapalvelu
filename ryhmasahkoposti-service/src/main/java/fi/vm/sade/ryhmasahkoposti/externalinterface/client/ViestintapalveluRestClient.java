package fi.vm.sade.ryhmasahkoposti.externalinterface.client;

import fi.vm.sade.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.javautils.legacy_caching_rest_client.CachingRestClient;
import fi.vm.sade.properties.OphProperties;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.AttachmentResource;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.TemplateResource;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.UrisContainerDto;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class ViestintapalveluRestClient implements TemplateResource, AttachmentResource {
    protected static Logger logger = LoggerFactory.getLogger(ViestintapalveluRestClient.class);
    private final CachingRestClient restClient;
    private final OphProperties.UrlResolver urlResolver;
    private final ObjectMapperProvider objectMapperProvider;

    public ViestintapalveluRestClient(String viestintaPalveluUrl, ObjectMapperProvider objectMapperProvider) {
        this.urlResolver = new OphProperties() {
            UrlResolver createUrlResolver() {
                Properties urlsConfig = new Properties();
                urlsConfig.setProperty("viestintapalvelu.baseUrl", viestintaPalveluUrl);
                urlsConfig.setProperty("viestintapalvelu.templatecontent.by.id", "/viestintapalvelu/api/v1/template/$1/$2/getTemplateContent");
                urlsConfig.setProperty("viestintapalvelu.templatecontent.by.name", "/viestintapalvelu/api/v1/template/$1/$2/$3/getTemplateContent");
                urlsConfig.setProperty("viestintapalvelu.templatecontent.by.name.with.applicationperiod", "/viestintapalvelu/api/v1/template/$1/$2/$3/$4/getTemplateContent");
                urlsConfig.setProperty("viestintapalvelu.attachment.getByUri", "/viestintapalvelu/api/v1/attachment/getByUri/$1");
                urlsConfig.setProperty("viestintapalvelu.attachment.urisDownloaded", "/viestintapalvelu/api/v1/attachment/urisDownloaded");
                return new UrlResolver(urlsConfig);
            }
        }.createUrlResolver();
        this.objectMapperProvider = objectMapperProvider;

        String callerId = "1.2.246.562.10.00000000001.viestintapalvelu.ryhmasahkoposti-service";
        this.restClient = new CachingRestClient(callerId);
    }

    @Override
    public TemplateDTO getTemplateContent(String templateName, String languageCode, String type, String applicationPeriod) throws IOException {
        logger.warn("Calling url viestintapalvelu.templatecontent.by.name.with.applicationperiod");
        String url = this.urlResolver.url("viestintapalvelu.templatecontent.by.name.with.applicationperiod",
            templateName, languageCode, type, applicationPeriod);
        return fetchTemplate(url);
    }

    @Override
    public TemplateDTO getTemplateContent(String templateName, String languageCode, String type) throws IOException {
        logger.warn("Calling url viestintapalvelu.templatecontent.by.name");
        return fetchTemplate(this.urlResolver.url("viestintapalvelu.templatecontent.by.name",
            templateName, languageCode, type));
    }

    @Override
    public TemplateDTO getTemplateByID(String templateId, String type) throws IOException {
        logger.warn("Calling url viestintapalvelu.templatecontent.by.id");
        return fetchTemplate(this.urlResolver.url("viestintapalvelu.templatecontent.by.id", templateId, type));
    }

    private TemplateDTO fetchTemplate(String url) throws IOException {
        String jsonString = this.restClient.getAsString(url);
        return objectMapperProvider.getContext(TemplateDTO.class).readValue(jsonString, TemplateDTO.class);
    }


    @Override
    public EmailAttachment downloadByUri(String uri) throws IOException {
        logger.warn("Calling url viestintapalvelu.attachment.getByUri");
        String url = this.urlResolver.url("viestintapalvelu.attachment.getByUri", uri);
        String jsonString = this.restClient.getAsString(url);
        return objectMapperProvider.getContext(EmailAttachment.class).readValue(jsonString, EmailAttachment.class);
    }

    @Override
    public HttpResponse deleteByUris(UrisContainerDto urisContainerDto) throws IOException {
        logger.warn("Calling url viestintapalvelu.attachment.urisDownloaded");
        String url = this.urlResolver.url("viestintapalvelu.attachment.urisDownloaded");
        String body = objectMapperProvider.getContext(UrisContainerDto.class).writeValueAsString(urisContainerDto);
        String contentType = "application/json;charset=utf-8";
        return this.restClient.post(url, contentType, body);
    }
}
