package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaVastaanottajaDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.RaportoitavaViestiDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavanViestinLiite;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaLiiteService;

@Component
public class RaportoitavaVastaanottajaToRaportoitavaVastaanottajaDTO {
	private static RaportoitavaLiiteService raportoitavaLiiteService;

	@Autowired
	public RaportoitavaVastaanottajaToRaportoitavaVastaanottajaDTO(RaportoitavaLiiteService raportoitavaLiiteService) {
		RaportoitavaVastaanottajaToRaportoitavaVastaanottajaDTO.raportoitavaLiiteService = raportoitavaLiiteService;
	}
	
	public static RaportoitavaVastaanottajaDTO convert(RaportoitavaVastaanottaja vastaanottaja) {
		RaportoitavaVastaanottajaDTO vastaanottajaDTO = new RaportoitavaVastaanottajaDTO();
		
		vastaanottajaDTO.setVastaanottajaID(vastaanottaja.getId());
		vastaanottajaDTO.setEtunimi("");
		vastaanottajaDTO.setSukunimi("");
		vastaanottajaDTO.setOrganisaationNimi("");
		vastaanottajaDTO.setVastaanottajanOid(vastaanottaja.getVastaanottajaOid());
		vastaanottajaDTO.setVastaanottajanSahkopostiosoite(vastaanottaja.getVastaanottajanSahkoposti());

		Set<RaportoitavanViestinLiite> viestinLiitteet = 
			vastaanottaja.getRaportoitavaviesti().getRaportoitavanViestinLiitteet();
		List<RaportoitavaLiite> raportoitavatLiitteet = 
			raportoitavaLiiteService.haeRaportoitavanViestinLiitteet(viestinLiitteet);
		
		RaportoitavaViestiDTO viestiDTO = RaportoitavaViestiToRaportoitavaViestiDTO.convert(
			vastaanottaja.getRaportoitavaviesti(), raportoitavatLiitteet, false);
		
		vastaanottajaDTO.setRaportoitavaViesti(viestiDTO);
		return vastaanottajaDTO;
	}

	public static List<RaportoitavaVastaanottajaDTO> convert(List<RaportoitavaVastaanottaja> vastaanottajat) {
		List<RaportoitavaVastaanottajaDTO> vastaanottajatDTO = new ArrayList<RaportoitavaVastaanottajaDTO>();
		
		for (RaportoitavaVastaanottaja vastaanottaja : vastaanottajat) {
			vastaanottajatDTO.add(convert(vastaanottaja));
			
		}
		
		return vastaanottajatDTO;
	}
}
