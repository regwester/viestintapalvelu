/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.model.DraftModel;

public interface DraftDAO extends JpaDAO<DraftModel, Long> {

    public List<DraftModel> getAllDrafts(String oid);
    public DraftModel getDraft(Long id, String oid);
    public Long getCount(String oid);
    public DraftModel saveDraft(DraftModel draft);
    public void deleteDraft(Long id, String oid);
    public void updateDraft(Long id, String oid, DraftModel draft);

}
