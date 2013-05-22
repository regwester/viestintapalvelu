package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import java.util.ArrayList;
import java.util.List;

public class JalkiohjauskirjeIpostBatch extends JalkiohjauskirjeBatch {
	private String ipostTemplateName;

	public JalkiohjauskirjeIpostBatch() {
	}

	public JalkiohjauskirjeIpostBatch(String kirjeTemplateName,
			String liiteTemplateName, String ipostTemplateName,
			List<Jalkiohjauskirje> letters) {
		super(kirjeTemplateName, liiteTemplateName, letters);
		this.ipostTemplateName = ipostTemplateName;
	}

	public String getIpostTemplateName() {
		return ipostTemplateName;
	}

	@Override
	protected JalkiohjauskirjeIpostBatch createSubBatch(
			List<Jalkiohjauskirje> lettersOfSubBatch) {
		return new JalkiohjauskirjeIpostBatch(getKirjeTemplateName(),
				getLiiteTemplateName(), ipostTemplateName,
				new ArrayList<Jalkiohjauskirje>(lettersOfSubBatch));
	}

	@Override
	public String toString() {
		return "JalkiohjauskirjeBatch [kirjeTemplateName="
				+ getKirjeTemplateName() + "liiteTemplateName="
				+ getLiiteTemplateName() + "ipostTemplateName="
				+ getIpostTemplateName() + ", letters=" + getLetters() + "]";
	}
}
