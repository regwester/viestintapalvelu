package fi.vm.sade.externalinterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fi.vm.sade.dto.OrganisaatioHenkiloDto;
import fi.vm.sade.generic.rest.CachingRestClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class KayttooikeusRestClient extends CachingRestClient {
    private String baseUrl;

    public List<OrganisaatioHenkiloDto> getOrganisaatioHenkilo(String henkiloOid) throws IOException {
        Type listType = new TypeToken<ArrayList<OrganisaatioHenkiloDto>>(){}.getType();
        String url = baseUrl + "/henkilo/" + henkiloOid + "/organisaatiohenkilo";
        String resultJsonString = this.getAsString(url);
        return new Gson().fromJson(resultJsonString, listType);
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
