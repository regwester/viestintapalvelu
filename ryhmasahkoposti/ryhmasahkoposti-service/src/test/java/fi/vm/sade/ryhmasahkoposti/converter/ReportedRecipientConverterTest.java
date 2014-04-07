package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.GetOrganizationComponent;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.GetPersonComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReportedRecipientConverter.class)
@ContextConfiguration("/test-bundle-context.xml")
public class ReportedRecipientConverterTest {
    private ReportedRecipientConverter reportedRecipientConverter;
    @Mock
    GetPersonComponent getPersonComponent;
    @Mock
    GetOrganizationComponent getOrganizationComponent;

    @Before
    public void setup() {
        this.reportedRecipientConverter = new ReportedRecipientConverter(getPersonComponent, getOrganizationComponent);
    }
    
    @Test
	public void testReportedRecipientIsPerson() {
        Henkilo henkilo = RaportointipalveluTestData.getHenkilo();
	    when(getPersonComponent.getPerson(any(String.class))).thenReturn(henkilo);
	    
	    OrganisaatioRDTO organisaatio = RaportointipalveluTestData.getOrganisaatioRDTO();
	    when(getOrganizationComponent.getOrganization(any(String.class))).thenReturn(organisaatio);	    
	    
		EmailRecipient emailRecipient = RaportointipalveluTestData.getEmailRecipient();
		emailRecipient.setEmail("testMuodostaRaportoitavaVastaanottaja@sposti.fi");

        ReportedRecipient reportedRecipient = reportedRecipientConverter.convert(emailRecipient);
		
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
