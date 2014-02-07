package fi.vm.sade.viestintapalvelu.koekutsukirje;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Kerralla muodostettavien koekutsukirjeiden joukko")
public class KoekutsukirjeBatch {
	@ApiModelProperty(value = "Kerralla muodostettavien koekutsukirjeiden joukko, (1-n)", required=true)
    private List<Koekutsukirje> letters;

    public KoekutsukirjeBatch() {
    }

    public KoekutsukirjeBatch(List<Koekutsukirje> letters) {
        this.letters = letters;
    }

    public List<Koekutsukirje> getLetters() {
        return letters;
    }

    @Override
    public String toString() {
        return "KoekutsukirjeBatch [letters=" + letters + "]";
    }
}
