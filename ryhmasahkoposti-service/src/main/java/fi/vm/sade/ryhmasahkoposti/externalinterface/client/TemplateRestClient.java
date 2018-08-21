package fi.vm.sade.ryhmasahkoposti.externalinterface.client;

import fi.vm.sade.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.generic.rest.CachingRestClient;
import fi.vm.sade.properties.OphProperties;
import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.TemplateResource;

import java.io.IOException;
import java.util.Properties;

public class TemplateRestClient extends CachingRestClient implements TemplateResource {
    private final OphProperties.UrlResolver urlResolver;
    private final ObjectMapperProvider objectMapperProvider;

    public TemplateRestClient(String viestintaPalveluUrl, ObjectMapperProvider objectMapperProvider) {
        this.urlResolver = new OphProperties() {
            UrlResolver createUrlResolver() {
                Properties urlsConfig = new Properties();
                urlsConfig.setProperty("viestintapalvelu.baseUrl", viestintaPalveluUrl);
                urlsConfig.setProperty("viestintapalvelu.templatecontent.by.id", "/viestintapalvelu/api/v1/template/$1/$2/getTemplateContent");
                urlsConfig.setProperty("viestintapalvelu.templatecontent.by.name", "/viestintapalvelu/api/v1/template/$1/$2/$3/getTemplateContent");
                urlsConfig.setProperty("viestintapalvelu.templatecontent.by.name.with.applicationperiod", "/viestintapalvelu/api/v1/template/$1/$2/$3/$4/getTemplateContent");
                return new UrlResolver(urlsConfig);
            }
        }.createUrlResolver();
        this.objectMapperProvider = objectMapperProvider;
    }

    @Override
    public TemplateDTO getTemplateContent(String templateName, String languageCode, String type, String applicationPeriod) throws IOException {
        String url = this.urlResolver.url("viestintapalvelu.templatecontent.by.name.with.applicationperiod",
            templateName, languageCode, type, applicationPeriod);
        return fetchTemplate(url);
    }

    @Override
    public TemplateDTO getTemplateContent(String templateName, String languageCode, String type) throws IOException {
        return fetchTemplate(this.urlResolver.url("viestintapalvelu.templatecontent.by.name",
            templateName, languageCode, type));
    }

    @Override
    public TemplateDTO getTemplateByID(String templateId, String type) throws IOException {
        return fetchTemplate(this.urlResolver.url("viestintapalvelu.templatecontent.by.id", templateId, type));
    }

    private TemplateDTO fetchTemplate(String url) throws IOException {
        String jsonString = this.getAsString(url);
        return objectMapperProvider.getContext(TemplateDTO.class).readValue(jsonString, TemplateDTO.class);
    }
}
