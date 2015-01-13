/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.viestintapalvelu.externalinterface.api.dto;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
