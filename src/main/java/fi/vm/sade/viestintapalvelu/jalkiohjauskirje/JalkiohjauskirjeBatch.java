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

	public List<Jalkiohjauskirje> getLetters() {
		return letters;
	}

	public String getLiiteTemplateName() {
		return liiteTemplateName;
	}

	@Override
	public String toString() {
		return "JalkiohjauskirjeBatch [templateName=" + kirjeTemplateName
				+ ", letters=" + letters + "]";
	}
}
