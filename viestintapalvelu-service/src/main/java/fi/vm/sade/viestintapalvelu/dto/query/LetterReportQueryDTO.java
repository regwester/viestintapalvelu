package fi.vm.sade.viestintapalvelu.dto.query;

import java.io.Serializable;
import java.util.List;

public class LetterReportQueryDTO implements Serializable {
    private static final long serialVersionUID = -8516467453321234990L;
    private List<String> organizationOids;
    private String searchArgument;
    
    public List<String> getOrganizationOids() {
        return organizationOids;
    }
    
    public void setOrganizationOids(List<String> organizationOids) {
        this.organizationOids = organizationOids;
    }
    
    public String getSearchArgument() {
        return searchArgument;
    }
    
    public void setSearchArgument(String searchArgument) {
        this.searchArgument = searchArgument;
    }
}
