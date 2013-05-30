package fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje;

import java.util.List;

import fi.vm.sade.viestintapalvelu.infrastructure.AbstractBatch;

public class HyvaksymiskirjeBatch extends AbstractBatch<Hyvaksymiskirje> {
	public HyvaksymiskirjeBatch(List<Hyvaksymiskirje> letters) {
		super(letters);
	}

	public List<Hyvaksymiskirje> getLetters() {
		return getContents();
	}

	@Override
	protected AbstractBatch<Hyvaksymiskirje> createSubBatch(
			List<Hyvaksymiskirje> contentsOfSubBatch) {
		return new HyvaksymiskirjeBatch(contentsOfSubBatch);
	}

	@Override
	public String toString() {
		return "HyvaksymiskirjeBatch [letters=" + getContents() + "]";
	}
}
