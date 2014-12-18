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

package fi.vm.sade.viestintapalvelu.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.dao.dto.StructureListDto;
import fi.vm.sade.viestintapalvelu.model.Structure;

import static org.junit.Assert.*;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 18:12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class StructureDAOTest {
    @Autowired
    private StructureDAO structureDAO;

    @Test
    public void testFindLatestStructuresVersionsForList() {
        Structure s = new Structure("name","a","FI"),
                s2 = new Structure("name","c","SV"),
                s3 = new Structure("name","b","FI"),
                s4 = new Structure("name2", null, "FI");
        s3.setTimestamp(new Date(s.getTimestamp().getTime()+10000l));
        structureDAO.insert(s);
        structureDAO.insert(s2);
        structureDAO.insert(s3);

        List<StructureListDto> list = structureDAO.findLatestStructuresVersionsForList();
        assertEquals(2, list.size());
        StructureListDto dto = list.get(0);
        assertNotNull(dto.getId());
        assertNotNull(dto.getName());
        assertNotNull(dto.getLanguage());
        assertNotNull(dto.getTimestamp());
        assertNotNull(dto.getDescription());
        assertEquals("b", dto.getDescription());
        assertEquals("c", list.get(1).getDescription());
    }

    @Test
    public void testFindLatestStructrueByNameAndLanguage() {
        Structure s = new Structure("name","a","FI"),
            s2 = new Structure("name","b","FI");
        s2.setTimestamp(new Date(s.getTimestamp().getTime()+10000l));
        structureDAO.insert(s);
        structureDAO.insert(s2);

        assertFalse(structureDAO.findLatestStructrueByNameAndLanguage("name","SV").isPresent());
        Optional<Structure> opt = structureDAO.findLatestStructrueByNameAndLanguage("name", "FI");
        assertTrue(opt.isPresent());
        assertEquals("b", opt.get().getDescription());
    }

}
