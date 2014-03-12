package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;
import java.util.List;

public class ReportedMessagesDTO implements Serializable {
    private static final long serialVersionUID = -8138474011582310278L;
    private List<ReportedMessageDTO> reportedMessages;
    private Long numberOfReportedMessages;
    
    public List<ReportedMessageDTO> getReportedMessages() {
        return reportedMessages;
    }
    
    public void setReportedMessages(List<ReportedMessageDTO> reportedMessages) {
        this.reportedMessages = reportedMessages;
    }
    
    public Long getNumberOfReportedMessages() {
        return numberOfReportedMessages;
    }
    
    public void setNumberOfReportedMessages(Long numberOfReportedMessages) {
        this.numberOfReportedMessages = numberOfReportedMessages;
    }    
}
