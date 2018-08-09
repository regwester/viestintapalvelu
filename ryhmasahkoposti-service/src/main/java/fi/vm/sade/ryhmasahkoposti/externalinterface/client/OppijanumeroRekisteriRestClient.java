package fi.vm.sade.ryhmasahkoposti.externalinterface.client;

import fi.vm.sade.dto.HenkiloDto;
import fi.vm.sade.generic.rest.CachingRestClient;

import java.io.IOException;

public class OppijanumeroRekisteriRestClient extends CachingRestClient {
    private String webCasUrl;
    private String targetService;
    private String baseUrl;

    public HenkiloDto getHenkilo(String henkiloOid) throws IOException {
        String url = baseUrl + "/henkilo/" + henkiloOid;
        return this.get(url, HenkiloDto.class);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}