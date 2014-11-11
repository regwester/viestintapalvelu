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

package fi.vm.sade.viestintapalvelu.structure.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.viestintapalvelu.dao.StructureDAO;
import fi.vm.sade.viestintapalvelu.dao.dto.StructureListDto;
import fi.vm.sade.viestintapalvelu.model.Structure;
import fi.vm.sade.viestintapalvelu.structure.StructureService;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureViewDto;
import fi.vm.sade.viestintapalvelu.structure.dto.converter.StructureDtoConverter;
import fi.vm.sade.viestintapalvelu.util.OptionalHelpper;

import static com.google.common.base.Optional.fromNullable;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 14:18
 */
@Service
public class StructureServiceImpl implements StructureService {
    @Autowired
    private StructureDAO structureDAO;

    @Autowired
    private StructureDtoConverter dtoConverter;

    @Override
    @Transactional(readOnly = true)
    public List<StructureListDto> findLatestStructuresVersionsForList() {
        return structureDAO.findLatestStructuresVersionsForList();
    }

    @Override
    @Transactional(readOnly = true)
    public StructureViewDto getStructure(long id) {
        Structure structure = fromNullable(structureDAO.read(id)).or(
                OptionalHelpper.<Structure>notFound("Structure not found by id="+id));
        return dtoConverter.convert(structure, new StructureViewDto());
    }

    @Override
    @Transactional(readOnly = true)
    public StructureViewDto getLatestStructureByNameAndLanguage(String name, String language) {
        Structure structure = structureDAO.findLatestStructrueByNameAndLanguage(name, language).or(
                OptionalHelpper.<Structure>notFound("Structure not found by name="+name + ", language="+language));
        return dtoConverter.convert(structure, new StructureViewDto());
    }

    @Override
    @Transactional
    public long storeStructure(StructureSaveDto dto) {
        Structure structure = dtoConverter.convert(dto, new Structure());
        structureDAO.insert(structure);
        return structure.getId();
    }
}
