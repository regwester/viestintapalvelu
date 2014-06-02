package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReportedMessageConverter.class)
@ContextConfiguration("/test-bundle-context.xml")
public class ReportedMessageConverterTest {
    private ReportedMessageConverter reportedMessageConverter;
    
    @Mock
    CurrentUserComponent mockedCurrentUserComponent;
    
    @Before
    public void setup() {
        this.reportedMessageConverter = new ReportedMessageConverter(mockedCurrentUserComponent);
    }

	@Test
	public void testReportedMessageConversion() throws IOException {
		EmailMessage emailMessage = RaportointipalveluTestData.getEmailMessage();
        Henkilo henkilo = RaportointipalveluTestData.getHenkilo();
        henkilo.setOidHenkilo(emailMessage.getSenderOid());
        
        when(mockedCurrentUserComponent.getCurrentUser()).thenReturn(henkilo);
        
        ReportedMessage reportedMessage = reportedMessageConverter.convert(emailMessage, null);

		assertNotNull(reportedMessage);
		assertEquals(emailMessage.getSenderOid(), reportedMessage.getSenderOid()); 
	}
}
