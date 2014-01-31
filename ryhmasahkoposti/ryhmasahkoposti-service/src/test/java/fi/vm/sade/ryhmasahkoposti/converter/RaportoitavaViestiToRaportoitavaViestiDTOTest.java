package fi.vm.sade.ryhmasahkoposti.raportointi.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.converter.RaportoitavaViestiToRaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.raportointi.testdata.RaportointipalveluTestData;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaVastaanottajaService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
public class RaportoitavaViestiToRaportoitavaViestiDTOTest {
	
	@Test
	public void testRaportoitavienviestienConvertOnnistuu() {
		List<RaportoitavaViesti> raportoitavatViestit = new ArrayList<RaportoitavaViesti>();
		raportoitavatViestit.add(RaportointipalveluTestData.getRaportoitavatViesti());
		Long viestiID = new Long(1);
		
		RaportoitavaVastaanottajaService raportoitavaVastaanottajaService = mock(RaportoitavaVastaanottajaService.class);
		when(raportoitavaVastaanottajaService.haeRaportoitavienVastaanottajienLukumaara(viestiID, false)).thenReturn(new Long(0));		
		
		List<RaportoitavaViestiDTO> viestit = RaportoitavaViestiToRaportoitavaViestiDTO.convert(raportoitavatViestit);
		
		assertNotNull(viestit);
		assertTrue(viestit.size() == 1);
		assertNotNull(viestit.get(0).getLahetysraportti());
	}

	@Test
	public void testRaportoitavienviestinConvertOnnistuu() {
		RaportoitavaViesti raportoitavaViesti = RaportointipalveluTestData.getRaportoitavatViesti();
		
		List<RaportoitavaLiite> raportoitavatLiitteet = new ArrayList<RaportoitavaLiite>();
		RaportoitavaLiite raportoitavaLiite = RaportointipalveluTestData.getRaportoitavaLiite();
		raportoitavatLiitteet.add(raportoitavaLiite);
				
		RaportoitavaViestiDTO viestiDTO = 
			RaportoitavaViestiToRaportoitavaViestiDTO.convert(raportoitavaViesti, raportoitavatLiitteet, false);
		
		assertNotNull(viestiDTO);
		assertEquals(raportoitavaViesti.getId(), viestiDTO.getViestiID());
		assertEquals(raportoitavaViesti.getViesti(), viestiDTO.getViestinSisalto());
		assertTrue(viestiDTO.getVastaanottajat().size() > 0);
		assertNotNull(viestiDTO.getVastaanottajat().get(0).getVastaanottajanSahkopostiosoite());
		assertTrue(viestiDTO.getLiitetiedostot().size() > 0);
		assertNotNull(viestiDTO.getLiitetiedostot().get(0).getLiitetiedostonNimi());
	}
}
