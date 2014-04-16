package fi.vm.sade.viestintapalvelu.dao;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;;

public interface LetterBatchDAO extends JpaDAO<LetterBatch, Long> {

	public LetterBatch findLetterBatchByNameOrgTag(String templateName, String organizationOid, String tag);
}
