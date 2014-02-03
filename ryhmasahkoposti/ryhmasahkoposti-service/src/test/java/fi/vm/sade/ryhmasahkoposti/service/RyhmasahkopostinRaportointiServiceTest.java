package fi.vm.sade.ryhmasahkoposti.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyLiiteDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenLopetusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenTilanneDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaVastaanottajaDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaViestiService;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, 
	DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
public class RyhmasahkopostinRaportointiServiceTest {
	@Autowired
	private GroupEmailReportingService ryhmasahkopostinRaportointiService;
	
	@Autowired
	private RaportoitavaViestiService raportoitavaViestiService;

	@Test
	public void testLahetyksenTulosVastaaRaportoitujatietoja() throws IOException {
		FileItem mockedFileItem = mock(FileItem.class);
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		when(mockedFileItem.getName()).thenReturn("Koekutsu");
		when(mockedFileItem.getContentType()).thenReturn("application/pdf");
		when(mockedFileItem.get()).thenReturn(sisalto);
		
		Long liiteID = ryhmasahkopostinRaportointiService.saveAttachment(mockedFileItem);
		
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO vastaanottaja = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		vastaanottaja.setVastaanottajanSahkoposti("testLahetyksenTulosVastaaRaportoitujatietoja@sposti.fi");
		vastaanottaja.setEpaonnistumisenSyy("Failed");
		
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(vastaanottaja);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		LahetettyLiiteDTO liite = RaportointipalveluTestData.getLahetettyLiiteDTO(liiteID);
		List<LahetettyLiiteDTO> liitteet = new ArrayList<LahetettyLiiteDTO>();
		liitteet.add(liite);
		lahetyksenAloitus.setLahetetynviestinliitteet(liitteet);
		
		Long viestiID = ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);
		
		LahetyksenTilanneDTO tulos = ryhmasahkopostinRaportointiService.haeLahetyksenTulos(viestiID);
		
		assertEquals(new Long(1), tulos.getVastaanottajienLukumaara());
		assertEquals(new Long(1), tulos.getLahetysEpaonnistuiLukumaara());
		assertEquals(new Long(0), tulos.getLahetysOnnistuiLukumaara());
	}
	
	@Test
	public void testRaportoiLahetyksenAloitus() throws IOException {
		FileItem mockedFileItem = mock(FileItem.class);
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		when(mockedFileItem.getName()).thenReturn("Koekutsu");
		when(mockedFileItem.getContentType()).thenReturn("application/pdf");
		when(mockedFileItem.get()).thenReturn(sisalto);
		
		Long liiteID = ryhmasahkopostinRaportointiService.saveAttachment(mockedFileItem);
		
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();

		LahetettyVastaanottajalleDTO vastaanottaja = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		vastaanottaja.setVastaanottajanSahkoposti("testRaportoiLahetyksenAloitus@sposti.fi");
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(vastaanottaja);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		LahetettyLiiteDTO liite = RaportointipalveluTestData.getLahetettyLiiteDTO(liiteID);
		List<LahetettyLiiteDTO> liitteet = new ArrayList<LahetettyLiiteDTO>();
		liitteet.add(liite);
		lahetyksenAloitus.setLahetetynviestinliitteet(liitteet);

		Long viestiID = ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);		

		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.haeRaportoitavaViesti(viestiID);
		
		assertNotNull(raportoitavaViesti);
		assertNotNull(raportoitavaViesti.getId());
		assertNotNull(raportoitavaViesti.getRaportoitavatVastaanottajat());
	}
	
	@Test
	public void testRaportoiLahetyksenLopetus() throws IOException {
		FileItem mockedFileItem = mock(FileItem.class);
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		when(mockedFileItem.getName()).thenReturn("Koekutsu");
		when(mockedFileItem.getContentType()).thenReturn("application/pdf");
		when(mockedFileItem.get()).thenReturn(sisalto);
		
		Long liiteID = ryhmasahkopostinRaportointiService.saveAttachment(mockedFileItem);

		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti("testRaportoiLahetyksenLopetus@sposti.fi");
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(lahetettyVastaanottajalle);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);

		LahetettyLiiteDTO liite = RaportointipalveluTestData.getLahetettyLiiteDTO(liiteID);
		List<LahetettyLiiteDTO> liitteet = new ArrayList<LahetettyLiiteDTO>();
		liitteet.add(liite);
		lahetyksenAloitus.setLahetetynviestinliitteet(liitteet);

		Long viestiID = ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);
		
		LahetyksenLopetusDTO lahetyksenLopetus = 
			RaportointipalveluTestData.getLahetyksenLopetusDTO(viestiID);		
		ryhmasahkopostinRaportointiService.raportoiLahetyksenLopetus(lahetyksenLopetus);
		
		List<RaportoitavaViesti> raportoitavatViestit = raportoitavaViestiService.haeRaportoitavatViestit();
		
		assertNotNull(raportoitavatViestit);
		assertEquals(lahetyksenLopetus.getLahetysPaattyi(), raportoitavatViestit.get(0).getLahetysPaattyi());		
	}

	@Test
	public void testRaportoiLahetysVastaanottajalleOnnistui() throws IOException {
		FileItem mockedFileItem = mock(FileItem.class);
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		when(mockedFileItem.getName()).thenReturn("Koekutsu");
		when(mockedFileItem.getContentType()).thenReturn("application/pdf");
		when(mockedFileItem.get()).thenReturn(sisalto);
		
		Long liiteID = ryhmasahkopostinRaportointiService.saveAttachment(mockedFileItem);
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO vastaanottaja = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		vastaanottaja.setLahetysalkoi(null);
		vastaanottaja.setLahetyspaattyi(null);
		vastaanottaja.setVastaanottajanSahkoposti("testRaportoiLahetysVastaanottajalleOnnistui@sposti.fi");
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(vastaanottaja);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		LahetettyLiiteDTO liite = RaportointipalveluTestData.getLahetettyLiiteDTO(liiteID);
		List<LahetettyLiiteDTO> liitteet = new ArrayList<LahetettyLiiteDTO>();
		liitteet.add(liite);
		lahetyksenAloitus.setLahetetynviestinliitteet(liitteet);
		
		Long viestiID = ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);

		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = new LahetettyVastaanottajalleDTO();
		lahetettyVastaanottajalle.setViestiID(viestiID);
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti(vastaanottaja.getVastaanottajanSahkoposti());
		lahetettyVastaanottajalle.setLahetysalkoi(new Date());
		lahetettyVastaanottajalle.setLahetyspaattyi(null);
		lahetettyVastaanottajalle.setEpaonnistumisenSyy("");

		boolean vastaus = 
			ryhmasahkopostinRaportointiService.raportoiLahetyksenTilanne(lahetettyVastaanottajalle);
		
		assertTrue(vastaus);
		
		RaportoitavaViesti raportoitavaViesti = raportoitavaViestiService.haeRaportoitavaViesti(viestiID);
		
		assertNotNull(raportoitavaViesti);
		assertNotNull(raportoitavaViesti.getRaportoitavatVastaanottajat());
		
		Iterator<RaportoitavaVastaanottaja> iterator = raportoitavaViesti.getRaportoitavatVastaanottajat().iterator();
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = iterator.next();
			
		
		assertNotNull(raportoitavaVastaanottaja.getLahetysalkoi());
		assertNull(raportoitavaVastaanottaja.getLahetyspaattyi());
		assertEquals(raportoitavaVastaanottaja.getEpaonnistumisenSyy(), "");
	}
	
	@Test
	public void testRaportoiLahetysVastaanottajalleEpaonnistui() throws IOException {
		FileItem mockedFileItem = mock(FileItem.class);
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		when(mockedFileItem.getName()).thenReturn("Koekutsu");
		when(mockedFileItem.getContentType()).thenReturn("application/pdf");
		when(mockedFileItem.get()).thenReturn(sisalto);
		
		Long liiteID = ryhmasahkopostinRaportointiService.saveAttachment(mockedFileItem);

		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO vastaanottaja = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		vastaanottaja.setLahetyspaattyi(null);
		vastaanottaja.setVastaanottajanSahkoposti("testRaportoiLahetysVastaanottajalleEpaonnistui@sposti.fi");
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(vastaanottaja);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		LahetettyLiiteDTO liite = RaportointipalveluTestData.getLahetettyLiiteDTO(liiteID);
		List<LahetettyLiiteDTO> liitteet = new ArrayList<LahetettyLiiteDTO>();
		liitteet.add(liite);
		lahetyksenAloitus.setLahetetynviestinliitteet(liitteet);
		
		Long viestiID = ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);

		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = new LahetettyVastaanottajalleDTO();
		lahetettyVastaanottajalle.setViestiID(viestiID);
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti(vastaanottaja.getVastaanottajanSahkoposti());
		lahetettyVastaanottajalle.setLahetysalkoi(new Date());
		lahetettyVastaanottajalle.setLahetyspaattyi(null);
		lahetettyVastaanottajalle.setEpaonnistumisenSyy("");

		boolean vastaus = 
			ryhmasahkopostinRaportointiService.raportoiLahetyksenTilanne(lahetettyVastaanottajalle);
		
		assertFalse(vastaus);
	}
	
	@Test
	public void testRaportoitujenViestienHakuOnnistui() throws IOException {
		FileItem mockedFileItem = mock(FileItem.class);
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		when(mockedFileItem.getName()).thenReturn("Koekutsu");
		when(mockedFileItem.getContentType()).thenReturn("application/pdf");
		when(mockedFileItem.get()).thenReturn(sisalto);
		
		Long liiteID = ryhmasahkopostinRaportointiService.saveAttachment(mockedFileItem);

		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO vastaanottaja = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		vastaanottaja.setLahetysalkoi(null);
		vastaanottaja.setLahetyspaattyi(null);
		vastaanottaja.setVastaanottajanSahkoposti("testRaportoitujenViestienHakuOnnistui@sposti.fi");
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(vastaanottaja);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);

		LahetettyLiiteDTO liite = RaportointipalveluTestData.getLahetettyLiiteDTO(liiteID);
		List<LahetettyLiiteDTO> liitteet = new ArrayList<LahetettyLiiteDTO>();
		liitteet.add(liite);
		lahetyksenAloitus.setLahetetynviestinliitteet(liitteet);

		Long viestiID = ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);

		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = new LahetettyVastaanottajalleDTO();
		lahetettyVastaanottajalle.setViestiID(viestiID);
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti(vastaanottaja.getVastaanottajanSahkoposti());
		lahetettyVastaanottajalle.setLahetysalkoi(new Date());
		lahetettyVastaanottajalle.setLahetyspaattyi(null);
		lahetettyVastaanottajalle.setEpaonnistumisenSyy("");

		ryhmasahkopostinRaportointiService.raportoiLahetyksenTilanne(lahetettyVastaanottajalle);

        String hakuKentta = "testRaportoitujenViestienHakuOnnistui@sposti.fi";
		List<RaportoitavaViestiDTO> viestit = ryhmasahkopostinRaportointiService.getMessages(hakuKentta);
		
		assertNotNull(viestit);
		assertTrue(viestit.size() > 0);
	}
	
	@Test
	public void testLiitteenTallennusOnnistuu() throws IOException {
		FileItem mockedFileItem = mock(FileItem.class);
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		when(mockedFileItem.getName()).thenReturn("Koekutsu");
		when(mockedFileItem.getContentType()).thenReturn("application/pdf");
		when(mockedFileItem.get()).thenReturn(sisalto);
		
		Long liiteID = ryhmasahkopostinRaportointiService.saveAttachment(mockedFileItem);
		
		assertNotNull(liiteID);
		assertTrue(liiteID.longValue() > 0);
	}
	
	@Test
	public void testRaportoitavanViestinHakuOnnistuu() throws IOException {
		FileItem mockedFileItem = mock(FileItem.class);
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		when(mockedFileItem.getName()).thenReturn("koekutsu.doc");
		when(mockedFileItem.getContentType()).thenReturn("application/pdf");
		when(mockedFileItem.get()).thenReturn(sisalto);
		
		Long liiteID = ryhmasahkopostinRaportointiService.saveAttachment(mockedFileItem);

		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();
		
		LahetettyVastaanottajalleDTO vastaanottaja = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		vastaanottaja.setLahetysalkoi(null);
		vastaanottaja.setLahetyspaattyi(null);
		vastaanottaja.setVastaanottajanSahkoposti("testRaportoitavanViestinHakuOnnistuu@sposti.fi");
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(vastaanottaja);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);

		LahetettyLiiteDTO liite = RaportointipalveluTestData.getLahetettyLiiteDTO(liiteID);
		List<LahetettyLiiteDTO> liitteet = new ArrayList<LahetettyLiiteDTO>();
		liitteet.add(liite);
		lahetyksenAloitus.setLahetetynviestinliitteet(liitteet);

		Long viestiID = ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);

		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = new LahetettyVastaanottajalleDTO();
		lahetettyVastaanottajalle.setViestiID(viestiID);
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti(vastaanottaja.getVastaanottajanSahkoposti());
		lahetettyVastaanottajalle.setLahetysalkoi(new Date());
		lahetettyVastaanottajalle.setLahetyspaattyi(null);
		lahetettyVastaanottajalle.setEpaonnistumisenSyy("");

		ryhmasahkopostinRaportointiService.raportoiLahetyksenTilanne(lahetettyVastaanottajalle);

		RaportoitavaViestiDTO viestiDTO = ryhmasahkopostinRaportointiService.haeRaportoitavaViesti(viestiID, true);
		
		assertNotNull(viestiDTO);
		assertNotNull(viestiDTO.getVastaanottajat());
		assertEquals(vastaanottaja.getVastaanottajanSahkoposti(), viestiDTO.getVastaanottajat().get(0).getVastaanottajanSahkopostiosoite());
		assertNotNull(viestiDTO.getLiitetiedostot());
		assertEquals(mockedFileItem.getName(), viestiDTO.getLiitetiedostot().get(0).getLiitetiedostonNimi());
	}
	
	@Test
	public void testHaeRaportoitavatVastaanottajatViestiLahettamatta() throws IOException {
		FileItem mockedFileItem = mock(FileItem.class);
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		when(mockedFileItem.getName()).thenReturn("Koekutsu");
		when(mockedFileItem.getContentType()).thenReturn("application/pdf");
		when(mockedFileItem.get()).thenReturn(sisalto);
		
		Long liiteID = ryhmasahkopostinRaportointiService.saveAttachment(mockedFileItem);
		
		LahetyksenAloitusDTO lahetyksenAloitus = RaportointipalveluTestData.getLahetyksenAloitusDTO();

		LahetettyVastaanottajalleDTO vastaanottaja = RaportointipalveluTestData.getLahetettyVastaanottajalleDTO();
		vastaanottaja.setVastaanottajanSahkoposti("testRaportoiLahetyksenAloitus@sposti.fi");
		vastaanottaja.setLahetysalkoi(null);
		vastaanottaja.setLahetyspaattyi(null);
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		vastaanottajat.add(vastaanottaja);
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
		
		LahetettyLiiteDTO liite = RaportointipalveluTestData.getLahetettyLiiteDTO(liiteID);
		List<LahetettyLiiteDTO> liitteet = new ArrayList<LahetettyLiiteDTO>();
		liitteet.add(liite);
		lahetyksenAloitus.setLahetetynviestinliitteet(liitteet);

		ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);		

		List<RaportoitavaVastaanottajaDTO> vastaanottajatDTO = 
			ryhmasahkopostinRaportointiService.haeRaportoitavatVastaanottajatViestiLahettamatta(10); 
		
		assertNotNull(vastaanottajatDTO);
		assertNotNull(vastaanottajatDTO.get(0).getVastaanottajaID());
	}
}
