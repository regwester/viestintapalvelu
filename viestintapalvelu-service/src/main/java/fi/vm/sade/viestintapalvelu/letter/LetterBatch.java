package fi.vm.sade.viestintapalvelu.letter;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Kerralla muodostettavien koekutsukirjeiden joukko")
public class LetterBatch {
	@ApiModelProperty(value = "Kerralla muodostettavien koekutsukirjeiden joukko, (1-n)", required=true)
    private List<Letter> letters;

    public LetterBatch() {
    }

    public LetterBatch(List<Letter> letters) {
        this.letters = letters;
    }

    public List<Letter> getLetters() {
        return letters;
    }

    @Override
    public String toString() {
        return "KoekutsukirjeBatch [letters=" + letters + "]";
    }
}
