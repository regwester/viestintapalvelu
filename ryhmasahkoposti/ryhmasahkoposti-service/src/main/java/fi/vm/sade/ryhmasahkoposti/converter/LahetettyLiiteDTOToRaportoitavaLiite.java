package fi.vm.sade.ryhmasahkoposti.converter;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyLiiteDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;

@Component
public class LahetettyLiiteDTOToRaportoitavaLiite {

	public static RaportoitavaLiite convert(LahetettyLiiteDTO lahetettyLiite) {
		RaportoitavaLiite raportoitavaLiite = new RaportoitavaLiite();
		
		return raportoitavaLiite;
	}
}
