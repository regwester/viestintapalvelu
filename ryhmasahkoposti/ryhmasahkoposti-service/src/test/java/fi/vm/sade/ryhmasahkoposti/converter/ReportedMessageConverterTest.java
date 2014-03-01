package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
public class ReportedMessageConverterTest {

	@Test
	public void testReportedMessageConversion() throws IOException {
		EmailMessage emailMessage = RaportointipalveluTestData.getEmailMessage();
		
		ReportedMessage reportedMessage = 
			ReportedMessageConverter.convert(emailMessage);

		assertNotNull(reportedMessage);
		assertEquals(emailMessage.getSenderOid(), reportedMessage.getSenderOid());
		assertEquals(emailMessage.getSenderEmail(), reportedMessage.getSenderEmail());
	}
}
