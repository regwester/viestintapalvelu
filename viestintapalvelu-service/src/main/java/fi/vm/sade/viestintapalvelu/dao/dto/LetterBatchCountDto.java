package fi.vm.sade.viestintapalvelu.dao.dto;

public class LetterBatchCountDto {
    public final long numberOfLetters;
    public final long numberOfReadyLetters;

    public LetterBatchCountDto(long numberOfLetters, long numberOfReadyLetters) {
        this.numberOfLetters = numberOfLetters;
        this.numberOfReadyLetters = numberOfReadyLetters;
    }
}
