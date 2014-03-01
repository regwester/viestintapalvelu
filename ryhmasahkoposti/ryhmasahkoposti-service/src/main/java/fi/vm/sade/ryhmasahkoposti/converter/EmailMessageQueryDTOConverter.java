package fi.vm.sade.ryhmasahkoposti.converter;

import org.springframework.stereotype.Component;

import fi.vm.sade.generic.common.HetuUtils;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailRecipientQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.validation.OidValidator;
import fi.vm.sade.ryhmasahkoposti.validation.EmailAddressValidator;

@Component
public class EmailMessageQueryDTOConverter {

	public static EmailMessageQueryDTO convert(String searchArgument) {
		EmailMessageQueryDTO emailMessageQueryDTO = new EmailMessageQueryDTO();
		EmailRecipientQueryDTO emailRecipientQueryDTO = new EmailRecipientQueryDTO();
				
		if (HetuUtils.isHetuValid(searchArgument)) {
			emailRecipientQueryDTO.setRecipientSocialSecurityID(searchArgument.trim());
			emailMessageQueryDTO.setEmailRecipientQueryDTO(emailRecipientQueryDTO);
			
			return emailMessageQueryDTO;
		}
		
		if (OidValidator.isOID(searchArgument)) {
			emailRecipientQueryDTO.setRecipientOid(searchArgument.trim());
			emailMessageQueryDTO.setEmailRecipientQueryDTO(emailRecipientQueryDTO);
			
			return emailMessageQueryDTO;
		}
		
		if (EmailAddressValidator.validate(searchArgument)) {
			emailRecipientQueryDTO.setRecipientEmail(searchArgument.trim());
			emailMessageQueryDTO.setEmailRecipientQueryDTO(emailRecipientQueryDTO);
			
			return emailMessageQueryDTO;			
		}
		
		emailRecipientQueryDTO.setRecipientName(searchArgument);
		emailMessageQueryDTO.setEmailRecipientQueryDTO(emailRecipientQueryDTO);
		emailMessageQueryDTO.setSearchArgument(searchArgument);		
		
		return emailMessageQueryDTO;
	}
}
