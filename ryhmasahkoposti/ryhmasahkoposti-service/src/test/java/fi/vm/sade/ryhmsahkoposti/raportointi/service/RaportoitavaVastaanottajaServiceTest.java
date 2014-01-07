package fi.vm.sade.ryhmsahkoposti.raportointi.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.raportointi.service.RaportoitavaVastaanottajaService;
import fi.vm.sade.ryhmsahkoposti.raportointi.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class RaportoitavaVastaanottajaServiceTest {
	@Autowired
	private RaportoitavaVastaanottajaService raportoitavaVastaanottajaService;

	@Test
	public void testMuodostaRaportoitavaVastaanottaja() {
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = 
			RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti("testi.vastaanottaja@sposti.fi");
		
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
			raportoitavaVastaanottajaService.muodostaRaportoitavaVastaanottaja(lahetettyVastaanottajalle);
		
		assertNotNull(raportoitavaVastaanottaja);
		assertNotNull(raportoitavaVastaanottaja.getVastaanottajaOid());
		assertNotNull(raportoitavaVastaanottaja.getVastaanottajanSahkoposti());
		assertNotNull(raportoitavaVastaanottaja.getLahetysalkoi());
		assertNotNull(raportoitavaVastaanottaja.getLahetyspaattyi());
		assertNotNull(raportoitavaVastaanottaja.getEpaonnistumisenSyy());
	}
}
