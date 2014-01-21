package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaVastaanottajaDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;

@Component
public class RaportoitavaViestiToRaportoitavaViestiDTO {

	public static List<RaportoitavaViestiDTO> convert(List<RaportoitavaViesti> raportoitavatViestit) {
		List<RaportoitavaViestiDTO> raportoitavatViestitDTO = new ArrayList<RaportoitavaViestiDTO>();
		
		for (RaportoitavaViesti raportoitavaViesti : raportoitavatViestit) {
			RaportoitavaViestiDTO raportoitavaViestiDTO = new RaportoitavaViestiDTO();
			
			raportoitavaViestiDTO.setAihe(raportoitavaViesti.getAihe());
			raportoitavaViestiDTO.setLahettajanSahkopostiosoite(raportoitavaViesti.getLahettajanSahkopostiosoite());
			raportoitavaViestiDTO.setLahetysAlkoi(raportoitavaViesti.getLahetysAlkoi());
			raportoitavaViestiDTO.setLahetysPaattyi(raportoitavaViesti.getLahetysPaattyi());
			raportoitavaViestiDTO.setLahetysraportti(mauodostaLahetysraportti(raportoitavaViesti));
			raportoitavaViestiDTO.setLahetystunnus(raportoitavaViesti.getId());
			raportoitavaViestiDTO.setLiitetiedostot(muodostaLiitetiedostot(raportoitavaViesti));
			raportoitavaViestiDTO.setProsessi(raportoitavaViesti.getProsessi());
			raportoitavaViestiDTO.setVastaanottajat(muodostaVastaanottajat(raportoitavaViesti));
			raportoitavaViestiDTO.setVastauksenSaajanSahkopostiosoite(
				raportoitavaViesti.getVastauksensaajanSahkopostiosoite());
			raportoitavaViestiDTO.setViestinSisalto(raportoitavaViesti.getViesti());
			
			raportoitavatViestitDTO.add(raportoitavaViestiDTO);
		}
		
		return raportoitavatViestitDTO;
	}

	private static String mauodostaLahetysraportti(RaportoitavaViesti raportoitavaViesti) {
		return null;
	}

	private static List<String> muodostaLiitetiedostot(RaportoitavaViesti raportoitavaViesti) {
		// TODO Auto-generated method stub
		return null;
	}

	private static List<RaportoitavaVastaanottajaDTO> muodostaVastaanottajat(RaportoitavaViesti raportoitavaViesti) {
		// TODO Auto-generated method stub
		return null;
	}
}
