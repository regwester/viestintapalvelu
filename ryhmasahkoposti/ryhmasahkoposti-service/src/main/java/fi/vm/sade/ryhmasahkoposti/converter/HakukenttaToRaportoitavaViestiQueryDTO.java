package fi.vm.sade.ryhmasahkoposti.converter;

import org.springframework.stereotype.Component;

import fi.vm.sade.generic.common.HetuUtils;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.RaportoitavaVastaanottajaQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.RaportoitavaViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.validation.OidValidator;
import fi.vm.sade.ryhmasahkoposti.validation.SahkopostiValidator;

@Component
public class HakukenttaToRaportoitavaViestiQueryDTO {

	public static RaportoitavaViestiQueryDTO convert(String hakuKentta) {
		RaportoitavaViestiQueryDTO raportoitavaViestiQuery = new RaportoitavaViestiQueryDTO();
		RaportoitavaVastaanottajaQueryDTO raportoitavaVastaanottajaQuery = new RaportoitavaVastaanottajaQueryDTO();
				
		if (HetuUtils.isHetuValid(hakuKentta)) {
			raportoitavaVastaanottajaQuery.setVastaanottajanHenkilotunnus(hakuKentta.trim());
			raportoitavaViestiQuery.setVastaanottajaQuery(raportoitavaVastaanottajaQuery);
			
			return raportoitavaViestiQuery;
		}
		
		if (OidValidator.isOID(hakuKentta)) {
			raportoitavaVastaanottajaQuery.setVastaanottajanOid(hakuKentta.trim());
			raportoitavaViestiQuery.setVastaanottajaQuery(raportoitavaVastaanottajaQuery);
			
			return raportoitavaViestiQuery;
		}
		
		if (SahkopostiValidator.validate(hakuKentta)) {
			raportoitavaVastaanottajaQuery.setVastaanottajanSahkopostiosoite(hakuKentta.trim());
			raportoitavaViestiQuery.setVastaanottajaQuery(raportoitavaVastaanottajaQuery);
			
			return raportoitavaViestiQuery;			
		}
		
		raportoitavaVastaanottajaQuery.setVastaanottajanNimi(hakuKentta);
		raportoitavaViestiQuery.setVastaanottajaQuery(raportoitavaVastaanottajaQuery);
		raportoitavaViestiQuery.setSanaHaku(hakuKentta);		
		
		return raportoitavaViestiQuery;
	}
}
