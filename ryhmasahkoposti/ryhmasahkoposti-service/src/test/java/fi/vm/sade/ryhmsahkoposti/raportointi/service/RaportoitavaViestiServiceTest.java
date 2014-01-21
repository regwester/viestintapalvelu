package fi.vm.sade.ryhmsahkoposti.raportointi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.RaportoitavaVastaanottajaQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.RaportoitavaViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaVastaanottajaService;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaViestiService;
import fi.vm.sade.ryhmsahkoposti.raportointi.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class RaportoitavaViestiServiceTest {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
		
	@Autowired
	private RaportoitavaViestiService raportoitavaViestiService;
	@Autowired
	private RaportoitavaVastaanottajaService raportoitavaVastaanottajaService;

	@Test
	public void testRaportoitavanViestinMuodostusOnnistuu() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(lahetettyVastaanottajalle);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.muodostaRaportoitavaViesti(lahetyksenAloitus);

		assertNotNull(raportoitavaViesti);
		assertEquals(lahetyksenAloitus.getLahettajanOid(), raportoitavaViesti.getLahettajanOid());
		assertEquals(lahetyksenAloitus.getVastauksensaajaOid(), raportoitavaViesti.getVastauksensaajanOid());
	}

	@Test
	public void testTallennaRaportoitavaViestiOnnistuu() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(lahetettyVastaanottajalle);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.muodostaRaportoitavaViesti(lahetyksenAloitus);
		RaportoitavaViesti tallennettuRaportoitavaViesti = 
			raportoitavaViestiService.tallennaRaportoitavaViesti(raportoitavaViesti);
		
		assertNotNull(tallennettuRaportoitavaViesti.getId());
		assertNotNull(tallennettuRaportoitavaViesti.getVersion());
	}

	@Test
	public void testHaeRaportoitavatViestitLoytyvat() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(lahetettyVastaanottajalle);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.muodostaRaportoitavaViesti(lahetyksenAloitus);
 		raportoitavaViestiService.tallennaRaportoitavaViesti(raportoitavaViesti);

		List<RaportoitavaViesti> raportoitavatViestit = raportoitavaViestiService.haeRaportoitavatViestit();
		
		assertNotNull(raportoitavatViestit);
		assertNotEquals(0, raportoitavatViestit.size());
	}

	@Test
	public void testRaportoitavatViestitLoytyvatHakutekijoilla() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(lahetettyVastaanottajalle);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.muodostaRaportoitavaViesti(lahetyksenAloitus);
		List<RaportoitavaVastaanottaja> raportoitavatVastaanottajat = 
			raportoitavaVastaanottajaService.muodostaRaportoitavatVastaanottajat(raportoitavaViesti, vastaanottajat);
		raportoitavaViesti.setRaportoitavatVastaanottajat(raportoitavatVastaanottajat);
 		raportoitavaViestiService.tallennaRaportoitavaViesti(raportoitavaViesti);

		RaportoitavaViestiQueryDTO raportoitavaViestiQuery = new RaportoitavaViestiQueryDTO();
        RaportoitavaVastaanottajaQueryDTO raportoitavaVastaanottajaQuery = new RaportoitavaVastaanottajaQueryDTO();
        raportoitavaVastaanottajaQuery.setVastaanottajanSahkopostiosoite("vastaan.ottaja@sposti.fi");
        raportoitavaViestiQuery.setVastaanottajaQuery(raportoitavaVastaanottajaQuery);

		List<RaportoitavaViesti> raportoitavatViestit = 
			raportoitavaViestiService.haeRaportoitavatViestit(raportoitavaViestiQuery);
		
		assertNotNull(raportoitavatViestit);
		assertNotEquals(0, raportoitavatViestit.size());		
	}
	
	@Test
	public void testHaeRaportoitavaViestiPaaAvaimella() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(lahetettyVastaanottajalle);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.muodostaRaportoitavaViesti(lahetyksenAloitus);
		RaportoitavaViesti tallennettuRaportoitavaViesti = 
			raportoitavaViestiService.tallennaRaportoitavaViesti(raportoitavaViesti);

		RaportoitavaViesti haettuRaportoitavaViesti = 
			raportoitavaViestiService.haeRaportoitavaViesti(tallennettuRaportoitavaViesti.getId());
		
		assertNotNull(haettuRaportoitavaViesti);
	}

	@Test
	public void testHakuPaaAvaimellaEpaonnistuu() {		
		RaportoitavaViesti haettuRaportoitavaViesti = raportoitavaViestiService.haeRaportoitavaViesti(new Long(1330));
		
		assertNull(haettuRaportoitavaViesti);
	}

	@Test
	public void testHaeRaportoitavaViestiLahetystunnuksella() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(lahetettyVastaanottajalle);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.muodostaRaportoitavaViesti(lahetyksenAloitus);
		RaportoitavaViesti tallennettuRaportoitavaViesti = 
			raportoitavaViestiService.tallennaRaportoitavaViesti(raportoitavaViesti);

		RaportoitavaViesti haettuRaportoitavaViesti = 
			raportoitavaViestiService.haeRaportoitavaViesti(tallennettuRaportoitavaViesti.getId());
		
		assertNotNull(haettuRaportoitavaViesti);		
	}

	@Test
	public void testPaivitaRaportoitavaViesti() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(lahetettyVastaanottajalle);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.muodostaRaportoitavaViesti(lahetyksenAloitus);
		RaportoitavaViesti tallennettuRaportoitavaViesti = 
			raportoitavaViestiService.tallennaRaportoitavaViesti(raportoitavaViesti);
		
		assertEquals(new Long(0), tallennettuRaportoitavaViesti.getVersion());
	 
		tallennettuRaportoitavaViesti.setLahetysPaattyi(new Date());
		raportoitavaViestiService.paivitaRaportoitavaViesti(tallennettuRaportoitavaViesti);

		RaportoitavaViesti haettuRaportoitavaViesti = 
			raportoitavaViestiService.haeRaportoitavaViesti(tallennettuRaportoitavaViesti.getId());

		assertEquals(new Long(1), haettuRaportoitavaViesti.getVersion());
	}
}
