package fi.vm.sade.viestintapalvelu.externalinterface.api.dto;

/**
 * Created by jonimake on 21.11.2014.
 */

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class LOPDto {

    private String id;
    private String name;
    private String address;
    private Boolean providerOrg;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean isProviderOrg() {
        return providerOrg;
    }

    public void setProviderOrg(Boolean providerOrg) {
        this.providerOrg = providerOrg;
    }
}
