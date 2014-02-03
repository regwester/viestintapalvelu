package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;

@Component
public class RaportoitavaVastaanottajaConverter {

	public static RaportoitavaVastaanottaja convert(EmailRecipient emailRecipient) {
		RaportoitavaVastaanottaja vastaanottaja = new RaportoitavaVastaanottaja();
		
		vastaanottaja.setVastaanottajaOid(emailRecipient.getOid());
		vastaanottaja.setVastaanottajaOidTyyppi(emailRecipient.getOidType());
		vastaanottaja.setHenkilotunnus("");
		vastaanottaja.setVastaanottajanSahkoposti(emailRecipient.getEmail());
		vastaanottaja.setKielikoodi(emailRecipient.getLanguageCode());
		vastaanottaja.setHakuNimi("");
		vastaanottaja.setLahetysalkoi(null);
		vastaanottaja.setLahetyspaattyi(null);
		vastaanottaja.setLahetysOnnistui(null);
		vastaanottaja.setEpaonnistumisenSyy(null);
		vastaanottaja.setAikaleima(new Date());
		
		return vastaanottaja;
	}

	public static Set<RaportoitavaVastaanottaja> convert(RaportoitavaViesti raportoitavaViesti, 
		List<EmailRecipient> emailRecipients) {
		Set<RaportoitavaVastaanottaja> raportoitavatVastaanottajat = new HashSet<RaportoitavaVastaanottaja>();
		
		for (EmailRecipient emailRecipient : emailRecipients) {
			RaportoitavaVastaanottaja vastaanottaja = convert(emailRecipient);
			vastaanottaja.setRaportoitavaViesti(raportoitavaViesti);
			raportoitavatVastaanottajat.add(vastaanottaja);
		}
		
		return raportoitavatVastaanottajat;
	}
}
