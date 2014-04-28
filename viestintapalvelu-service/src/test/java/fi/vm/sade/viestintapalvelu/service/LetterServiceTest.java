package fi.vm.sade.viestintapalvelu.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.letter.LetterService;
import fi.vm.sade.viestintapalvelu.letter.impl.LetterServiceImpl;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

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


    public void testFindById() {
        fail("Not yet implemented");
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
