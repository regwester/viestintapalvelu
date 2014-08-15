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
import fi.vm.sade.ryhmasahkoposti.api.dto.ReplacementDTO;
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

        ReportedMessage reportedMessage = reportedMessageConverter.convert(emailMessage, null, null, null, null, null, null);

        assertNotNull(reportedMessage);
        assertEquals(emailMessage.getSenderOid(), reportedMessage.getSenderOid()); 
    }


    @Test
    public void testReportedMessageConversionTemplate() throws IOException {
        EmailMessage emailMessage = RaportointipalveluTestData.getEmailMessage();
        Henkilo henkilo = RaportointipalveluTestData.getHenkilo();
        henkilo.setOidHenkilo(emailMessage.getSenderOid());

        when(mockedCurrentUserComponent.getCurrentUser()).thenReturn(henkilo);

        ReplacementDTO templateSubject = new ReplacementDTO();
        templateSubject.setName(ReplacementDTO.NAME_EMAIL_SUBJECT);
        templateSubject.setDefaultValue("subject-template");

        ReplacementDTO templateSenderFromAddress = new ReplacementDTO();
        templateSenderFromAddress.setName(ReplacementDTO.NAME_EMAIL_SENDER_FROM);
        templateSenderFromAddress.setDefaultValue("sender@test.com");

        ReplacementDTO templateSenderFromPersonal = new ReplacementDTO();
        templateSenderFromPersonal.setName(ReplacementDTO.NAME_EMAIL_SENDER_FROM_PERSONAL);
        templateSenderFromPersonal.setDefaultValue("Test-Sender");

        ReplacementDTO templateReplyToAddress = new ReplacementDTO();
        templateReplyToAddress.setName(ReplacementDTO.NAME_EMAIL_REPLY_TO);
        templateReplyToAddress.setDefaultValue("replyto@test.com");

        ReplacementDTO templateReplyToPersonal = new ReplacementDTO();
        templateReplyToPersonal.setName(ReplacementDTO.NAME_EMAIL_REPLY_TO_PERSONAL);
        templateReplyToPersonal.setDefaultValue("Test-reply-to");

        String templateContent = "test template";

        ReportedMessage reportedMessage = reportedMessageConverter.convert(emailMessage, templateSenderFromAddress, templateSenderFromPersonal, 
                templateReplyToAddress, templateReplyToPersonal, templateSubject, templateContent);

        assertNotNull(reportedMessage);

        assertEquals(emailMessage.getSenderOid(), reportedMessage.getSenderOid()); 
        assertEquals(reportedMessage.getSenderEmail(), templateSenderFromPersonal.getDefaultValue() + templateSenderFromAddress.getDefaultValue());
        assertEquals(reportedMessage.getReplyToEmail(), templateReplyToPersonal.getDefaultValue() + templateReplyToAddress.getDefaultValue());
        assertEquals(reportedMessage.getSubject(), templateSubject.getDefaultValue());
        
    }
}
