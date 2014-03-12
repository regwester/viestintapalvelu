package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.externalinterface.route.HenkiloRoute;
import fi.vm.sade.ryhmasahkoposti.externalinterface.route.OrganisaatioRoute;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReportedRecipientConverter.class)
@ContextConfiguration("/test-bundle-context.xml")
public class ReportedRecipientConverterTest {
    @Mock
    HenkiloRoute henkiloRoute;
    @Mock
    OrganisaatioRoute organisaatioRoute;

    @Test
	public void testReportedRecipientIsPerson() {
        Henkilo henkilo = RaportointipalveluTestData.getHenkilo();
	    when(henkiloRoute.getHenkilo(any(String.class))).thenReturn(henkilo);
	    
	    OrganisaatioRDTO organisaatio = RaportointipalveluTestData.getOrganisaatioRDTO();
	    when(organisaatioRoute.getOrganisaatio(any(String.class))).thenReturn(organisaatio);	    
	    
		EmailRecipient emailRecipient = RaportointipalveluTestData.getEmailRecipient();
		emailRecipient.setEmail("testMuodostaRaportoitavaVastaanottaja@sposti.fi");

		@SuppressWarnings("static-access")
        ReportedRecipient reportedRecipient = 
		    new ReportedRecipientConverter(henkiloRoute, organisaatioRoute).convert(emailRecipient);
		
		assertNotNull(reportedRecipient);
		assertNotNull(reportedRecipient.getRecipientOid());
		assertNotNull(reportedRecipient.getSearchName());
		assertTrue(reportedRecipient.getSearchName().equals(henkilo.getSukunimi() + "," + henkilo.getEtunimet()));
		assertNotNull(reportedRecipient.getRecipientEmail());
		assertTrue(reportedRecipient.getSendingStarted() == null);
		assertTrue(reportedRecipient.getSendingEnded() == null);
		assertTrue(reportedRecipient.getFailureReason() == null);
		assertTrue(reportedRecipient.getSendingSuccesful() == null);
	}
}
