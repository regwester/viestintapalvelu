package fi.vm.sade.ryhmasahkoposti.converter;

import org.springframework.stereotype.Component;

import fi.vm.sade.generic.common.HetuUtils;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailRecipientQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.validation.OidValidator;
import fi.vm.sade.ryhmasahkoposti.validation.SahkopostiValidator;

@Component
public class EmailMessageQueryDTOConverter {

	public static EmailMessageQueryDTO convert(String hakuKentta) {
		EmailMessageQueryDTO emailMessageQueryDTO = new EmailMessageQueryDTO();
		EmailRecipientQueryDTO emailRecipientQueryDTO = new EmailRecipientQueryDTO();
				
		if (HetuUtils.isHetuValid(hakuKentta)) {
			emailRecipientQueryDTO.setRecipientSocialSecurityID(hakuKentta.trim());
			emailMessageQueryDTO.setEmailRecipientQueryDTO(emailRecipientQueryDTO);
			
			return emailMessageQueryDTO;
		}
		
		if (OidValidator.isOID(hakuKentta)) {
			emailRecipientQueryDTO.setRecipientOid(hakuKentta.trim());
			emailMessageQueryDTO.setEmailRecipientQueryDTO(emailRecipientQueryDTO);
			
			return emailMessageQueryDTO;
		}
		
		if (SahkopostiValidator.validate(hakuKentta)) {
			emailRecipientQueryDTO.setRecipientEmail(hakuKentta.trim());
			emailMessageQueryDTO.setEmailRecipientQueryDTO(emailRecipientQueryDTO);
			
			return emailMessageQueryDTO;			
		}
		
		emailRecipientQueryDTO.setRecipientName(hakuKentta);
		emailMessageQueryDTO.setEmailRecipientQueryDTO(emailRecipientQueryDTO);
		emailMessageQueryDTO.setSearchArgument(hakuKentta);		
		
		return emailMessageQueryDTO;
	}
}
