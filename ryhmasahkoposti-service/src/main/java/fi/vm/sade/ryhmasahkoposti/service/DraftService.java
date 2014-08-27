package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.api.dto.Draft;

public interface DraftService {
    
    public Draft getDraft(Long id, String userOid);
    public List<Draft> getAllDrafts(String userOid);
    public Long getCount(String userOid);
    public void deleteDraft(Long id, String userOid) throws Exception;
    public Long saveDraft(Draft draft);
    public void updateDraft(Long id, String userOid, Draft draft);

}
