package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedMessageQueryDTOConverter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
public class ReportedMessageQueryDTOConverterTest {

	@Test
	public void testEmailInSearchArgument() {
		String searchArgument = "testi.osoite@sposti.fi";
		
		ReportedMessageQueryDTO query = ReportedMessageQueryDTOConverter.convert(searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getReportedRecipientQueryDTO());
		assertEquals(searchArgument, query.getReportedRecipientQueryDTO().getRecipientEmail());
	}
	
	@Test
	public void testOidInSearchArgument() {
		String searchArgument = "1.2.246.562.24.42645159413";
		
		ReportedMessageQueryDTO query = ReportedMessageQueryDTOConverter.convert(searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getReportedRecipientQueryDTO());
		assertEquals(searchArgument, query.getReportedRecipientQueryDTO().getRecipientOid());
	}	

	@Test
	public void testSocialSecurityIdInSearchArgument() {
		String searchArgument = "100970-965W";
		
		ReportedMessageQueryDTO query = ReportedMessageQueryDTOConverter.convert(searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getReportedRecipientQueryDTO());
		assertEquals(searchArgument, query.getReportedRecipientQueryDTO().getRecipientSocialSecurityID());
	}	
}
