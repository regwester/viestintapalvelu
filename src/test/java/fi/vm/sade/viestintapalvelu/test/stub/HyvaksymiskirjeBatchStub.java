package fi.vm.sade.viestintapalvelu.test.stub;

import java.util.Arrays;

import fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje.Hyvaksymiskirje;
import fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje.HyvaksymiskirjeBatch;

public class HyvaksymiskirjeBatchStub implements
		HyvaksymiskirjeBatch {
	private final Hyvaksymiskirje kirje;

	public HyvaksymiskirjeBatchStub(Hyvaksymiskirje kirje) {
		this.kirje = kirje;
	}

	public java.util.List<Hyvaksymiskirje> getLetters() {
		return Arrays.asList(kirje);
	}
}