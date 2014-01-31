package fi.vm.sade.ryhmasahkoposti.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavaVastaanottajaDAO;
import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavaViestiDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@Transactional(readOnly = true)
public class RaportoitavaVastaanottajaDAOTest {
    @Autowired
    private RaportoitavaVastaanottajaDAO raportoitavaVastaanottajaDAO;
    @Autowired
    private RaportoitavaViestiDAO raportoitavaViestiDAO;

    @Test
    public void testVastaanottajanHakuOnnistuuViestiIdJaSahkopostiosoitteella() {
        RaportoitavaViesti raportoitavaViesti = RaportointipalveluTestData.getRaportoitavaViestiLahetyksenAloitusDTOTiedosta();
        RaportoitavaViesti tallennettuRaportoitavaViesti = raportoitavaViestiDAO.insert(raportoitavaViesti);

        RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
        	RaportointipalveluTestData.getRaportoitavaVastaanottaja(tallennettuRaportoitavaViesti);
        Date lahetysajankohta = new Date();
        raportoitavaVastaanottaja.setLahetyspaattyi(lahetysajankohta);
        raportoitavaVastaanottajaDAO.insert(raportoitavaVastaanottaja);

        RaportoitavaVastaanottaja haettuRaportoitavaVastaanottaja = 
        	raportoitavaVastaanottajaDAO.findByLahetettyviestiIdAndVastaanottajanSahkopostiosoite(
        	tallennettuRaportoitavaViesti.getId(), raportoitavaVastaanottaja.getVastaanottajanSahkoposti());

        assertNotNull(haettuRaportoitavaVastaanottaja);
    }
    
    @Test
    public void testLahettamattomienHakuOnnistuu() {
        RaportoitavaViesti raportoitavaViesti = RaportointipalveluTestData.getRaportoitavaViestiLahetyksenAloitusDTOTiedosta();
        RaportoitavaViesti tallennettuRaportoitavaViesti = raportoitavaViestiDAO.insert(raportoitavaViesti);

        RaportoitavaVastaanottaja raportoitavaVastaanottaja = 
        	RaportointipalveluTestData.getRaportoitavaVastaanottaja(tallennettuRaportoitavaViesti);
        raportoitavaVastaanottaja.setLahetysalkoi(null);
        raportoitavaVastaanottaja.setLahetyspaattyi(null);
        raportoitavaVastaanottajaDAO.insert(raportoitavaVastaanottaja);

        List<RaportoitavaVastaanottaja> vastaanottajat = 
        	raportoitavaVastaanottajaDAO.findLahettamattomat();
        
        assertNotNull(vastaanottajat);
        assertTrue(vastaanottajat.size() > 0);
    }
}
