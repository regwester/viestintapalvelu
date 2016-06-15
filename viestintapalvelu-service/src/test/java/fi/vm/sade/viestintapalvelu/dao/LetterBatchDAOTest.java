/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.viestintapalvelu.dao;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import fi.vm.sade.viestintapalvelu.dao.dto.LetterBatchCountDto;
import fi.vm.sade.viestintapalvelu.letter.LetterListItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterBatch.Status;
import fi.vm.sade.viestintapalvelu.model.LetterBatchGeneralProcessingError;
import fi.vm.sade.viestintapalvelu.model.LetterBatchIPostProcessingError;
import fi.vm.sade.viestintapalvelu.model.LetterBatchLetterProcessingError;
import fi.vm.sade.viestintapalvelu.model.LetterBatchProcessingError;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-dao-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class LetterBatchDAOTest {
    private static final Logger logger = LoggerFactory.getLogger(LetterBatchDAOTest.class);

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private LetterBatchDAO letterBatchDAO;

    @Test
    public void testFindLetterBatchByNameOrgTag() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatchDAO.insert(letterBatch);
        
        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(
            "test-templateName", "FI", "1.2.246.562.10.00000000001", Optional.of("test-tag"),
                Optional.<String>absent());
        
        assertNotNull(foundLetterBatch);
        assertTrue(foundLetterBatch.getId() > 0);
        assertNotNull(foundLetterBatch.getLetterReceivers());
        assertTrue(foundLetterBatch.getLetterReceivers().size() > 0);
        assertNotNull(foundLetterBatch.getLetterReplacements());
        assertTrue(foundLetterBatch.getLetterReplacements().size() > 0);
        assertEquals("Status is 'processing' by default in the test data generator", LetterBatch.Status.processing, foundLetterBatch.getBatchStatus());
    }

    @Test
    public void testPersistGeneralError() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatchDAO.insert(letterBatch);

        LetterBatchGeneralProcessingError error = new LetterBatchGeneralProcessingError();
        error.setLetterBatch(letterBatch);
        error.setErrorTime(new Date());
        error.setErrorCause("cause");
        letterBatch.getProcessingErrors().add(error);
        entityManager.flush();
        assertEquals(1, letterBatchDAO.read(letterBatch.getId()).getProcessingErrors().size());
    }

    @Test
    public void testPersistIpostError() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatchDAO.insert(letterBatch);

        LetterBatchIPostProcessingError error = new LetterBatchIPostProcessingError();
        error.setLetterBatch(letterBatch);
        error.setErrorTime(new Date());
        error.setErrorCause("cause");
        error.setOrderNumber(1);
        letterBatch.getProcessingErrors().add(error);
        entityManager.flush();
        assertEquals(1, letterBatchDAO.read(letterBatch.getId()).getProcessingErrors().size());
    }

    @Test
    public void testPersistReceiverError() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatchDAO.insert(letterBatch);
        LetterReceivers receiver = letterBatch.getLetterReceivers().iterator().next();
        assertNotNull(receiver);

        LetterBatchLetterProcessingError error = new LetterBatchLetterProcessingError();
        error.setLetterBatch(letterBatch);
        error.setErrorTime(new Date());
        error.setErrorCause("cause");
        error.setLetterReceivers(receiver);
        letterBatch.getProcessingErrors().add(error);
        entityManager.flush();
        assertEquals(1, letterBatchDAO.read(letterBatch.getId()).getProcessingErrors().size());
    }

    @Test
    public void testFindLetterBatchByNameOrgTagAndApplicationPeriod() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setApplicationPeriod("period");
        letterBatchDAO.insert(letterBatch);

        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(
                "test-templateName", "FI", "1.2.246.562.10.00000000001", Optional.of("test-tag"),
                Optional.of("period"));

        assertNotNull(foundLetterBatch);
        assertTrue(foundLetterBatch.getId() > 0);
        assertNotNull(foundLetterBatch.getLetterReceivers());
        assertTrue(foundLetterBatch.getLetterReceivers().size() > 0);
        assertNotNull(foundLetterBatch.getLetterReplacements());
        assertTrue(foundLetterBatch.getLetterReplacements().size() > 0);
    }

    @Test
    public void testFindLetterBatchByNameOrgTagAndApplicationPeriodNotFoundByTag() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setTag("other-tag");
        letterBatch.setApplicationPeriod("period");
        letterBatchDAO.insert(letterBatch);

        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(
                "test-templateName", "FI", "1.2.246.562.10.00000000001", Optional.of("test-tag"),
                Optional.of("period"));

        assertNull(foundLetterBatch);
    }

    @Test
    public void testFindLetterBatchByNameOrgTagAndApplicationPeriodWithoutTag() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setApplicationPeriod("period");
        letterBatchDAO.insert(letterBatch);

        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(
                "test-templateName", "FI", "1.2.246.562.10.00000000001", Optional.<String>absent(),
                Optional.of("period"));

        assertNotNull(foundLetterBatch);
        assertTrue(foundLetterBatch.getId() > 0);
        assertNotNull(foundLetterBatch.getLetterReceivers());
        assertTrue(foundLetterBatch.getLetterReceivers().size() > 0);
        assertNotNull(foundLetterBatch.getLetterReplacements());
        assertTrue(foundLetterBatch.getLetterReplacements().size() > 0);
    }

    @Test
    public void testFindLetterBatchByNameOrgTagAndApplicationPeriodNotFound() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setApplicationPeriod("period");
        letterBatchDAO.insert(letterBatch);

        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(
                "test-templateName", "FI", "1.2.246.562.10.00000000001", Optional.of("test-tag"),
                Optional.of("other period"));

        assertNull(foundLetterBatch);
    }

    @Test
    public void testFindLetterBatchByNameOrg() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatchDAO.insert(letterBatch);
        
        LetterBatch foundLetterBatch = letterBatchDAO.findLetterBatchByNameOrg(
            "test-templateName", "FI", "1.2.246.562.10.00000000001");
        
        assertNotNull(foundLetterBatch);
        assertTrue(foundLetterBatch.getId() > 0);
        assertNotNull(foundLetterBatch.getLetterReceivers());
        assertTrue(foundLetterBatch.getLetterReceivers().size() > 0);
        assertNotNull(foundLetterBatch.getLetterReplacements());
        assertTrue(foundLetterBatch.getLetterReplacements().size() > 0);
        assertEquals("Status is 'processing' by default in the test data generator",
                LetterBatch.Status.processing, foundLetterBatch.getBatchStatus());
    }

    @Test(expected = PersistenceException.class)
    public void insertNullError() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setBatchStatus(LetterBatch.Status.error);
        LetterBatchProcessingError error = new LetterBatchLetterProcessingError();
        error.setErrorCause("Testing failure case");
        error.setLetterBatch(letterBatch);
        letterBatch.addProcessingErrors(error);
        letterBatchDAO.insert(letterBatch).getId();
    }

    @Test
    public void getBatchStatus() {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setBatchStatus(LetterBatch.Status.processing);
        long idA = letterBatchDAO.insert(letterBatch).getId();

        letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setBatchStatus(LetterBatch.Status.ready);
        long idB = letterBatchDAO.insert(letterBatch).getId();

        letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setBatchStatus(LetterBatch.Status.error);
        LetterBatchLetterProcessingError error = new LetterBatchLetterProcessingError();
        error.setErrorCause("Testing failure case");
        error.setLetterBatch(letterBatch);
        error.setErrorTime(new Date());
        error.setLetterReceivers(letterBatch.getLetterReceivers().iterator().next());
        letterBatch.addProcessingErrors(error);
        long idC = letterBatchDAO.insert(letterBatch).getId();

        letterBatch = letterBatchDAO.read(idA);
        assertEquals(new HashSet<LetterBatchProcessingError>(), letterBatch.getProcessingErrors());

        letterBatch = letterBatchDAO.read(idB);
        assertTrue(letterBatch.getProcessingErrors().isEmpty());

        letterBatch = letterBatchDAO.read(idC);
        assertEquals(1, letterBatch.getProcessingErrors().size());
        System.out.println(letterBatch.getProcessingErrors().toString());
        assertEquals("Testing failure case", letterBatch.getProcessingErrors().iterator().next().getErrorCause());
    }
    
    @Test
    public void returnsEmptyListWhenAllLettersAreProcessed() {
        assertTrue(letterBatchDAO.findUnprocessedLetterReceiverIdsByBatch(givenLetterBatchWithLetter(Status.processing, "afeaf".getBytes())).isEmpty());
    }

    @Test
    public void returnsUnprocessedLetters() {
        assertEquals(1, letterBatchDAO.findUnprocessedLetterReceiverIdsByBatch(givenLetterBatchWithLetter(Status.processing, null)).size());
    }
    
    @Test
    public void returnsUnfinishedBatches() {
        givenLetterBatchWithLetter(Status.processing, null);
        assertEquals(1, letterBatchDAO.findUnfinishedLetterBatches().size());        
    }
    
    @Test
    public void doesNotReturnFinishedBatches() {
        givenLetterBatchWithLetter(Status.ready, null);
        assertEquals(0, letterBatchDAO.findUnfinishedLetterBatches().size());   
    }

    @Test
    public void doesNotReturnBatchesThatAreFaulty() {
        givenLetterBatchWithLetter(Status.error, null);
        assertEquals(0, letterBatchDAO.findUnfinishedLetterBatches().size());
    }
    
    @Test
    public void ordersBatchesByModified() throws Exception {
        givenLetterBatchWithDateModified(Status.created, null, new Date());
        Long first = givenLetterBatchWithDateModified(Status.processing, null, new Date(0));
        assertEquals(first, letterBatchDAO.findUnfinishedLetterBatches().get(0));
    }

    @Test
    public void findLettersReadyForPublishByPersonOid() throws Exception {
        insertLetterBatchForPersonOid("test-haku-oid-1", "test-person-oid-1", "hyvaksymiskirje", true);
        insertLetterBatchForPersonOid("test-haku-oid-1", "test-person-oid-1", "hyvaksymiskirje", false);
        insertLetterBatchForPersonOid("test-haku-oid-2", "test-person-oid-1", "jalkiohjauskirje", true);
        insertLetterBatchForPersonOid("test-haku-oid-2", "test-person-oid-2", "hyvaksymiskirje", true);

        List<LetterListItem> listItems = letterBatchDAO.findLettersReadyForPublishByPersonOid("test-person-oid-1");

        assertEquals(2, listItems.size());
        for(LetterListItem item : listItems) {
            logger.info(item.toString());
        }
        assertTrue(expectedListItemInList(listItems, "test-haku-oid-1", "hyvaksymiskirje"));
        assertTrue(expectedListItemInList(listItems, "test-haku-oid-2", "jalkiohjauskirje"));

        listItems = letterBatchDAO.findLettersReadyForPublishByPersonOid("test-person-oid-2");

        assertEquals(1, listItems.size());
        logger.info(listItems.get(0).toString());
        assertTrue(listItemEquals(listItems.get(0), "test-haku-oid-2", "hyvaksymiskirje"));
    }

   @Test
    public void publishLetterBatch() throws Exception {
       long batchId1 = insertLetterBatchForPersonOids("test-haku-oid-1",
               Arrays.asList("test-person-oid-1", "test-person-oid-2", "test-person-oid-3"), "hyvaksymiskirje", false);
       long batchId2 = insertLetterBatchForPersonOids("test-haku-oid-2",
               Arrays.asList("test-person-oid-1", "test-person-oid-2"), "jalkiohjauskirje", false);

       List<LetterListItem> listItems = letterBatchDAO.findLettersReadyForPublishByPersonOid("test-person-oid-1");
       assertEquals(0, listItems.size());

       assertEquals(3l, letterBatchDAO.publishLetterBatch(batchId1));

       listItems = letterBatchDAO.findLettersReadyForPublishByPersonOid("test-person-oid-1");
       assertEquals(1, listItems.size());
       logger.info(listItems.get(0).toString());
       assertTrue(listItemEquals(listItems.get(0), "test-haku-oid-1", "hyvaksymiskirje"));

       assertEquals(2l, letterBatchDAO.publishLetterBatch(batchId2));

       listItems = letterBatchDAO.findLettersReadyForPublishByPersonOid("test-person-oid-1");
       assertEquals(2, listItems.size());
       for(LetterListItem item : listItems) {
           logger.info(item.toString());
       }
       assertTrue(expectedListItemInList(listItems, "test-haku-oid-1", "hyvaksymiskirje"));
       assertTrue(expectedListItemInList(listItems, "test-haku-oid-2", "jalkiohjauskirje"));

       LetterBatchCountDto letterBatchCountDto = letterBatchDAO.countBatchStatus("test-haku-oid-1", "hyvaksymiskirje", "fi");
       assertEquals(1, letterBatchCountDto.letterTotalCount);
       assertEquals(1, letterBatchCountDto.letterReadyCount);
       assertEquals(0, letterBatchCountDto.letterErrorCount);

       LetterBatchCountDto letterBatchCountDto2 = letterBatchDAO.countBatchStatus("test-haku-oid-1", "hyvaksymiskirje", "sv");
       assertEquals(0, letterBatchCountDto2.letterTotalCount);
       assertEquals(0, letterBatchCountDto2.letterReadyCount);
       assertEquals(0, letterBatchCountDto2.letterErrorCount);

   }

    @Test
    public void getLetterBatchId() throws Exception {
        Long batchId1 = insertLetterBatchForPersonOids("test-haku-oid-1",
                Arrays.asList("test-person-oid-1", "test-person-oid-2", "test-person-oid-3"), "hyvaksymiskirje", false);
        Long batchId2 = insertLetterBatchForPersonOids("test-haku-oid-1",
                Arrays.asList("test-person-oid-1", "test-person-oid-2", "test-person-oid-3"), "jalkiohjauskirje", true);
        Long batchId3 = insertLetterBatchForPersonOids("test-haku-oid-1",
                Arrays.asList("test-person-oid-1", "test-person-oid-2", "test-person-oid-3"), "hyvaksymiskirje", false);
        Long batchId4 = insertLetterBatchForPersonOids("test-haku-oid-1",
                Arrays.asList("test-person-oid-1", "test-person-oid-2", "test-person-oid-3"), "hyvaksymiskirje", true);
        Long batchId5 = insertLetterBatchForPersonOids("test-haku-oid-2",
                Arrays.asList("test-person-oid-1", "test-person-oid-2", "test-person-oid-3"), "hyvaksymiskirje", false);

        assertEquals(batchId4, letterBatchDAO.getLatestLetterBatchId("test-haku-oid-1", "hyvaksymiskirje", "FI", true).get());
        assertEquals(batchId3, letterBatchDAO.getLatestLetterBatchId("test-haku-oid-1", "hyvaksymiskirje", "FI", false).get());
        assertEquals(batchId2, letterBatchDAO.getLatestLetterBatchId("test-haku-oid-1", "jalkiohjauskirje", "FI", true).get());
        assertEquals(batchId5, letterBatchDAO.getLatestLetterBatchId("test-haku-oid-2", "hyvaksymiskirje", "FI", false).get());
        assertFalse(letterBatchDAO.getLatestLetterBatchId("test-haku-oid-2", "hyvaksymiskirje", "FI", true).isPresent());

    }

    @Test
    public void getEPostiEmailAddressesByBatchId() throws Exception {
        Long batchId1 = insertLetterBatchForPersonOids("test-haku-oid-1",
                Arrays.asList("test-person-oid-1", "test-person-oid-2", "test-person-oid-3"), "hyvaksymiskirje", true);
        Long batchId2 = insertLetterBatchForPersonOids("test-haku-oid-1",
                Arrays.asList("test-person-oid-1", "test-person-oid-2", "test-person-oid-3"), "hyvaksymiskirje", false);

        assertEquals(Arrays.asList("test-person-oid-1@testi.fi", "test-person-oid-2@testi.fi", "test-person-oid-3@testi.fi"),
                letterBatchDAO.getEPostiEmailAddressesByBatchId(batchId1));
        assertEquals(0, letterBatchDAO.getEPostiEmailAddressesByBatchId(batchId2).size());
    }

    private boolean expectedListItemInList(List<LetterListItem> listItems, String hakuOid, String templateName) {
        for(LetterListItem actual : listItems) {
            if(listItemEquals(actual, hakuOid, templateName)) {
                return true;
            }
        }
        return false;
    }

    private boolean listItemEquals(LetterListItem actual, String hakuOid, String templateName) {
        return hakuOid.equals(actual.getHakuOid()) && templateName.equals(actual.getTyyppi()) && "application/pdf".equals(actual.getTiedostotyyppi());
    }

    private void insertLetterBatchForPersonOid(String hakuOid, String personOid, String templateName, boolean readyForPublish) {
        insertLetterBatchForPersonOids(hakuOid, Arrays.asList(personOid), templateName, readyForPublish);
    }
    private long insertLetterBatchForPersonOids(String hakuOid, List<String> personOids, String templateName, boolean readyForPublish) {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null, personOids.size());
        letterBatch.setApplicationPeriod(hakuOid);
        letterBatch.setTag(hakuOid);
        letterBatch.setTemplateName(templateName);
        letterBatch.setBatchStatus(Status.ready);
        Iterator<LetterReceivers> receiversIterator = letterBatch.getLetterReceivers().iterator();
        int i = 0;
        while(receiversIterator.hasNext()) {
            LetterReceivers letterReceivers = receiversIterator.next();
            letterReceivers.setOidPerson(personOids.get(i));
            letterReceivers.setEmailAddressEPosti(personOids.get(i) + "@testi.fi");
            letterReceivers.getLetterReceiverLetter().setReadyForPublish(readyForPublish);
            letterReceivers.getLetterReceiverLetter().setContentType("application/pdf");
            i++;
        }
        return letterBatchDAO.insert(letterBatch).getId();
    }


    private long givenLetterBatchWithLetter(Status status, byte[] letter) {
        return givenLetterBatchWithDateModified(status, letter, new Date());
    }

    private long givenLetterBatchWithDateModified(Status status, byte[] letter, Date modified) {
        LetterBatch letterBatch = DocumentProviderTestData.getLetterBatch(null);
        letterBatch.setBatchStatus(status);
        letterBatch.getLetterReceivers().iterator().next().getLetterReceiverLetter().setLetter(letter);
        letterBatch.setTimestamp(modified);
        return letterBatchDAO.insert(letterBatch).getId();
    }

}
