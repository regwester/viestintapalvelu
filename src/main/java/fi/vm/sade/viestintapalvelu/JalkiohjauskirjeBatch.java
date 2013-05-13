package fi.vm.sade.viestintapalvelu;

import java.util.List;

public class JalkiohjauskirjeBatch {
	private String templateName;
	private List<Jalkiohjauskirje> letters;

	public JalkiohjauskirjeBatch() {
	}

	public JalkiohjauskirjeBatch(String templateName, List<Jalkiohjauskirje> letters) {
		this.templateName = templateName;
		this.letters = letters;
	}

	public String getTemplateName() {
		return templateName;
	}

	public List<Jalkiohjauskirje> getLetters() {
		return letters;
	}

	@Override
	public String toString() {
		return "JalkiohjauskirjeBatch [templateName=" + templateName
				+ ", letters=" + letters + "]";
	}
}
