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

import fi.vm.sade.viestintapalvelu.model.Style;

import static org.junit.Assert.*;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 18:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class StyleDAOTest {
    @Autowired
    private StyleDAO styleDAO;

    @Test
    public void testFindLatestByName() {
        Style style = new Style("tyyli","a"),
                style2 = new Style("tyyli","b");
        style2.setTimestamp(new Date(style.getTimestamp().getTime()+10000l));
        styleDAO.insert(style);
        styleDAO.insert(style2);

        assertFalse(styleDAO.findLatestByName("notfound").isPresent());
        Optional<Style> opt = styleDAO.findLatestByName("tyyli");
        assertTrue(opt.isPresent());
        assertEquals("b", opt.get().getStyle());
    }
}
