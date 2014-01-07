package fi.vm.sade.ryhmsahkoposti.raportointi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenLopetusDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenTulosDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.raportointi.service.RaportoitavaViestiService;
import fi.vm.sade.ryhmasahkoposti.raportointi.service.RyhmasahkopostinRaportointiService;
import fi.vm.sade.ryhmsahkoposti.raportointi.testdata.RaportointipalveluTestData;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class RyhmasahkopostinRaportointiServiceTest {
	@Autowired
	private RyhmasahkopostinRaportointiService ryhmasahkopostinRaportointiService;
	
	@Autowired
	private RaportoitavaViestiService raportoitavaViestiService;

	@Test
	public void testLahetyksenTulosVastaaRaportoitujatietoja() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO vastaanottaja = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		vastaanottaja.setVastaanottajanSahkoposti("testLahetyksenTulosVastaaRaportoitujatietoja@sposti.fi");
		vastaanottaja.setEpaonnistumisenSyy("Failed");
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(vastaanottaja);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		Long viestiID = ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);
		
		LahetyksenTulosDTO tulos = ryhmasahkopostinRaportointiService.haeLahetyksenTulos(viestiID);
		
		assertEquals(new Long(1), tulos.getVastaanottajienLukumaara());
		assertEquals(new Long(1), tulos.getLahetysEpaonnistuiLukumaara());
		assertEquals(new Long(0), tulos.getLahetysOnnistuiLukumaara());
	}
	
	@Test
	public void testRaportoiLahetyksenAloitus() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();

		LahetettyVastaanottajalleDTO vastaanottaja = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		vastaanottaja.setVastaanottajanSahkoposti("testRaportoiLahetyksenAloitus@sposti.fi");
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(vastaanottaja);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);

		Long viestiID = ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);		

		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.haeRaportoitavaViesti(viestiID);
		
		assertNotNull(raportoitavaViesti);
		assertNotNull(raportoitavaViesti.getId());
		assertNotNull(raportoitavaViesti.getRaportoitavatVastaanottajat());
	}
	
	@Test
	public void testRaportoiLahetyksenLopetus() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti("testRaportoiLahetyksenLopetus@sposti.fi");
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(lahetettyVastaanottajalle);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		Long viestiID = ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);
		
		LahetyksenLopetusDTO lahetyksenLopetus = 
			RaportointipalveluTestData.getLahetyksenLopetusDTO(viestiID);		
		ryhmasahkopostinRaportointiService.raportoiLahetyksenLopetus(lahetyksenLopetus);
		
		List<RaportoitavaViesti> raportoitavatViestit = raportoitavaViestiService.haeRaportoitavatViestit();
		
		assertNotNull(raportoitavatViestit);
		assertEquals(lahetyksenLopetus.getLahetysPaattyi(), raportoitavatViestit.get(0).getLahetysPaattyi());		
	}
	
	@Test
	public void testRaportoiLahetysVastaanottajalle() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO vastaanottaja = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		vastaanottaja.setVastaanottajanSahkoposti("testRaportoiLahetysVastaanottajalle@sposti.fi");
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(vastaanottaja);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		Long viestiID = ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);

		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = new LahetettyVastaanottajalleDTO();
		lahetettyVastaanottajalle.setViestiID(viestiID);
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti(vastaanottaja.getVastaanottajanSahkoposti());
		lahetettyVastaanottajalle.setLahetysalkoi(new Date());
		lahetettyVastaanottajalle.setLahetyspaattyi(new Date());
		lahetettyVastaanottajalle.setEpaonnistumisenSyy("Failed");

		ryhmasahkopostinRaportointiService.raportoiLahetysVastaanottajalle(lahetettyVastaanottajalle);
		
		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.haeRaportoitavaViesti(viestiID);
		
		assertNotNull(raportoitavaViesti);
		assertNotNull(raportoitavaViesti.getRaportoitavatVastaanottajat());
		
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
			raportoitavaViesti.getRaportoitavatVastaanottajat().get(0);
		
		assertNotNull(raportoitavaVastaanottaja.getLahetysalkoi());
		assertNotNull(raportoitavaVastaanottaja.getLahetyspaattyi());
		assertEquals(raportoitavaVastaanottaja.getEpaonnistumisenSyy(), lahetettyVastaanottajalle.getEpaonnistumisenSyy());
	}
}
