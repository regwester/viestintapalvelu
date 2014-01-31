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

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.converter.LahetettyVastaanottajalleDTOToRaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class LahetettyVastaanottajalleDTOToRaportoitavaVastaanottajaTest {
	@Test
	public void testRaportoitavanVastaanottajanMuodostaminenOnnistuu() {
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = 
			RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti("testMuodostaRaportoitavaVastaanottaja@sposti.fi");
		lahetettyVastaanottajalle.setLahetyspaattyi(null);
		
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
			LahetettyVastaanottajalleDTOToRaportoitavaVastaanottaja.convert(lahetettyVastaanottajalle);
		
		assertNotNull(raportoitavaVastaanottaja);
		assertNotNull(raportoitavaVastaanottaja.getVastaanottajaOid());
		assertNotNull(raportoitavaVastaanottaja.getVastaanottajanSahkoposti());
		assertNotNull(raportoitavaVastaanottaja.getLahetysalkoi());
		assertTrue(raportoitavaVastaanottaja.getLahetyspaattyi() == null);
		assertNotNull(raportoitavaVastaanottaja.getEpaonnistumisenSyy());
		assertTrue(raportoitavaVastaanottaja.getLahetysOnnistui() == null);
	}
	
	@Test
	public void testLahetysOnnistuiOnArvoltaanEpaonnistui() {
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = 
			RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti("testLahetysOnnistuiOnArvoltaanEpaonnistui@sposti.fi");
		lahetettyVastaanottajalle.setEpaonnistumisenSyy("Postilaatikko täynnä");
		
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
			LahetettyVastaanottajalleDTOToRaportoitavaVastaanottaja.convert(lahetettyVastaanottajalle);
		
		assertNotNull(raportoitavaVastaanottaja);
		assertNotNull(raportoitavaVastaanottaja.getEpaonnistumisenSyy());
		assertTrue(raportoitavaVastaanottaja.getLahetysOnnistui().equals("0"));
	}	
}
