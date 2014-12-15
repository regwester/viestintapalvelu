package fi.vm.sade.viestintapalvelu.externalinterface.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Set;

/**
 * Created by jonimake on 12.12.2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HakukohdeDTO {
    public String hakukohteenNimi;
    public String hakuOid;
    public Map<String, String> hakukohteenNimet;
    public Map<String, String> tarjoajaNimet;
    public Set<String> tarjoajaOids;

    public String getHakukohteenNimi() {
        return hakukohteenNimi;
    }

    public void setHakukohteenNimi(String hakukohteenNimi) {
        this.hakukohteenNimi = hakukohteenNimi;
    }

    public String getHakuOid() {
        return hakuOid;
    }

    public void setHakuOid(String hakuOid) {
        this.hakuOid = hakuOid;
    }

    public Map<String, String> getHakukohteenNimet() {
        return hakukohteenNimet;
    }

    public void setHakukohteenNimet(Map<String, String> hakukohteenNimet) {
        this.hakukohteenNimet = hakukohteenNimet;
    }

    public Map<String, String> getTarjoajaNimet() {
        return tarjoajaNimet;
    }

    public void setTarjoajaNimet(Map<String, String> tarjoajaNimet) {
        this.tarjoajaNimet = tarjoajaNimet;
    }

    public Set<String> getTarjoajaOids() {
        return tarjoajaOids;
    }

    public void setTarjoajaOids(Set<String> tarjoajaOids) {
        this.tarjoajaOids = tarjoajaOids;
    }
}
