package fi.vm.sade.ryhmsahkoposti.raportointi.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.converter.RaportoitavaViestiToRaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaVastaanottajaService;
import fi.vm.sade.ryhmsahkoposti.raportointi.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
public class RaportoitavaViestiToRaportoitavaViestiDTOTest {
	
	@Test
	public void testConvertOnnistuu() {
		List<RaportoitavaViesti> raportoitavatViestit = RaportointipalveluTestData.getRaportoitavaViestiLista();
		Long viestiID = new Long(1);
		
		RaportoitavaVastaanottajaService raportoitavaVastaanottajaService = mock(RaportoitavaVastaanottajaService.class);
		when(raportoitavaVastaanottajaService.haeRaportoitavienVastaanottajienLukumaara(viestiID, false)).thenReturn(new Long(0));		
		
		List<RaportoitavaViestiDTO> viestit = RaportoitavaViestiToRaportoitavaViestiDTO.convert(raportoitavatViestit);
		
		assertNotNull(viestit);
		assertTrue(viestit.size() == 2);
		assertNotNull(viestit.get(0).getLahetysraportti());
	}

}
