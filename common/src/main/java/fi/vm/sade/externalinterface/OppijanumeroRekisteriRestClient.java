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

    public OppijanumeroRekisteriRestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        String callerId = "1.2.246.562.10.00000000001.viestintapalvelu.common";
        this.restClient = new CachingRestClient(callerId);
    }

    public HenkiloDto getHenkilo(String henkiloOid) throws IOException {
        String url = baseUrl + "/henkilo/" + henkiloOid;
        return this.restClient.get(url, HenkiloDto.class);
    }
}
