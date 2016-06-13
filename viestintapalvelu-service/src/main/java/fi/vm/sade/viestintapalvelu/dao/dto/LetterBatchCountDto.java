package fi.vm.sade.viestintapalvelu.dao.dto;

public class LetterBatchCountDto {
    public final long letterTotalCount;
    public final long letterReadyCount;
    public final long letterErrorCount;

    public LetterBatchCountDto(long letterTotalCount, long letterReadyCount, long letterErrorCount) {
        this.letterTotalCount = letterTotalCount;
        this.letterReadyCount = letterReadyCount;
        this.letterErrorCount = letterErrorCount;
    }
}
