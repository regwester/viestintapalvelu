package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import java.util.ArrayList;
import java.util.List;

public class JalkiohjauskirjeBatch {
	private String kirjeTemplateName;
	private String liiteTemplateName;
	private List<Jalkiohjauskirje> letters;

	public JalkiohjauskirjeBatch() {
	}

	public JalkiohjauskirjeBatch(String kirjeTemplateName,
			String liiteTemplateName, List<Jalkiohjauskirje> letters) {
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
		return "JalkiohjauskirjeBatch [kirjeTemplateName=" + kirjeTemplateName
				+ "liiteTemplateName=" + liiteTemplateName + ", letters="
				+ letters + "]";
	}

	public List<JalkiohjauskirjeBatch> split(int limit) {
		List<JalkiohjauskirjeBatch> batches = new ArrayList<JalkiohjauskirjeBatch>();
		split(letters, batches, limit);
		return batches;
	}

	protected JalkiohjauskirjeBatch createSubBatch(
			List<Jalkiohjauskirje> lettersOfSubBatch) {
		return new JalkiohjauskirjeBatch(kirjeTemplateName, liiteTemplateName,
				new ArrayList<Jalkiohjauskirje>(lettersOfSubBatch));
	}

	private void split(List<Jalkiohjauskirje> remaining,
			List<JalkiohjauskirjeBatch> batches, int limit) {
		if (limit >= remaining.size()) {
			batches.add(createSubBatch(remaining));
		} else {
			batches.add(createSubBatch(new ArrayList<Jalkiohjauskirje>(
					remaining.subList(0, limit))));
			split(remaining.subList(limit, remaining.size()), batches, limit);
		}
	}
}
