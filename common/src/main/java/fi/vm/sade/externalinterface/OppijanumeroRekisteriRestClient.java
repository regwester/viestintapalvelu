package fi.vm.sade.externalinterface;

import fi.vm.sade.dto.HenkiloDto;
import fi.vm.sade.javautils.legacy_caching_rest_client.CachingRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class OppijanumeroRekisteriRestClient {
    protected static Logger logger = LoggerFactory.getLogger(OppijanumeroRekisteriRestClient.class);
    private final CachingRestClient restClient;

    @Value("${oppijanumerorekisteri-service.base}")
    private String baseUrl;

    @Value("${oppijanumerorekisteri-service.base}/j_spring_cas_security_check")
    private String casService;

    @Value("${web.url.cas}")
    private String webCasUrl;

    @Value("${ryhmasahkoposti.app.username.to.viestintapalvelu}")
    private String username;

    @Value("${ryhmasahkoposti.app.password.to.viestintapalvelu}")
    private String password;

    public OppijanumeroRekisteriRestClient() {
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
