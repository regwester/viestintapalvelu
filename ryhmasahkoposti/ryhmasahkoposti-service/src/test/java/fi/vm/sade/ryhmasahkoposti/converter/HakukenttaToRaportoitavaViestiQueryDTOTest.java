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
public class HakukenttaToRaportoitavaViestiQueryDTOTest {

	@Test
	public void testSahkopostiosoiteHakukentassa() {
		String hakuKentta = "testi.osoite@sposti.fi";
		
		EmailMessageQueryDTO query = EmailMessageQueryDTO.convert(hakuKentta);
		
		assertNotNull(query);
		assertNotNull(query.getEmailRecipientQueryDTO());
		assertEquals(hakuKentta, query.getEmailRecipientQueryDTO().getRecipientEmail());
	}
	
	@Test
	public void testOidHakukentassa() {
		String hakuKentta = "1.2.246.562.24.42645159413";
		
		EmailMessageQueryDTO query = EmailMessageQueryDTO.convert(hakuKentta);
		
		assertNotNull(query);
		assertNotNull(query.getEmailRecipientQueryDTO());
		assertEquals(hakuKentta, query.getEmailRecipientQueryDTO().getRecipientOid());
	}	

	@Test
	public void testHenkilotunnusHakukentassa() {
		String hakuKentta = "100970-965W";
		
		EmailMessageQueryDTO query = EmailMessageQueryDTO.convert(hakuKentta);
		
		assertNotNull(query);
		assertNotNull(query.getEmailRecipientQueryDTO());
		assertEquals(hakuKentta, query.getEmailRecipientQueryDTO().getRecipientSocialSecurityID());
	}	
}
