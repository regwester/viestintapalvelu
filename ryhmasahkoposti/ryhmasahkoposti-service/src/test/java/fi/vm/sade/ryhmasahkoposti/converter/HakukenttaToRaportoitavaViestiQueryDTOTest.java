package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.ryhmasahkoposti.api.dto.query.RaportoitavaViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.converter.HakukenttaToRaportoitavaViestiQueryDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
public class HakukenttaToRaportoitavaViestiQueryDTOTest {

	@Test
	public void testSahkopostiosoiteHakukentassa() {
		String hakuKentta = "testi.osoite@sposti.fi";
		
		RaportoitavaViestiQueryDTO query = HakukenttaToRaportoitavaViestiQueryDTO.convert(hakuKentta);
		
		assertNotNull(query);
		assertNotNull(query.getVastaanottajaQuery());
		assertEquals(hakuKentta, query.getVastaanottajaQuery().getVastaanottajanSahkopostiosoite());
	}
	
	@Test
	public void testOidHakukentassa() {
		String hakuKentta = "1.2.246.562.24.42645159413";
		
		RaportoitavaViestiQueryDTO query = HakukenttaToRaportoitavaViestiQueryDTO.convert(hakuKentta);
		
		assertNotNull(query);
		assertNotNull(query.getVastaanottajaQuery());
		assertEquals(hakuKentta, query.getVastaanottajaQuery().getVastaanottajanOid());
	}	

	@Test
	public void testHenkilotunnusHakukentassa() {
		String hakuKentta = "100970-965W";
		
		RaportoitavaViestiQueryDTO query = HakukenttaToRaportoitavaViestiQueryDTO.convert(hakuKentta);
		
		assertNotNull(query);
		assertNotNull(query.getVastaanottajaQuery());
		assertEquals(hakuKentta, query.getVastaanottajaQuery().getVastaanottajanHenkilotunnus());
	}	
}
