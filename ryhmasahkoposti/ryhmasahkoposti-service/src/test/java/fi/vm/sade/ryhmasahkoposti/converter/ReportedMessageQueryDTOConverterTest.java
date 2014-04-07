package fi.vm.sade.ryhmasahkoposti.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.GetCurrentUserComponent;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReportedMessageQueryDTOConverterTest.class)
@ContextConfiguration("/test-bundle-context.xml")
public class ReportedMessageQueryDTOConverterTest {
    private ReportedMessageQueryDTOConverter reportedMessageQueryDTOConverter;
    @Mock
    GetCurrentUserComponent mockedGetCurrentUserComponent;
    
    @Before
    public void setup() {
        this.reportedMessageQueryDTOConverter = new ReportedMessageQueryDTOConverter(mockedGetCurrentUserComponent);
    }
    
	@Test
	public void testEmailInSearchArgument() {
		String searchArgument = "testi.osoite@sposti.fi";
		
        ReportedMessageQueryDTO query = reportedMessageQueryDTOConverter.convert(searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getReportedRecipientQueryDTO());
		assertEquals(searchArgument, query.getReportedRecipientQueryDTO().getRecipientEmail());
	}
	
	@Test
	public void testOidInSearchArgument() {
		String searchArgument = "1.2.246.562.24.42645159413";
		
        ReportedMessageQueryDTO query = reportedMessageQueryDTOConverter.convert(searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getReportedRecipientQueryDTO());
		assertEquals(searchArgument, query.getReportedRecipientQueryDTO().getRecipientOid());
	}	

	@Test
	public void testSocialSecurityIdInSearchArgument() {
		String searchArgument = "100970-965W";
		
        ReportedMessageQueryDTO query = reportedMessageQueryDTOConverter.convert(searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getReportedRecipientQueryDTO());
		assertEquals(searchArgument, query.getReportedRecipientQueryDTO().getRecipientSocialSecurityID());
	}	
}
