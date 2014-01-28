package fi.vm.sade.ryhmsahkoposti.raportointi.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyLiiteDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaLiiteService;
import fi.vm.sade.ryhmsahkoposti.raportointi.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(readOnly=true)
public class RaportoitavaLiiteServiceTest {
	@Autowired
	private RaportoitavaLiiteService raportoitavaLiiteService;
	
	@Test
	public void testRaportoitavanLiitteenTallennusOnnistuu() {
		RaportoitavaLiite raportoitavaLiite = RaportointipalveluTestData.getRaportoitavaLiite();
		
		Long liitteenID = raportoitavaLiiteService.tallennaRaportoitavaLiite(raportoitavaLiite);
		
		assertNotNull(liitteenID);
		assertTrue(liitteenID.longValue() > 0);
	}
	
	@Test
	public void testTallennetunLiitteenHakuOnnistuu() {
		RaportoitavaLiite raportoitavaLiite = RaportointipalveluTestData.getRaportoitavaLiite();		
		Long liitteenID = raportoitavaLiiteService.tallennaRaportoitavaLiite(raportoitavaLiite);
	
		LahetettyLiiteDTO lahetettyLiiteDTO = RaportointipalveluTestData.getLahetettyLiiteDTO(liitteenID);
		List<LahetettyLiiteDTO> lahetetytLiitteet = new ArrayList<LahetettyLiiteDTO>();
		lahetetytLiitteet.add(lahetettyLiiteDTO);
		
		List<RaportoitavaLiite> liitteet = raportoitavaLiiteService.haeRaportoitavatLiitteet(lahetetytLiitteet);
		
		assertNotNull(liitteet);
		assertEquals(liitteet.get(0).getId(), liitteenID);
	}	
}
