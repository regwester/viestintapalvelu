package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;

public class OrganizationDTO implements Serializable {
    private static final long serialVersionUID = -2866717483329690231L;
    private String oid;
    private String name;
    
    public String getOid() {
        return oid;
    }
    public void setOid(String oid) {
        this.oid = oid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
