package fi.vm.sade.viestintapalvelu.dto.query;

import java.io.Serializable;
import java.util.List;

import org.joda.time.LocalDate;

public class LetterReportQueryDTO implements Serializable {
    public enum SearchTarget {
        batch,
        receiver
    }

    private static final long serialVersionUID = -8516467453321234990L;
    private List<String> organizationOids;
    private SearchTarget target = SearchTarget.batch;
    private String letterBatchSearchArgument;
    private String receiverSearchArgument;
    private LocalDate beginDate;
    private LocalDate endDate;
    
    public List<String> getOrganizationOids() {
        return organizationOids;
    }
    
    public void setOrganizationOids(List<String> organizationOids) {
        this.organizationOids = organizationOids;
    }

    public String getLetterBatchSearchArgument() {
        return letterBatchSearchArgument;
    }

    public void setLetterBatchSearchArgument(String letterBatchSearchArgument) {
        this.letterBatchSearchArgument = letterBatchSearchArgument;
    }

    public String getReceiverSearchArgument() {
        return receiverSearchArgument;
    }

    public void setReceiverSearchArgument(String receiverSearchArgument) {
        this.receiverSearchArgument = receiverSearchArgument;
    }

    public SearchTarget getTarget() {
        return target;
    }

    public void setTarget(SearchTarget target) {
        this.target = target;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
