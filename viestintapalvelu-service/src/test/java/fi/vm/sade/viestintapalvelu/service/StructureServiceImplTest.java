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

package fi.vm.sade.viestintapalvelu.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.dao.StructureDAO;
import fi.vm.sade.viestintapalvelu.dao.StyleDAO;
import fi.vm.sade.viestintapalvelu.dao.dto.StructureListDto;
import fi.vm.sade.viestintapalvelu.model.Structure;
import fi.vm.sade.viestintapalvelu.model.Style;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureViewDto;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureViewDto;
import fi.vm.sade.viestintapalvelu.structure.dto.converter.StructureDtoConverter;
import fi.vm.sade.viestintapalvelu.structure.impl.StructureServiceImpl;
import fi.vm.sade.viestintapalvelu.util.BeanValidator;
import fi.vm.sade.viestintapalvelu.util.DaoVault;
import fi.vm.sade.viestintapalvelu.util.impl.BeanValidatorImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 16:49
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("/test-application-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class StructureServiceImplTest {
    @Mock
    private StyleDAO styleDAO;

    @Mock
    private StructureDAO structureDAO;

    @InjectMocks
    private StructureDtoConverter dtoConverter;

    @InjectMocks
    private StructureServiceImpl structureService;

    private BeanValidator beanValidator = new BeanValidatorImpl();

    private DaoVault<Style> styleVault;
    private DaoVault<Structure> structureVault;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        structureService.setDtoConverter(dtoConverter);
        structureVault = new DaoVault<Structure>(structureDAO, Structure.class);
        styleVault = new DaoVault<Style>(styleDAO, Style.class);
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JodaModule());
    }

    @Test
    public void testStoreTemplate() throws IOException {
        StructureSaveDto dto = fromJson(StructureSaveDto.class, "/json/teststructure.json");
        when(styleDAO.findLatestByName(any(String.class))).then(styleVault.readFirstOptional());

        beanValidator.validate(dto);
        long id = structureService.storeStructure(dto);
        assertNotNull(structureVault.get(id));

        Structure structure = structureVault.get(id);
        assertEquals(dto.getName(), structure.getName());
        assertEquals(dto.getLanguage(), structure.getLanguage());
        assertEquals(dto.getReplacements().size(), structure.getReplacements().size());
        assertEquals(dto.getContentStructures().size(), structure.getContentStructures().size());
    }

    @Test
    public void testGetTemplateById() throws IOException {
        StructureSaveDto dto = fromJson(StructureSaveDto.class, "/json/teststructure.json");
        when(styleDAO.findLatestByName(any(String.class))).then(styleVault.readFirstOptional());
        long id = structureService.storeStructure(dto);

        StructureViewDto viewDto = structureService.getStructure(id);
        assertEquals(dto.getName(), viewDto.getName());
        assertEquals(dto.getLanguage(), viewDto.getLanguage());
        assertEquals(dto.getReplacements().size(), viewDto.getReplacements().size());
        assertEquals(dto.getContentStructures().size(), viewDto.getContentStructures().size());
        for (ContentStructureViewDto csDto : viewDto.getContentStructures()) {
            if ((csDto.getType() != ContentStructureType.asiointitili)) {
                assertNotNull(csDto.getStyle());
            }
        }
    }

    @Test
    public void testFindLatestStructuresVersionsForList() throws IOException {
        List<StructureListDto> list = new ArrayList<StructureListDto>();
        when(structureDAO.findLatestStructuresVersionsForList()).thenReturn(list);
        assertTrue(structureService.findLatestStructuresVersionsForList() == list);
    }

    @Test
    public void testFindLatestStructrueByNameAndLanguage() throws IOException {
        StructureSaveDto dto = fromJson(StructureSaveDto.class, "/json/teststructure.json");
        when(styleDAO.findLatestByName(any(String.class))).then(styleVault.readFirstOptional());
        long id = structureService.storeStructure(dto);

        when(structureDAO.findLatestStructrueByNameAndLanguage(eq("a"),eq("b"))).thenReturn(
                Optional.of(structureVault.get(id)));
        StructureViewDto viewDto = structureService.getLatestStructureByNameAndLanguage("a","b");
        assertEquals(dto.getName(), viewDto.getName());
        assertEquals(dto.getLanguage(), viewDto.getLanguage());
        assertEquals(dto.getReplacements().size(), viewDto.getReplacements().size());
        assertEquals(dto.getContentStructures().size(), viewDto.getContentStructures().size());
    }


    private <T> T fromJson(Class<T> clz, String resourceLocation) throws IOException {
        return mapper.reader(clz).readValue(this.getClass().getResourceAsStream(resourceLocation));
    }
}
