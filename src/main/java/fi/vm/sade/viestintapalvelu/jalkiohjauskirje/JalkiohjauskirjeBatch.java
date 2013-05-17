package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import java.util.List;

public class JalkiohjauskirjeBatch {
	private String kirjeTemplateName;
	private String liiteTemplateName;
	private List<Jalkiohjauskirje> letters;

	public JalkiohjauskirjeBatch() {
	}

	public JalkiohjauskirjeBatch(String kirjeTemplateName, String liiteTemplateName, List<Jalkiohjauskirje> letters) {
		this.kirjeTemplateName = kirjeTemplateName;
		this.liiteTemplateName = liiteTemplateName;
		this.letters = letters;
	}

	public String getKirjeTemplateName() {
		return kirjeTemplateName;
	}

	public String getLiiteTemplateName() {
		return liiteTemplateName;
	}
	
	public List<Jalkiohjauskirje> getLetters() {
		return letters;
	}

	@Override
	public String toString() {
		return "JalkiohjauskirjeBatch [kirjeTemplateName=" + kirjeTemplateName + "liiteTemplateName=" + liiteTemplateName
				+ ", letters=" + letters + "]";
	}
}
