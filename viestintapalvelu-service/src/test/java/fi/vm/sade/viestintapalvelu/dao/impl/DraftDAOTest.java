package fi.vm.sade.viestintapalvelu.dao.impl;

import fi.vm.sade.viestintapalvelu.dao.DraftDAO;
import fi.vm.sade.viestintapalvelu.model.Draft;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-application-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class DraftDAOTest {

    @Autowired
    DraftDAO draftDAO;

    @Before
    public void setup() {
        Draft draft = new Draft();
        draft.setOrganizationOid("org1");
        draft.setApplicationPeriod("appPeriod1");
        draft.setTemplateName("name1");
        draft.setTemplateLanguage("FI");
        draft.setTimestamp(new Date());

        Draft draft2 = new Draft();
        draft2.setOrganizationOid("org2");
        draft2.setApplicationPeriod("appPeriod1");
        draft2.setTemplateName("name2");
        draft2.setTemplateLanguage("FI");
        draft2.setTimestamp(new Date());

        Draft draft3 = new Draft();
        draft3.setOrganizationOid("org3");
        draft3.setApplicationPeriod("appPeriod2");
        draft3.setTemplateName("name3");
        draft3.setTemplateLanguage("FI");
        draft3.setTimestamp(new Date());


        draftDAO.insert(draft);
        draftDAO.insert(draft2);
        draftDAO.insert(draft3);
    }

    @Test
    public void testFindByOrgOidsAndApplicationPeriod() throws Exception {

        List<String> oids = new ArrayList<>();
        oids.add("org1");
        oids.add("org2");
        oids.add("org3");
        List<Draft> drafts = draftDAO.findByOrgOidsAndApplicationPeriod(oids, "appPeriod2");
        assertEquals(1, drafts.size());

        drafts = draftDAO.findByOrgOidsAndApplicationPeriod(oids, "appPeriod1");
        assertEquals(2, drafts.size());

    }
}