package fi.vm.sade.ryhmasahkoposti.converter;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.constants.RyhmasahkopostiConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;

@Component
public class RaportoitavaViestiConverter {
	public static RaportoitavaViesti convert(EmailMessage emailMessage) throws IOException {
		RaportoitavaViesti raportoitavaViesti = new RaportoitavaViesti();
		
		raportoitavaViesti.setAihe(emailMessage.getSubject());
		raportoitavaViesti.setProsessi(emailMessage.getCallingProcess());
		raportoitavaViesti.setLahettajanOid(emailMessage.getSenderOid());
		raportoitavaViesti.setLahettajanOidTyyppi(emailMessage.getSenderOidType());
		raportoitavaViesti.setLahettajanSahkopostiosoite(emailMessage.getSenderEmail());
		raportoitavaViesti.setVastauksensaajanOid("");
		raportoitavaViesti.setVastauksensaajanOidTyyppi("");
		raportoitavaViesti.setVastauksensaajanSahkopostiosoite(emailMessage.getOwnerEmail());
		raportoitavaViesti.setAihe(emailMessage.getSubject());
		raportoitavaViesti.setViesti(emailMessage.getBody());
		if (emailMessage.isHtml()) {
			raportoitavaViesti.setHtmlViesti(RyhmasahkopostiConstants.HTML_VIESTI);
		} else {
			raportoitavaViesti.setHtmlViesti(RyhmasahkopostiConstants.EI_HTML_VIESTI);
		}
		raportoitavaViesti.setMerkisto(emailMessage.getCharset());
		raportoitavaViesti.setLahetysAlkoi(new Date());
		raportoitavaViesti.setAikaleima(new Date());
		
		return raportoitavaViesti;
	}
}
