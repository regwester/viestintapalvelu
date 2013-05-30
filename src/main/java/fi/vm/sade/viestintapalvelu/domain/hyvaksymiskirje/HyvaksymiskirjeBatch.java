package fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje;

import java.util.List;

import fi.vm.sade.viestintapalvelu.infrastructure.Batch;

public class HyvaksymiskirjeBatch extends Batch<Hyvaksymiskirje> {
	public HyvaksymiskirjeBatch() {
	}

	public HyvaksymiskirjeBatch(List<Hyvaksymiskirje> letters) {
		super(letters);
	}

	public List<Hyvaksymiskirje> getLetters() {
		return getContents();
	}

	@Override
	protected Batch<Hyvaksymiskirje> createSubBatch(
			List<Hyvaksymiskirje> contentsOfSubBatch) {
		return new HyvaksymiskirjeBatch(contentsOfSubBatch);
	}

	@Override
	public String toString() {
		return "HyvaksymiskirjeBatch [letters=" + getContents() + "]";
	}
}
