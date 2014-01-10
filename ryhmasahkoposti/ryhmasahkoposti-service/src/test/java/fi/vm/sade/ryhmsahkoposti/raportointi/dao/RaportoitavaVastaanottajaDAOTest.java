package fi.vm.sade.ryhmsahkoposti.raportointi.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.ryhmasahkoposti.api.dto.query.RyhmasahkopostiVastaanottajaQueryDTO;
import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavaVastaanottajaDAO;
import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavaViestiDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmsahkoposti.raportointi.testdata.RaportointipalveluTestData;

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

    @DirtiesContext
    @Test
    public void testHaeVastaanottajayhdellaHakutekijalla() {
        RaportoitavaViesti raportoitavaViesti = RaportointipalveluTestData.getRaportoitavaViesti();
        RaportoitavaViesti tallennettuRaportoitavaViesti = raportoitavaViestiDAO.insert(raportoitavaViesti);

        RaportoitavaVastaanottaja raportoitavaVastaanottaja = RaportointipalveluTestData
                .getRaportoitavaVastaanottaja(tallennettuRaportoitavaViesti);
        raportoitavaVastaanottajaDAO.insert(raportoitavaVastaanottaja);

        RyhmasahkopostiVastaanottajaQueryDTO query = RaportointipalveluTestData.getRaportoitavaVastaanottajaQuery();
        List<RaportoitavaVastaanottaja> haetutRaportoitavatVastaanottajat = raportoitavaVastaanottajaDAO
                .findBySearchCriterias(query);

        assertNotNull(haetutRaportoitavatVastaanottajat);
        org.junit.Assert.assertTrue(1 <= haetutRaportoitavatVastaanottajat.size());
    }

    @Test
    public void testHaeVastaanottajaUsellaHakutekijalla() {
        RaportoitavaViesti raportoitavaViesti = RaportointipalveluTestData.getRaportoitavaViesti();
        RaportoitavaViesti tallennettuRaportoitavaViesti = raportoitavaViestiDAO.insert(raportoitavaViesti);

        RaportoitavaVastaanottaja raportoitavaVastaanottaja = RaportointipalveluTestData
                .getRaportoitavaVastaanottaja(tallennettuRaportoitavaViesti);
        Date lahetysajankohta = new Date();
        raportoitavaVastaanottaja.setLahetyspaattyi(lahetysajankohta);
        raportoitavaVastaanottajaDAO.insert(raportoitavaVastaanottaja);

        RyhmasahkopostiVastaanottajaQueryDTO query = RaportointipalveluTestData.getRaportoitavaVastaanottajaQuery();
        query.setLahetysajankohta(lahetysajankohta);
        List<RaportoitavaVastaanottaja> haetutRaportoitavatVastaanottajat = raportoitavaVastaanottajaDAO
                .findBySearchCriterias(query);

        assertNotNull(haetutRaportoitavatVastaanottajat);
        assertEquals(1, haetutRaportoitavatVastaanottajat.size());
    }

    @Test
    public void testVastaanottajiaEiLoydyUsellaHakutekijalla() {
        RaportoitavaViesti raportoitavaViesti = RaportointipalveluTestData.getRaportoitavaViesti();
        RaportoitavaViesti tallennettuRaportoitavaViesti = raportoitavaViestiDAO.insert(raportoitavaViesti);

        RaportoitavaVastaanottaja raportoitavaVastaanottaja = RaportointipalveluTestData
                .getRaportoitavaVastaanottaja(tallennettuRaportoitavaViesti);
        Date lahetysajankohta = new Date();
        raportoitavaVastaanottaja.setLahetyspaattyi(lahetysajankohta);
        raportoitavaVastaanottajaDAO.insert(raportoitavaVastaanottaja);

        RyhmasahkopostiVastaanottajaQueryDTO query = RaportointipalveluTestData.getRaportoitavaVastaanottajaQuery();
        query.setVastaanottajanSahkopostiosoite("vastaanottaja@sposti.fi");
        List<RaportoitavaVastaanottaja> haetutRaportoitavatVastaanottajat = raportoitavaVastaanottajaDAO
                .findBySearchCriterias(query);

        assertNotNull(haetutRaportoitavatVastaanottajat);
        assertEquals(0, haetutRaportoitavatVastaanottajat.size());
    }

    @Test
    public void testVastaanottajanHakuOnnistuuViestiIdJaSahkopostiosoitteella() {
        RaportoitavaViesti raportoitavaViesti = RaportointipalveluTestData.getRaportoitavaViesti();
        RaportoitavaViesti tallennettuRaportoitavaViesti = raportoitavaViestiDAO.insert(raportoitavaViesti);

        RaportoitavaVastaanottaja raportoitavaVastaanottaja = RaportointipalveluTestData
                .getRaportoitavaVastaanottaja(tallennettuRaportoitavaViesti);
        Date lahetysajankohta = new Date();
        raportoitavaVastaanottaja.setLahetyspaattyi(lahetysajankohta);
        raportoitavaVastaanottajaDAO.insert(raportoitavaVastaanottaja);

        RaportoitavaVastaanottaja haettuRaportoitavaVastaanottaja = raportoitavaVastaanottajaDAO
                .findByLahetettyviestiIdAndVastaanottajanSahkopostiosoite(tallennettuRaportoitavaViesti.getId(),
                        raportoitavaVastaanottaja.getVastaanottajanSahkoposti());

        assertNotNull(haettuRaportoitavaVastaanottaja);
    }
}
