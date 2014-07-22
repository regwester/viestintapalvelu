package fi.vm.sade.viestintapalvelu.dto.query;

import java.io.Serializable;

public class LetterReportQueryDTO implements Serializable {
    private static final long serialVersionUID = -8516467453321234990L;
    private String organizationOid;
    private String searchArgument;
    
    public String getOrganizationOid() {
        return organizationOid;
    }
    
    public void setOrganizationOid(String organizationOid) {
        this.organizationOid = organizationOid;
    }
    
    public String getSearchArgument() {
        return searchArgument;
    }
    
    public void setSearchArgument(String searchArgument) {
        this.searchArgument = searchArgument;
    }
}
