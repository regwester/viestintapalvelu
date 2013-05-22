package fi.vm.sade.viestintapalvelu.hyvaksymiskirje;

import java.util.List;

public class HyvaksymiskirjeBatch {
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
