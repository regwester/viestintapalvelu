package fi.vm.sade.viestintapalvelu.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.letter.LetterService;
import fi.vm.sade.viestintapalvelu.letter.impl.LetterServiceImpl;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration("/test-application-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class LetterServiceTest {
    @Mock
    private LetterBatchDAO mockedLetterBatchDAO;
    @Mock
    private LetterReceiverLetterDAO mockedLetterReceiverLetterDAO;
    @Mock
    private CurrentUserComponent mockedCurrentUserComponent;
    private LetterService letterService;
    
    @Before
    public void setup() {
        this.letterService = new LetterServiceImpl(mockedLetterBatchDAO, mockedLetterReceiverLetterDAO,
            mockedCurrentUserComponent);
    }
    

    public void testLetterServiceImpl() {
        fail("Not yet implemented");
    }


    public void testCreateLetter() {
        when(mockedCurrentUserComponent.getCurrentUser()).thenReturn(DocumentProviderTestData.getHenkilo());
        when(mockedLetterBatchDAO.insert(any(LetterBatch.class))).thenReturn(any(LetterBatch.class));
    }

    @Test
    public void testFindById() {
        List<LetterBatch> mockedLetterBatchList = new ArrayList<LetterBatch>();
        mockedLetterBatchList.add(DocumentProviderTestData.getLetterBatch());
        when(mockedLetterBatchDAO.findBy(any(String.class), any(Object.class))).thenReturn(mockedLetterBatchList);
        
        fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatchFindById = letterService.findById(new Long(1));
        
        assertNotNull(letterBatchFindById);
        assertNotNull(letterBatchFindById.getTemplateId());
    }


    public void testFindLetterBatchByNameOrgTag() {
        fail("Not yet implemented");
    }


    public void testFindReplacementByNameOrgTag() {
        fail("Not yet implemented");
    }


    public void testGetLetter() {
        fail("Not yet implemented");
    }

}
