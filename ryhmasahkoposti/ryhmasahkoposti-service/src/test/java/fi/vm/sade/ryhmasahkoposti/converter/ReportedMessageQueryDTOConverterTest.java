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
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.CurrentUserComponent;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReportedMessageQueryDTOConverterTest.class)
@ContextConfiguration("/test-bundle-context.xml")
public class ReportedMessageQueryDTOConverterTest {
    private ReportedMessageQueryDTOConverter reportedMessageQueryDTOConverter;
    @Mock
    CurrentUserComponent mockedCurrentUserComponent;
    
    @Before
    public void setup() {
        this.reportedMessageQueryDTOConverter = new ReportedMessageQueryDTOConverter(mockedCurrentUserComponent);
    }
    
	@Test
	public void testEmailInSearchArgument() {
	    String oid = "1.2.246.562.10.00000000001";
		String searchArgument = "testi.osoite@sposti.fi";
		
        ReportedMessageQueryDTO query = reportedMessageQueryDTOConverter.convert(oid, searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getReportedRecipientQueryDTO());
		assertEquals(searchArgument, query.getReportedRecipientQueryDTO().getRecipientEmail());
	}
	
	@Test
	public void testOidInSearchArgument() {
        String oid = "1.2.246.562.10.00000000001";
		String searchArgument = "1.2.246.562.24.42645159413";
		
        ReportedMessageQueryDTO query = reportedMessageQueryDTOConverter.convert(oid, searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getReportedRecipientQueryDTO());
		assertEquals(searchArgument, query.getReportedRecipientQueryDTO().getRecipientOid());
	}	

	@Test
	public void testSocialSecurityIdInSearchArgument() {
        String oid = "1.2.246.562.10.00000000001";
		String searchArgument = "100970-965W";
		
        ReportedMessageQueryDTO query = reportedMessageQueryDTOConverter.convert(oid, searchArgument);
		
		assertNotNull(query);
		assertNotNull(query.getReportedRecipientQueryDTO());
		assertEquals(searchArgument, query.getReportedRecipientQueryDTO().getRecipientSocialSecurityID());
	}	
}
