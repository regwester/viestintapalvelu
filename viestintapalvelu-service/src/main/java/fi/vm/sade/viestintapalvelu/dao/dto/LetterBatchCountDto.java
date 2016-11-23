package fi.vm.sade.viestintapalvelu.dao.dto;

public class LetterBatchCountDto {
    public final Long letterBatchId;
    public final long letterTotalCount;
    public final long letterReadyCount;
    public final long letterErrorCount;
    public final long letterPublishedCount;
    public final boolean readyForPublish;
    public final boolean readyForEPosti;

    public LetterBatchCountDto() {
        this.letterBatchId = null;
        this.letterTotalCount = 0l;
        this.letterReadyCount = 0l;
        this.letterErrorCount = 0l;
        this.letterPublishedCount = 0l;
        this.readyForPublish = false;
        this.readyForEPosti = false;
    }

    public LetterBatchCountDto(Long letterBatchId, long letterTotalCount, long letterReadyCount, long letterErrorCount, long letterPublishedCount, boolean readyForPublish, boolean readyForEPosti) {
        this.letterBatchId = letterBatchId;
        this.letterTotalCount = letterTotalCount;
        this.letterReadyCount = letterReadyCount;
        this.letterErrorCount = letterErrorCount;
        this.letterPublishedCount = letterPublishedCount;
        this.readyForPublish = readyForPublish;
        this.readyForEPosti = readyForEPosti;
    }
}
