package fi.vm.sade.ryhmasahkoposti.externalinterface.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fi.vm.sade.dto.OrganisaatioHenkiloDto;
import fi.vm.sade.generic.rest.CachingRestClient;
import fi.vm.sade.properties.OphProperties;
import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateDTO;
import org.apache.xml.resolver.apps.resolver;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TemplateRestClient extends CachingRestClient implements TemplateResource {
    private final String viestintaPalveluUrl;
    private final OphProperties.UrlResolver urlResolver;

    public TemplateRestClient(String viestintaPalveluUrl) {
        this.viestintaPalveluUrl = viestintaPalveluUrl;
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
    }

    public List<OrganisaatioHenkiloDto> getOrganisaatioHenkilo(String henkiloOid) throws IOException {
        Type listType = new TypeToken<ArrayList<OrganisaatioHenkiloDto>>(){}.getType();
        String url = viestintaPalveluUrl + "/henkilo/" + henkiloOid + "/organisaatiohenkilo";
        String resultJsonString = this.getAsString(url);
        return new Gson().fromJson(resultJsonString, listType);
    }

/*
    @GET
    @Produces("application/json")
    @Path("/{templateName}/{languageCode}/{type}/{applicationPeriod}/getTemplateContent")
    TemplateDTO getTemplateContent(@PathParam("templateName") String templateName, @PathParam("languageCode") String languageCode,
                                   @PathParam("type") String type, @PathParam("applicationPeriod") String applicationPeriod) throws IOException, DocumentException;

    @GET
    @Produces("application/json")
    @Path("/{templateName}/{languageCode}/{type}/getTemplateContent")
    TemplateDTO getTemplateContent(@PathParam("templateName") String templateName, @PathParam("languageCode") String languageCode,
                                   @PathParam("type") String type) throws IOException, DocumentException;

    @GET
    @Path("/{templateId}/{type}/getTemplateContent")
    @Produces("application/json")
    TemplateDTO getTemplateByID(@PathParam("templateId") String templateId, @PathParam("type") String type);
* */
    @Override
    public TemplateDTO getTemplateContent(String templateName, String languageCode, String type, String applicationPeriod) throws IOException {
        String url = this.urlResolver.url("viestintapalvelu.templatecontent.by.name.with.applicationperiod", templateName, languageCode, type, applicationPeriod);
        return this.get(url, TemplateDTO.class);
    }

    @Override
    public TemplateDTO getTemplateContent(String templateName, String languageCode, String type) throws IOException {
        String url = this.urlResolver.url("viestintapalvelu.templatecontent.by.name", templateName, languageCode, type);
        return this.get(url, TemplateDTO.class);
    }

    @Override
    public TemplateDTO getTemplateByID(String templateId, String type) throws IOException {
        String url = this.urlResolver.url("viestintapalvelu.templatecontent.by.id", templateId, type);
        return this.get(url, TemplateDTO.class);
    }
}
