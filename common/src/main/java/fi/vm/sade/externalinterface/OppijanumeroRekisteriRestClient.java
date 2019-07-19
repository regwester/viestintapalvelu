package fi.vm.sade.externalinterface;

import fi.vm.sade.dto.HenkiloDto;
import fi.vm.sade.javautils.legacy_caching_rest_client.CachingRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OppijanumeroRekisteriRestClient {
    protected static Logger logger = LoggerFactory.getLogger(OppijanumeroRekisteriRestClient.class);
    private final CachingRestClient restClient;
    private final String baseUrl;

    private String webCasUrl;
    private String username;
    private String password;
    private String casService;

    public OppijanumeroRekisteriRestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        String callerId = "1.2.246.562.10.00000000001.viestintapalvelu.common";
        this.restClient = new CachingRestClient(callerId);
        this.restClient.setWebCasUrl(webCasUrl);
        this.restClient.setUsername(username);
        this.restClient.setPassword(password);
        this.restClient.setCasService(casService);
    }

    public HenkiloDto getHenkilo(String henkiloOid) throws IOException {
        String url = baseUrl + "/henkilo/" + henkiloOid;
        return this.restClient.get(url, HenkiloDto.class);
    }
}
