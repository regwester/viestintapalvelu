package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.converter.EmailMessageQueryDTOConverter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
public class EmailMessageQueryDTOConverterTest {

	@Test
	public void testEmailInSearchArgument() {
		String searchArgument = "testi.osoite@sposti.fi";
		
		EmailMessageQueryDTO query = EmailMessageQueryDTOConverter.convert(searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getEmailRecipientQueryDTO());
		assertEquals(searchArgument, query.getEmailRecipientQueryDTO().getRecipientEmail());
	}
	
	@Test
	public void testOidInSearchArgument() {
		String searchArgument = "1.2.246.562.24.42645159413";
		
		EmailMessageQueryDTO query = EmailMessageQueryDTOConverter.convert(searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getEmailRecipientQueryDTO());
		assertEquals(searchArgument, query.getEmailRecipientQueryDTO().getRecipientOid());
	}	

	@Test
	public void testSocialSecurityIdInSearchArgument() {
		String searchArgument = "100970-965W";
		
		EmailMessageQueryDTO query = EmailMessageQueryDTOConverter.convert(searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getEmailRecipientQueryDTO());
		assertEquals(searchArgument, query.getEmailRecipientQueryDTO().getRecipientSocialSecurityID());
	}	
}
