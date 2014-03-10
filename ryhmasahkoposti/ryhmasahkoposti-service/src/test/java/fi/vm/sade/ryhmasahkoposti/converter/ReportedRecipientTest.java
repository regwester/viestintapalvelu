package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class ReportedRecipientTest {
	@Test
	public void testReportedRecipientSuccesful() {
		EmailRecipient emailRecipient = RaportointipalveluTestData.getEmailRecipient();
		emailRecipient.setEmail("testMuodostaRaportoitavaVastaanottaja@sposti.fi");
		
		ReportedRecipient reportedRecipient = ReportedRecipientConverter.convert(emailRecipient);
		
		assertNotNull(reportedRecipient);
		assertNotNull(reportedRecipient.getRecipientOid());
		assertNotNull(reportedRecipient.getRecipientEmail());
		assertTrue(reportedRecipient.getSendingStarted() == null);
		assertTrue(reportedRecipient.getSendingEnded() == null);
		assertTrue(reportedRecipient.getFailureReason() == null);
		assertTrue(reportedRecipient.getSendingSuccesful() == null);
	}
}
