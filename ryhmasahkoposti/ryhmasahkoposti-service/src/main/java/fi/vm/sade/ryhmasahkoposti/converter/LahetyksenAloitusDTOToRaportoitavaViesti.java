package fi.vm.sade.ryhmasahkoposti.converter;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.constants.RyhmasahkopostiConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;

@Component
public class LahetyksenAloitusDTOToRaportoitavaViesti {
	public static RaportoitavaViesti convert(LahetyksenAloitusDTO lahetyksenAloitus) throws IOException {
		RaportoitavaViesti raportoitavaViesti = new RaportoitavaViesti();
		
		raportoitavaViesti.setAihe(lahetyksenAloitus.getAihe());
		raportoitavaViesti.setProsessi(lahetyksenAloitus.getProsessi());
		raportoitavaViesti.setLahettajanOid(lahetyksenAloitus.getLahettajanOid());
		raportoitavaViesti.setLahettajanOidTyyppi(lahetyksenAloitus.getLahettajanOidTyyppi());
		raportoitavaViesti.setLahettajanSahkopostiosoite(lahetyksenAloitus.getLahettajanSahkopostiosoite());
		raportoitavaViesti.setVastauksensaajanOid(lahetyksenAloitus.getVastauksensaajaOid());
		raportoitavaViesti.setVastauksensaajanOidTyyppi(lahetyksenAloitus.getVastauksenSaajanOidTyyppi());
		raportoitavaViesti.setVastauksensaajanSahkopostiosoite(lahetyksenAloitus.getVastauksensaajanSahkoposti());
		raportoitavaViesti.setAihe(lahetyksenAloitus.getAihe());
		raportoitavaViesti.setViesti(lahetyksenAloitus.getViesti());
		if (lahetyksenAloitus.isHtmlViesti()) {
			raportoitavaViesti.setHtmlViesti(RyhmasahkopostiConstants.HTML_VIESTI);
		} else {
			raportoitavaViesti.setHtmlViesti(RyhmasahkopostiConstants.EI_HTML_VIESTI);
		}
		raportoitavaViesti.setMerkisto(lahetyksenAloitus.getMerkisto());
		raportoitavaViesti.setLahetysAlkoi(lahetyksenAloitus.getLahetysAlkoi());
		raportoitavaViesti.setAikaleima(new Date());
		
		return raportoitavaViesti;
	}
}
