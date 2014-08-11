package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.api.dto.Draft;

public interface DraftService {
    
    public Draft getDraft(Long id);
    public List<Draft> getAllDrafts();
    public Long getCount();
    public Draft deleteDraft(Long id);
    public String saveDraft(Draft draft);
    
}
