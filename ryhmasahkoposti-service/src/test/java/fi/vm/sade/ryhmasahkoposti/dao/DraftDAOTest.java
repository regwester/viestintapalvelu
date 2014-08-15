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
        
        Assert.assertEquals("jack.bauer@ctu.com", draft.getReplyTo());
        Assert.assertEquals("Saving the world", draft.getSubject());
        Assert.assertEquals("Jack", draft.getUserOid());
        Assert.assertEquals("<p>I did it again.</p><br>"
                          + "<p>Jack</p>", draft.getBody());
        Assert.assertEquals(true, draft.isHtml());
    }
    
    @Test
    @Transactional
    public void testSaveDraft() {
        DraftModel oldDraft = new DraftModel.Builder()
        .replyTo("john.matrix@commando.com")
        .subject("Bennett")
        .userOid("John")
        .body("Let off some steam.")
        .isHtml(false)
        .build();
        
        draftDao.saveDraft(oldDraft);
        Long id = oldDraft.getId();
        Assert.assertNotNull(id);
        
        DraftModel newDraft = draftDao.getDraft(id, "John");
        
        Assert.assertEquals("john.matrix@commando.com", newDraft.getReplyTo());
        Assert.assertEquals("Bennett", newDraft.getSubject());
        Assert.assertEquals("John", newDraft.getUserOid());
        Assert.assertEquals("Let off some steam.", newDraft.getBody());
        Assert.assertEquals(false, newDraft.isHtml());
    }
    
    @Test
    @Transactional
    public void testGetAllDrafts() {
        DraftModel newDraft = new DraftModel.Builder()
        .replyTo("sledge.hammer@nypd.com")
        .subject("Trust me")
        .userOid("Sledge")
        .body("<p>I know what I'm doing.</p>")
        .isHtml(true)
        .build();
        
        draftDao.saveDraft(newDraft);
    
        List<DraftModel> drafts = draftDao.getAllDrafts("Sledge");
        Assert.assertEquals(1, drafts.size());
    }
    
    @Test
    @Transactional
    public void testDeleteDraft() {
        draftDao.deleteDraft(draftModel.getId(), "Jack");
        DraftModel draft = draftDao.getDraft(draftModel.getId(), "Jack");
        assertThat(draft).isNull();
    }
    
}
