package fi.vm.sade.ryhmsahkoposti.raportointi.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.converter.LahetyksenAloitusDTOToRaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmsahkoposti.raportointi.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
public class LahetyksenAloitusDTOToRaportoitavaViestiTest {

	@Test
	public void testRaportoitavanViestinMuodostusOnnistuu() throws IOException {
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = 
			RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(lahetettyVastaanottajalle);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		RaportoitavaViesti raportoitavaViesti = 
			LahetyksenAloitusDTOToRaportoitavaViesti.convert(lahetyksenAloitus);

		assertNotNull(raportoitavaViesti);
		assertEquals(lahetyksenAloitus.getLahettajanOid(), raportoitavaViesti.getLahettajanOid());
		assertEquals(lahetyksenAloitus.getVastauksensaajaOid(), raportoitavaViesti.getVastauksensaajanOid());
	}
}
