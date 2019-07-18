package fi.vm.sade.viestintapalvelu.externalinterface;

import fi.vm.sade.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.javautils.legacy_caching_rest_client.CachingRestClient;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.viestintapalvelu.externalinterface.api.EmailResource;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RyhmasahkopostiRestClient implements EmailResource {
    protected static Logger logger = LoggerFactory.getLogger(RyhmasahkopostiRestClient.class);
    private final CachingRestClient restClient;
    private final String baseUrl;
    private final ObjectMapperProvider objectMapperProvider;


    public RyhmasahkopostiRestClient(String baseUrl, ObjectMapperProvider objectMapperProvider) {
        this.baseUrl = baseUrl;
        this.objectMapperProvider = objectMapperProvider;
        String callerId = "1.2.246.562.10.00000000001.viestintapalvelu.common";
        this.restClient = new CachingRestClient(callerId);
    }

    @Override
    public HttpResponse sendEmail(EmailData emailData) throws IOException {
        String url = baseUrl + "/email";
        String postBody = objectMapperProvider.getContext(EmailData.class).writeValueAsString(emailData);

        logger.warn("Calling url " + url);
        return this.restClient.post(url, "application/json", postBody);
    }

    @Override
    public HttpResponse getPreview(EmailData emailData) throws Exception {
        String url = baseUrl + "/email/preview";
        String postBody = objectMapperProvider.getContext(EmailData.class).writeValueAsString(emailData);

        logger.warn("Calling url " + url);
        return this.restClient.post(url, "application/json", postBody);
    }
}
