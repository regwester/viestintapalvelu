package fi.vm.sade.viestintapalvelu.hyvaksymiskirje;

import java.util.List;

public class HyvaksymiskirjeBatch {
	private String kirjeTemplateName;
	private String liiteTemplateName;
	private List<Hyvaksymiskirje> letters;

	public HyvaksymiskirjeBatch() {
	}

	public HyvaksymiskirjeBatch(String kirjeTemplateName, String liiteTemplateName, List<Hyvaksymiskirje> letters) {
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
	
	public List<Hyvaksymiskirje> getLetters() {
		return letters;
	}

	@Override
	public String toString() {
		return "HyvaksymiskirjeBatch [kirjeTemplateName=" + kirjeTemplateName + "liiteTemplateName=" + liiteTemplateName
				+ ", letters=" + letters + "]";
	}
}
