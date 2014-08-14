package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.model.DraftModel;

public interface DraftDAO extends JpaDAO<DraftModel, Long> {
    
    public List<DraftModel> getAllDrafts(String oid);
    public DraftModel getDraft(Long id, String oid);
    public Long getCount(String oid);
    public void saveDraft(DraftModel draft);
    public DraftModel deleteDraft(Long id, String oid);
    
}
