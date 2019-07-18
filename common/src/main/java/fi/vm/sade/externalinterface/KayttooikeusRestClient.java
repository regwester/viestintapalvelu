package fi.vm.sade.externalinterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fi.vm.sade.dto.OrganisaatioHenkiloDto;
import fi.vm.sade.javautils.legacy_caching_rest_client.CachingRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class KayttooikeusRestClient {
    protected static Logger logger = LoggerFactory.getLogger(KayttooikeusRestClient.class);
    private final CachingRestClient restClient;
    private final String baseUrl;

    public KayttooikeusRestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        String callerId = "1.2.246.562.10.00000000001.viestintapalvelu.common";
        this.restClient = new CachingRestClient(callerId);
    }

    public List<OrganisaatioHenkiloDto> getOrganisaatioHenkilo(String henkiloOid) throws IOException {
        Type listType = new TypeToken<ArrayList<OrganisaatioHenkiloDto>>(){}.getType();
        String url = baseUrl + "/henkilo/" + henkiloOid + "/organisaatiohenkilo";
        String resultJsonString = this.restClient.getAsString(url);
        return new Gson().fromJson(resultJsonString, listType);
    }
}
