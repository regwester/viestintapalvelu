package fi.vm.sade.ryhmasahkoposti.api.dto.query;

import java.util.List;

public class ReportedMessageQueryDTO {
	private String searchArgument;
	private List<String> senderOids;
	private ReportedRecipientQueryDTO reportedRecipientQuery;
	
	public String getSearchArgument() {
		return searchArgument;
	}

	public void setSearchArgument(String searchArgument) {
		this.searchArgument = searchArgument;
	}

	public List<String> getSenderOids() {
        return senderOids;
    }

    public void setSenderOids(List<String> senderOids) {
        this.senderOids = senderOids;
    }

    public ReportedRecipientQueryDTO getReportedRecipientQueryDTO() {
		return reportedRecipientQuery;
	}

	public void setReportedRecipientQueryDTO(ReportedRecipientQueryDTO reportedRecipientQuery) {
		this.reportedRecipientQuery = reportedRecipientQuery;
	}	
}
