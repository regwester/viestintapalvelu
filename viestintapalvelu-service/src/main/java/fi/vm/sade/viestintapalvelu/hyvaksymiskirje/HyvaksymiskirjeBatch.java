package fi.vm.sade.viestintapalvelu.hyvaksymiskirje;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Kerralla muodostettavien hyväksymiskirjeiden joukko")

public class HyvaksymiskirjeBatch {
	@ApiModelProperty(value = "Kerralla muodostettavien hyväksymiskirjeiden joukko, (1-n)", required=true)	
    private List<Hyvaksymiskirje> letters;

    public HyvaksymiskirjeBatch() {
    }

    public HyvaksymiskirjeBatch(List<Hyvaksymiskirje> letters) {
        this.letters = letters;
    }

    public List<Hyvaksymiskirje> getLetters() {
        return letters;
    }

    @Override
    public String toString() {
        return "HyvaksymiskirjeBatch [letters=" + letters + "]";
    }
}
