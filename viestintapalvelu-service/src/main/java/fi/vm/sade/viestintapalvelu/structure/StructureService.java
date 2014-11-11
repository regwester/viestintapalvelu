/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.viestintapalvelu.structure;

import java.util.List;

import javax.ws.rs.NotFoundException;

import fi.vm.sade.viestintapalvelu.dao.dto.StructureListDto;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureViewDto;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 14:18
 */
public interface StructureService {

    /**
     * @return all structures in the system that have a description with id of their latest
     * version for the particular name and language ordered by description in ascending order
     * (and finally creation time descending)
     */
    List<StructureListDto> findLatestStructuresVersionsForList();

    /**
     * @param id of the structure
     * @return the details for the structure
     * @throws NotFoundException if not found by id
     */
    StructureViewDto getStructure(long id) throws NotFoundException;

    /**
     * @param name of the structure
     * @param language code
     * @return the latest structure for given name and language
     * @throws NotFoundException if not found
     */
    StructureViewDto getLatestStructureByNameAndLanguage(String name, String language) throws NotFoundException;

    /**
     * @param structure to be saved (assumed to be validated)
     * @return the id of the saved structure
     */
    long storeStructure(StructureSaveDto structure);
}
