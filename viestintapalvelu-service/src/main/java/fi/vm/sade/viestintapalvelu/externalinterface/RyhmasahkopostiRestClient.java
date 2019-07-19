package fi.vm.sade.viestintapalvelu.externalinterface;

import fi.vm.sade.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.javautils.legacy_caching_rest_client.CachingRestClient;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.viestintapalvelu.externalinterface.api.EmailResource;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class RyhmasahkopostiRestClient implements EmailResource {
    protected static Logger logger = LoggerFactory.getLogger(RyhmasahkopostiRestClient.class);
    private final CachingRestClient restClient;

    @Value("${ryhmasahkoposti.base}")
    private String baseUrl;

    @Value("${web.url.cas}")
    private String webCasUrl;

    @Value("${ryhmasahkoposti.base}")
    private String casService;

    @Value("${ryhmasahkoposti.app.username.to.viestintapalvelu}")
    private String username;

    @Value("${ryhmasahkoposti.app.password.to.viestintapalvelu}")
    private String password;

    private final ObjectMapperProvider objectMapperProvider;


    public RyhmasahkopostiRestClient(ObjectMapperProvider objectMapperProvider) {
        this.objectMapperProvider = objectMapperProvider;
        String callerId = "1.2.246.562.10.00000000001.viestintapalvelu.common";
        this.restClient = new CachingRestClient(callerId);
        this.restClient.setWebCasUrl(webCasUrl);
        this.restClient.setUsername(username);
        this.restClient.setPassword(password);
        this.restClient.setCasService(casService);
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
