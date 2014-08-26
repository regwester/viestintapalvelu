package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.model.DraftModel;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional
@TransactionConfiguration(defaultRollback=true)
public class DraftDAOTest {
    
    @Autowired
    private DraftDAO draftDao;
    
    private DraftModel draftModel;

    @Before
    public void setUp() {
        draftModel = new DraftModel.Builder()
            .replyTo("jack.bauer@ctu.com")
            .subject("Saving the world")
            .userOid("Jack")
            .organizationOid("CTU")
            .body("<p>I did it again.</p><br>"
                + "<p>Jack</p>")
            .isHtml(true)
            .build();
        
        draftDao.saveDraft(draftModel);
    }
    
    @Test
    @Transactional
    public void testGetDraftById() {
        DraftModel draft = draftDao.getDraft(draftModel.getId(), "Jack");
        
        assertThat(draft.getReplyTo()).isEqualTo("jack.bauer@ctu.com");
        assertThat(draft.getSubject()).isEqualTo("Saving the world");
        assertThat(draft.getUserOid()).isEqualTo("Jack");
        assertThat(draft.getOrganizationOid()).isEqualTo("CTU");
        assertThat(draft.getBody()).isEqualTo("<p>I did it again.</p><br>"
                          + "<p>Jack</p>");
        assertThat(draft.isHtml()).isTrue();
    }
    
    @Test
    @Transactional
    public void testSaveDraft() {
        DraftModel oldDraft = new DraftModel.Builder()
            .replyTo("john.matrix@commando.com")
            .subject("Bennett")
            .userOid("John")
            .organizationOid("Delta Force")
            .body("Let off some steam.")
            .isHtml(false)
            .build();
        
        draftDao.saveDraft(oldDraft);
        Long id = oldDraft.getId();
        assertThat(id).isNotNull();
        
        DraftModel newDraft = draftDao.getDraft(id, "John");
        
        assertThat(newDraft.getReplyTo()).isEqualTo("john.matrix@commando.com");
        assertThat(newDraft.getSubject()).isEqualTo("Bennett");
        assertThat(newDraft.getUserOid()).isEqualTo("John");
        assertThat(newDraft.getOrganizationOid()).isEqualTo("Delta Force");
        assertThat(newDraft.getBody()).isEqualTo("Let off some steam.");
        assertThat(newDraft.isHtml()).isFalse();
    }
    
    @Test
    @Transactional
    public void testGetAllDrafts() {
        DraftModel draft = new DraftModel.Builder()
            .replyTo("sledge.hammer@nypd.com")
            .subject("Trust me")
            .userOid("Sledge")
            .organizationOid("LAPD")
            .body("<p>I know what I'm doing.</p>")
            .isHtml(true)
            .build();

        DraftModel draft2 = new DraftModel.Builder()
            .replyTo("sledge.hammer@nypd.com")
            .subject("Trust me again")
            .userOid("Sledge")
            .organizationOid("LAPD")
            .body("<p>I still know what I'm doing.</p>")
            .isHtml(true)
            .build();
        
        draftDao.saveDraft(draft);
        draftDao.saveDraft(draft2);
    
        List<DraftModel> drafts = draftDao.getAllDrafts("Sledge");
        assertThat(drafts).hasSize(2);
    }
    
    @Test
    @Transactional
    public void testDeleteDraft() {
        draftDao.deleteDraft(draftModel.getId(), "Jack");
        DraftModel draft = draftDao.getDraft(draftModel.getId(), "Jack");
        assertThat(draft).isNull();
    }
    
}
