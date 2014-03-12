package fi.vm.sade.ryhmasahkoposti.converter;

import org.springframework.stereotype.Component;

import fi.vm.sade.generic.common.HetuUtils;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedRecipientQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.validation.OidValidator;
import fi.vm.sade.ryhmasahkoposti.validation.EmailAddressValidator;

@Component
public class ReportedMessageQueryDTOConverter {

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
}
