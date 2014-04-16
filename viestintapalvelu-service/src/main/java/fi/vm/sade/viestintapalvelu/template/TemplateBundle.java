package fi.vm.sade.viestintapalvelu.template;

import java.util.List;

/**
 * @author migar1
 *
 */
public class TemplateBundle {
	private Template latestTemplate;
	private List<Replacement> latestOrganisationReplacements;
	private List<Replacement> latestOrganisationReplacementsWithTag;
	
	public TemplateBundle() {}
	
	public Template getLatestTemplate() {
		return latestTemplate;
	}
	public void setLatestTemplate(Template latestTemplate) {
		this.latestTemplate = latestTemplate;
	}
	public List<Replacement> getLatestOrganisationReplacements() {
		return latestOrganisationReplacements;
	}
	public void setLatestOrganisationReplacements(List<Replacement> latestOrganisationReplacements) {
		this.latestOrganisationReplacements = latestOrganisationReplacements;
	}
	public List<Replacement> getLatestOrganisationReplacementsWithTag() {
		return latestOrganisationReplacementsWithTag;
	}

	public void setLatestOrganisationReplacementsWithTag(
			List<Replacement> latestOrganisationReplacementsWithTag) {
		this.latestOrganisationReplacementsWithTag = latestOrganisationReplacementsWithTag;
	}
	
}
