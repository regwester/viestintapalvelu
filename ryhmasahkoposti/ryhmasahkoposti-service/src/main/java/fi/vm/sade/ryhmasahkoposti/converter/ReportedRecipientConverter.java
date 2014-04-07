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
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.GetOrganizationComponent;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.GetPersonComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.validation.OidValidator;

@Component
public class ReportedRecipientConverter {
    private GetPersonComponent getPersonComponent;
    private GetOrganizationComponent getOrganizationComponent;
    
    @Autowired
    public ReportedRecipientConverter(GetPersonComponent getPersonComponent, 
        GetOrganizationComponent getOrganizationComponent) {
        this.getPersonComponent = getPersonComponent;
        this.getOrganizationComponent = getOrganizationComponent;
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
	
	private void setDataFromExternalInterfaces(ReportedRecipient reportedRecipient) {
        reportedRecipient.setSearchName("");
        reportedRecipient.setSocialSecurityID("");
        
        if (reportedRecipient.getRecipientOid() == null || reportedRecipient.getRecipientOid().isEmpty()) {
            return;
        }
	    
        if (OidValidator.isHenkiloOID(reportedRecipient.getRecipientOid())) {
            Henkilo henkilo = getPersonComponent.getPerson(reportedRecipient.getRecipientOid());
            reportedRecipient.setSearchName(henkilo.getSukunimi() + "," + henkilo.getEtunimet());
            reportedRecipient.setSocialSecurityID(henkilo.getHetu());
            
            return;
        } 
        
        if (OidValidator.isOrganisaatioOID(reportedRecipient.getRecipientOid())) {
            OrganisaatioRDTO organisaatio = getOrganizationComponent.getOrganization(reportedRecipient.getRecipientOid());
            String nameOfOrganisation = getNameOfOrganisation(organisaatio);
            reportedRecipient.setSearchName(nameOfOrganisation); 
        }
	}
	
	private String getNameOfOrganisation(OrganisaatioRDTO organisaatio) {
	    String[] language = {"fi", "sv", "en"};
	    
	    for (int i = 0; language.length > i; i++) {
	        String nameOfOrganisation = organisaatio.getNimi().get(language[i]);
	        if (nameOfOrganisation != null && !nameOfOrganisation.isEmpty()) {
	            return nameOfOrganisation;
	        }
	    }
	    
	    return "";
	}
}
