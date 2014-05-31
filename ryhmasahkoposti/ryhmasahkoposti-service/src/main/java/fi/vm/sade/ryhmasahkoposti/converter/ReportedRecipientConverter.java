package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.OrganizationComponent;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.PersonComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.validation.OidValidator;

@Component
public class ReportedRecipientConverter {
    private PersonComponent personComponent;
    private OrganizationComponent organizationComponent;
    
    @Autowired
    public ReportedRecipientConverter(PersonComponent personComponent, OrganizationComponent organizationComponent) {
        this.personComponent = personComponent;
        this.organizationComponent = organizationComponent;
    }

	public ReportedRecipient convert(EmailRecipient emailRecipient) {
		ReportedRecipient reportedRecipient = new ReportedRecipient();
		
		reportedRecipient.setRecipientOid(emailRecipient.getOid());
		reportedRecipient.setRecipientOidType(emailRecipient.getOidType());
		reportedRecipient.setSocialSecurityID("");
		reportedRecipient.setRecipientEmail(emailRecipient.getEmail());
		reportedRecipient.setLanguageCode(emailRecipient.getLanguageCode());
		reportedRecipient.setSearchName("");
		reportedRecipient.setSendingStarted(null);
		reportedRecipient.setSendingEnded(null);
		reportedRecipient.setSendingSuccesful(null);
		reportedRecipient.setFailureReason(null);
		reportedRecipient.setTimestamp(new Date());
		
		setDataFromExternalInterfaces(reportedRecipient);
		
		return reportedRecipient;
	}

	public Set<ReportedRecipient> convert(ReportedMessage reportedMessage, 
		List<EmailRecipient> emailRecipients) {
		Set<ReportedRecipient> reportedRecipients = new HashSet<ReportedRecipient>();
		
		for (EmailRecipient emailRecipient : emailRecipients) {
			ReportedRecipient reportedRecipient = convert(emailRecipient);
			reportedRecipient.setReportedMessage(reportedMessage);
			reportedRecipients.add(reportedRecipient);
		}
		
		return reportedRecipients;
	}
	
	public ReportedRecipient convert(ReportedMessage reportedMessage, 
		EmailRecipient emailRecipient) {
	    ReportedRecipient reportedRecipient = convert(emailRecipient);
	    reportedRecipient.setReportedMessage(reportedMessage);
	    return reportedRecipient;
	}
	
	private void setDataFromExternalInterfaces(ReportedRecipient reportedRecipient) {
        reportedRecipient.setSearchName("");
        reportedRecipient.setSocialSecurityID("");
        
        if (reportedRecipient.getRecipientOid() == null || reportedRecipient.getRecipientOid().isEmpty()) {
            return;
        }
	    
        if (OidValidator.isHenkiloOID(reportedRecipient.getRecipientOid())) {
            Henkilo henkilo = personComponent.getPerson(reportedRecipient.getRecipientOid());
            reportedRecipient.setSearchName(henkilo.getSukunimi() + "," + henkilo.getEtunimet());
            reportedRecipient.setSocialSecurityID(henkilo.getHetu());
            
            return;
        } 
        
        if (OidValidator.isOrganisaatioOID(reportedRecipient.getRecipientOid())) {
            OrganisaatioRDTO organisaatio = organizationComponent.getOrganization(reportedRecipient.getRecipientOid());
            String nameOfOrganisation = organizationComponent.getNameOfOrganisation(organisaatio);
            reportedRecipient.setSearchName(nameOfOrganisation); 
        }
	}
}
