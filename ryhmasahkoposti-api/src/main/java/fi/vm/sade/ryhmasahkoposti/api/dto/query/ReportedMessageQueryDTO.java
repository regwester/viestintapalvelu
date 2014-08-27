package fi.vm.sade.ryhmasahkoposti.api.dto.query;


public class ReportedMessageQueryDTO {
	private String searchArgument;
	private String organizationOid;
	private ReportedRecipientQueryDTO reportedRecipientQuery;
	
	public String getSearchArgument() {
		return searchArgument;
	}

	public void setSearchArgument(String searchArgument) {
		this.searchArgument = searchArgument;
	}

	public String getOrganizationOid() {
        return organizationOid;
    }

    public void setOrganizationOid(String organizationOid) {
        this.organizationOid = organizationOid;
    }

    public ReportedRecipientQueryDTO getReportedRecipientQueryDTO() {
		return reportedRecipientQuery;
	}

	public void setReportedRecipientQueryDTO(ReportedRecipientQueryDTO reportedRecipientQuery) {
		this.reportedRecipientQuery = reportedRecipientQuery;
	}	
}
