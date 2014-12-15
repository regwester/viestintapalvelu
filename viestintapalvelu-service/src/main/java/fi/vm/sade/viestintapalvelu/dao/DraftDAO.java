package fi.vm.sade.viestintapalvelu.dao;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.model.Draft;

import java.util.List;

public interface DraftDAO extends JpaDAO<Draft, Long> {
	public Draft findDraftByNameOrgTag(	String templateName, String templateLanguage, String organizationOid, 
										String applicationPeriod, String fetchTarget, String tag);
	public Draft findDraftByNameOrgTag2(String templateName, String templateLanguage, String organizationOid, 
			String applicationPeriod, String fetchTarget, String tag);

	List<Draft> findByOrgOidsAndApplicationPeriod(List<String> oids, String applicationPeriod);
}
