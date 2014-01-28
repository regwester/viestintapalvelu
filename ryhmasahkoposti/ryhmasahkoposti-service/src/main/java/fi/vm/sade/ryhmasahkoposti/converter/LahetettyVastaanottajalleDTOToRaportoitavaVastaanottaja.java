package fi.vm.sade.ryhmasahkoposti.converter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.constants.RyhmasahkopostiConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;

@Component
public class LahetettyVastaanottajalleDTOToRaportoitavaVastaanottaja {

	public static RaportoitavaVastaanottaja convert(LahetettyVastaanottajalleDTO lahetettyVastaanottajalle) {
		RaportoitavaVastaanottaja vastaanottaja = new RaportoitavaVastaanottaja();
		
		vastaanottaja.setVastaanottajaOid(lahetettyVastaanottajalle.getVastaanottajaOid());
		vastaanottaja.setVastaanottajaOidTyyppi(lahetettyVastaanottajalle.getVastaanottajanOidTyyppi());
		vastaanottaja.setHenkilotunnus("");
		vastaanottaja.setVastaanottajanSahkoposti(lahetettyVastaanottajalle.getVastaanottajanSahkoposti());
		vastaanottaja.setKielikoodi(lahetettyVastaanottajalle.getKielikoodi());
		vastaanottaja.setHakuNimi("");
		vastaanottaja.setLahetysalkoi(lahetettyVastaanottajalle.getLahetysalkoi());
		vastaanottaja.setLahetyspaattyi(lahetettyVastaanottajalle.getLahetyspaattyi());
		vastaanottaja.setLahetysOnnistui(onnistuikoLahetys(lahetettyVastaanottajalle));
		vastaanottaja.setEpaonnistumisenSyy(lahetettyVastaanottajalle.getEpaonnistumisenSyy());
		vastaanottaja.setAikaleima(new Date());
		
		return vastaanottaja;
	}

	public static Set<RaportoitavaVastaanottaja> convert(RaportoitavaViesti raportoitavaViesti, 
		List<LahetettyVastaanottajalleDTO> lahetettyVastaanottajille) {
		Set<RaportoitavaVastaanottaja> raportoitavatVastaanottajat = new HashSet<RaportoitavaVastaanottaja>();
		
		for (LahetettyVastaanottajalleDTO lahetettyVastaanottajalle : lahetettyVastaanottajille) {
			RaportoitavaVastaanottaja vastaanottaja = convert(lahetettyVastaanottajalle);
			vastaanottaja.setRaportoitavaViesti(raportoitavaViesti);
			raportoitavatVastaanottajat.add(vastaanottaja);
		}
		
		return raportoitavatVastaanottajat;
	}

	private static String onnistuikoLahetys(LahetettyVastaanottajalleDTO lahetettyVastaanottajalle) {
		if (lahetettyVastaanottajalle.getLahetyspaattyi() == null) {
			return null;
		}
		
		String lahetysOnnistui = RyhmasahkopostiConstants.LAHETYS_EPAONNISTUI;
		
		if (lahetettyVastaanottajalle.getEpaonnistumisenSyy() == null || 
			lahetettyVastaanottajalle.getEpaonnistumisenSyy().isEmpty()) {
			lahetysOnnistui = RyhmasahkopostiConstants.LAHETYS_ONNISTUI;
		}
		
		return lahetysOnnistui;
	}
}
