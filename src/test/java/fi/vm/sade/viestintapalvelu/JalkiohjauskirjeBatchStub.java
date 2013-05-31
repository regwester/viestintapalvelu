package fi.vm.sade.viestintapalvelu;

import java.util.List;

import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.JalkiohjauskirjeBatch;

public class JalkiohjauskirjeBatchStub extends JalkiohjauskirjeBatch {
	private final List<Jalkiohjauskirje> contents;

	public JalkiohjauskirjeBatchStub(List<Jalkiohjauskirje> contents) {
		this.contents = contents;
	}

	@Override
	public List<Jalkiohjauskirje> getLetters() {
		return contents;
	}
}
