package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import java.util.List;

public class JalkiohjauskirjeIpostBatch extends JalkiohjauskirjeBatch {
	private String ipostTemplateName;

	public JalkiohjauskirjeIpostBatch() {
	}

	public JalkiohjauskirjeIpostBatch(String kirjeTemplateName, String liiteTemplateName, String ipostTemplateName, List<Jalkiohjauskirje> letters) {
		super(kirjeTemplateName, liiteTemplateName, letters);
		this.ipostTemplateName = ipostTemplateName;
	}
	
	public String getIpostTemplateName() {
		return ipostTemplateName;
	}

	@Override
	public String toString() {
		return "JalkiohjauskirjeBatch [kirjeTemplateName=" + getKirjeTemplateName() 
				+ "liiteTemplateName=" + getLiiteTemplateName() + "ipostTemplateName=" + getIpostTemplateName()
				+ ", letters=" + getLetters() + "]";
	}
}
