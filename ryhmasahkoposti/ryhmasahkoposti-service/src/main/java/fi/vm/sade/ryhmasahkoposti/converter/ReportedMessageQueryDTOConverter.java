package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.generic.common.HetuUtils;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedRecipientQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.route.HenkiloRoute;
import fi.vm.sade.ryhmasahkoposti.validation.OidValidator;
import fi.vm.sade.ryhmasahkoposti.validation.EmailAddressValidator;

@Component
public class ReportedMessageQueryDTOConverter {
    private static HenkiloRoute henkiloRoute;
    
    @Autowired
    public ReportedMessageQueryDTOConverter(HenkiloRoute henkiloRoute) {
        ReportedMessageQueryDTOConverter.henkiloRoute = henkiloRoute;
    }
    
    public static ReportedMessageQueryDTO convert() {
        ReportedMessageQueryDTO reportedMessageQueryDTO = new ReportedMessageQueryDTO();
        
        return reportedMessageQueryDTO;
    }
    
	public static ReportedMessageQueryDTO convert(String searchArgument) {
		ReportedMessageQueryDTO reportedMessageQueryDTO = new ReportedMessageQueryDTO();
		ReportedRecipientQueryDTO reportedRecipientQueryDTO = new ReportedRecipientQueryDTO();
				
		if (HetuUtils.isHetuValid(searchArgument)) {
			reportedRecipientQueryDTO.setRecipientSocialSecurityID(searchArgument.trim());
			reportedMessageQueryDTO.setReportedRecipientQueryDTO(reportedRecipientQueryDTO);
			
			return reportedMessageQueryDTO;
		}
		
		if (OidValidator.isOID(searchArgument)) {
			reportedRecipientQueryDTO.setRecipientOid(searchArgument.trim());
			reportedMessageQueryDTO.setReportedRecipientQueryDTO(reportedRecipientQueryDTO);
			
			return reportedMessageQueryDTO;
		}
		
		if (EmailAddressValidator.validate(searchArgument)) {
			reportedRecipientQueryDTO.setRecipientEmail(searchArgument.trim());
			reportedMessageQueryDTO.setReportedRecipientQueryDTO(reportedRecipientQueryDTO);
			
			return reportedMessageQueryDTO;			
		}
		
		reportedRecipientQueryDTO.setRecipientName(searchArgument);
		reportedMessageQueryDTO.setReportedRecipientQueryDTO(reportedRecipientQueryDTO);
		reportedMessageQueryDTO.setSearchArgument(searchArgument);		
		
		return reportedMessageQueryDTO;
	}
	
	private static List<String> getSenderOidList() {
	    List<String> senderOidList = new ArrayList<String>();
	    
	    Henkilo henkilo = henkiloRoute.getCurrenUser();
	    senderOidList.add(henkilo.getOidHenkilo());
	    
	    return senderOidList;
	}
}
