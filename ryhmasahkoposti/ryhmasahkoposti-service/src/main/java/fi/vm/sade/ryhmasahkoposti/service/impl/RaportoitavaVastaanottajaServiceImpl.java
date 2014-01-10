package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.dao.RaportoitavaVastaanottajaDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.service.RaportoitavaVastaanottajaService;

@Service
public class RaportoitavaVastaanottajaServiceImpl implements RaportoitavaVastaanottajaService {
	private RaportoitavaVastaanottajaDAO raportoitavaVastaanottajaDAO;
	
	@Autowired
	public RaportoitavaVastaanottajaServiceImpl(RaportoitavaVastaanottajaDAO raportoitavaVastaanottajaDAO) {
		this.raportoitavaVastaanottajaDAO = raportoitavaVastaanottajaDAO;
	}
	
	@Override
	public List<RaportoitavaVastaanottaja> haeRaportoitavatVastaanottajat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RaportoitavaVastaanottaja haeRaportoitavaVastaanottaja(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RaportoitavaVastaanottaja haeRaportoitavaVastaanottaja(Long viestiID, String vastaanottajanSahkopostiosoite) {
		System.out.println("viestiID: " + viestiID + " vastaanottajanSahkopostiosoite: " + vastaanottajanSahkopostiosoite);
		return raportoitavaVastaanottajaDAO.findByLahetettyviestiIdAndVastaanottajanSahkopostiosoite(
			viestiID, vastaanottajanSahkopostiosoite);
	}
	
	@Override
	public Long haeRaportoitavienVastaanottajienLukumaara(Long viestiID) {
		return raportoitavaVastaanottajaDAO.findVastaanottajienLukumaaraByViestiID(viestiID);
	}

	@Override
	public Long haeRaportoitavienVastaanottajienLukumaara(Long viestiID, boolean lahetysOnnistui) {
		return raportoitavaVastaanottajaDAO.findVastaanottajienLukumaaraByViestiIdJaLahetysonnistui(
			viestiID, lahetysOnnistui);
	}

	@Override
	public List<RaportoitavaVastaanottaja> muodostaRaportoitavatVastaanottajat(
		RaportoitavaViesti raportoitavaViesti, List<LahetettyVastaanottajalleDTO> lahetettyVastaanottajille) {
		List<RaportoitavaVastaanottaja> raportoitavatVastaanottajat = new ArrayList<RaportoitavaVastaanottaja>();
		
		for (LahetettyVastaanottajalleDTO lahetettyVastaanottajalle : lahetettyVastaanottajille) {
			RaportoitavaVastaanottaja vastaanottaja = muodostaRaportoitavaVastaanottaja(lahetettyVastaanottajalle);
			vastaanottaja.setRaportoitavaViesti(raportoitavaViesti);
			raportoitavatVastaanottajat.add(vastaanottaja);
		}
		
		return raportoitavatVastaanottajat;
	}

	@Override
	public RaportoitavaVastaanottaja muodostaRaportoitavaVastaanottaja(
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle) {
		RaportoitavaVastaanottaja vastaanottaja = new RaportoitavaVastaanottaja();
		
		vastaanottaja.setVastaanottajaOid(lahetettyVastaanottajalle.getVastaanottajaOid());
		vastaanottaja.setVastaanottajaOidTyyppi(lahetettyVastaanottajalle.getVastaanottajanOidTyyppi());
		vastaanottaja.setVastaanottajanSahkoposti(lahetettyVastaanottajalle.getVastaanottajanSahkoposti());
		vastaanottaja.setLahetysalkoi(lahetettyVastaanottajalle.getLahetysalkoi());
		vastaanottaja.setLahetyspaattyi(lahetettyVastaanottajalle.getLahetyspaattyi());
		
		String lahetysOnnistui = "0";
		if (lahetettyVastaanottajalle.getEpaonnistumisenSyy() == null || 
			lahetettyVastaanottajalle.getEpaonnistumisenSyy().isEmpty()) {
			lahetysOnnistui = "1";
		}
		
		vastaanottaja.setLahetysOnnistui(lahetysOnnistui);
		vastaanottaja.setEpaonnistumisenSyy(lahetettyVastaanottajalle.getEpaonnistumisenSyy());
		
		return vastaanottaja;
	}

	@Override
	public void paivitaRaportoitavaVastaanottaja(RaportoitavaVastaanottaja raportoitavaVastaanottaja) {
		raportoitavaVastaanottajaDAO.update(raportoitavaVastaanottaja);
	}

	@Override
	public RaportoitavaVastaanottaja taydennaRaportoitavaaVastaanottajaa(
		RaportoitavaVastaanottaja raportoitavaVastaanottaja, LahetettyVastaanottajalleDTO lahetettyVastaanottajalle) {
		raportoitavaVastaanottaja.setLahetysalkoi(lahetettyVastaanottajalle.getLahetysalkoi());
		raportoitavaVastaanottaja.setLahetyspaattyi(lahetettyVastaanottajalle.getLahetyspaattyi());

		String lahetysOnnistui = "0";
		if (lahetettyVastaanottajalle.getEpaonnistumisenSyy() == null || 
			lahetettyVastaanottajalle.getEpaonnistumisenSyy().isEmpty()) {
			lahetysOnnistui = "1";
		}

		raportoitavaVastaanottaja.setLahetysOnnistui(lahetysOnnistui);
		raportoitavaVastaanottaja.setEpaonnistumisenSyy(lahetettyVastaanottajalle.getEpaonnistumisenSyy());
		
		return raportoitavaVastaanottaja;
	}

	@Override
	public void tallennaRaportoitavatVastaanottajat(List<RaportoitavaVastaanottaja> raportoitavatVastaanottajat) {
		for (RaportoitavaVastaanottaja raportoitavaVastaanottaja : raportoitavatVastaanottajat) {
			raportoitavaVastaanottajaDAO.insert(raportoitavaVastaanottaja);
		}
	}
}
