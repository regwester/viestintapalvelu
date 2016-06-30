package fi.vm.sade.viestintapalvelu.dao.dto;

public class LetterBatchCountDto {
    public final long letterTotalCount;
    public final long letterReadyCount;
    public final long letterErrorCount;
    public final boolean readyForPublish;
    public final boolean readyForEPosti;

    public LetterBatchCountDto() {
        this.letterTotalCount = 0l;
        this.letterReadyCount = 0l;
        this.letterErrorCount = 0l;
        this.readyForPublish = false;
        this.readyForEPosti = false;
    }

    public LetterBatchCountDto(long letterTotalCount, long letterReadyCount, long letterErrorCount, boolean readyForPublish, boolean readyForEPosti) {
        this.letterTotalCount = letterTotalCount;
        this.letterReadyCount = letterReadyCount;
        this.letterErrorCount = letterErrorCount;
        this.readyForPublish = readyForPublish;
        this.readyForEPosti = readyForEPosti;
    }
}
