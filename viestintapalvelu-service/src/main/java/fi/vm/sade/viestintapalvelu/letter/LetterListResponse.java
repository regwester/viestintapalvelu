package fi.vm.sade.viestintapalvelu.letter;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.List;

public class LetterListResponse {

    @ApiModelProperty(value = "Lista käyttäjän julkaistavista kirjeistä")
    List<LetterListItem> letters;

    public List<LetterListItem> getLetters() {
        return letters;
    }

    public void setLetters(List<LetterListItem> letters) {
        this.letters = letters;
    }

    @Override
    public String toString() {
        return "LetterListResponse{" +
                "letters=" + letters +
                '}';
    }
}

