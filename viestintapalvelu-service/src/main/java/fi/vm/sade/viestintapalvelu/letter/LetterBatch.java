package fi.vm.sade.viestintapalvelu.letter;

import java.util.List;
import java.util.Map;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Kerralla muodostettavien koekutsukirjeiden joukko")
public class LetterBatch {
    @ApiModelProperty(value = "Kerralla muodostettavien koekutsukirjeiden joukko, (1-n)", required = true)
    private List<Letter> letters;

    @ApiModelProperty(value = "Kirjeen yleiset personointikentät", required = false, notes = "")
    private Map<String, Object> templateReplacements;

    public Map<String, Object> getTemplateReplacements() {
        return templateReplacements;
    }

    public void setTemplateReplacements(Map<String, Object> templateReplacements) {
        this.templateReplacements = templateReplacements;
    }

    public void setLetters(List<Letter> letters) {
        this.letters = letters;
    }

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
        return "LetterBatch [letters=" + letters + ", templateReplacements="
                + templateReplacements + "]";
    }
}
