package fi.vm.sade.ryhmasahkoposti.raportointi.testdata;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyLiiteDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.LahetyksenLopetusDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavanViestinLiite;

public class RaportointipalveluTestData {

	public static LahetettyLiiteDTO getLahetettyLiiteDTO(Long id) {
		LahetettyLiiteDTO liiteDTO = new LahetettyLiiteDTO();
		
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		liiteDTO.setLiitetiedosto(sisalto);
		liiteDTO.setLiitetiedostonID(id);
		liiteDTO.setLiitetiedostonNimi("koekutsu.doc");
		liiteDTO.setSisaltotyyppi("application/pdf");
		
		return liiteDTO;
	}
	
	public static LahetettyVastaanottajalleDTO getLahetettyVastaanottajalleDTO() {
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = new LahetettyVastaanottajalleDTO();
		
		lahetettyVastaanottajalle.setVastaanottajaOid("102030405100");
		lahetettyVastaanottajalle.setVastaanottajanOidTyyppi("oppilas");
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti("vastaan.ottaja@sposti.fi");
		lahetettyVastaanottajalle.setKielikoodi("FI");
		lahetettyVastaanottajalle.setLahetysalkoi(new Date());
		lahetettyVastaanottajalle.setLahetyspaattyi(new Date());
		lahetettyVastaanottajalle.setEpaonnistumisenSyy("");
		
		return lahetettyVastaanottajalle;
	
	}

	public static LahetyksenAloitusDTO getLahetyksenAloitusDTO() {
		LahetyksenAloitusDTO lahetyksenAloitus = new LahetyksenAloitusDTO();
		
		lahetyksenAloitus.setProsessi("Haku-prosessi");
		lahetyksenAloitus.setLahettajanOid("102030405000");
		lahetyksenAloitus.setLahettajanOidTyyppi("virkailija");
		lahetyksenAloitus.setLahettajanSahkopostiosoite("testi.lahettaja@oph.fi");
		lahetyksenAloitus.setVastauksensaajaOid("102030405100");
		lahetyksenAloitus.setVastauksenSaajanOidTyyppi("oppilaitos");
		lahetyksenAloitus.setVastauksensaajanSahkoposti("testi.vastauksensaaja@oph.fi");
		lahetyksenAloitus.setHtmlViesti(false);
		lahetyksenAloitus.setMerkisto("utf-8");
		lahetyksenAloitus.setAihe("Koekutsu");
		lahetyksenAloitus.setViesti("Kutsu kokeeseen");
		lahetyksenAloitus.setLahetysAlkoi(new Date());
		
		return lahetyksenAloitus;
	}

	public static List<LahetyksenAloitusDTO> getLahetyksenAloitusDTOLista() {
		List<LahetyksenAloitusDTO> lahetyksenAloitukset = new ArrayList<LahetyksenAloitusDTO>();
		
		LahetyksenAloitusDTO lahetyksenAloitus1 = new LahetyksenAloitusDTO();
		
		lahetyksenAloitus1.setProsessi("Haku-prosessi");
		lahetyksenAloitus1.setLahettajanOid("102030405010");
		lahetyksenAloitus1.setLahettajanOidTyyppi("virkailija");
		lahetyksenAloitus1.setLahettajanSahkopostiosoite("testi1.lahettaja@oph.fi");
		lahetyksenAloitus1.setVastauksensaajaOid("102030405110");
		lahetyksenAloitus1.setVastauksenSaajanOidTyyppi("oppilaitos");
		lahetyksenAloitus1.setVastauksensaajanSahkoposti("testi.vastauksensaaja@oph.fi");
		lahetyksenAloitus1.setHtmlViesti(false);
		lahetyksenAloitus1.setMerkisto("utf-8");
		lahetyksenAloitus1.setAihe("Koekutsu");
		lahetyksenAloitus1.setViesti("Kutsu kokeeseen");
		lahetyksenAloitus1.setLahetysAlkoi(new Date());
		
		lahetyksenAloitukset.add(lahetyksenAloitus1);

		LahetyksenAloitusDTO lahetyksenAloitus2 = new LahetyksenAloitusDTO();
		
		lahetyksenAloitus2.setProsessi("Haku-prosessi");
		lahetyksenAloitus2.setLahettajanOid("102030405020");
		lahetyksenAloitus2.setLahettajanOidTyyppi("virkailija");
		lahetyksenAloitus2.setLahettajanSahkopostiosoite("testi2.lahettaja@oph.fi");
		lahetyksenAloitus2.setVastauksensaajaOid("102030405120");
		lahetyksenAloitus2.setVastauksenSaajanOidTyyppi("oppilaitos");
		lahetyksenAloitus2.setVastauksensaajanSahkoposti("testi.vastauksensaaja@oph.fi");
		lahetyksenAloitus2.setHtmlViesti(false);
		lahetyksenAloitus2.setMerkisto("utf-8");
		lahetyksenAloitus2.setAihe("Koekutsu");
		lahetyksenAloitus2.setViesti("Kutsu kokeeseen");
		lahetyksenAloitus2.setLahetysAlkoi(new Date());
		
		lahetyksenAloitukset.add(lahetyksenAloitus2);
		
		return lahetyksenAloitukset;
	}
	
	public static LahetyksenLopetusDTO getLahetyksenLopetusDTO(Long viestiID) {
		LahetyksenLopetusDTO lahetyksenLopetus = new LahetyksenLopetusDTO();
		
		lahetyksenLopetus.setViestiID(viestiID);
		lahetyksenLopetus.setLahetysPaattyi(new Date());
		
		return lahetyksenLopetus;
	}

	public static RaportoitavaVastaanottaja getRaportoitavaVastaanottaja(RaportoitavaViesti raportoitavaViesti) {
		RaportoitavaVastaanottaja raportoitavaVastaanottaja = new RaportoitavaVastaanottaja();
		
		raportoitavaVastaanottaja.setRaportoitavaViesti(raportoitavaViesti);
		raportoitavaVastaanottaja.setVastaanottajaOid("102030405100");
		raportoitavaVastaanottaja.setVastaanottajaOidTyyppi("oppilas");
		raportoitavaVastaanottaja.setHenkilotunnus("");
		raportoitavaVastaanottaja.setVastaanottajanSahkoposti("testi.vastaanottaja@sposti.fi");
		raportoitavaVastaanottaja.setKielikoodi("FI");
		raportoitavaVastaanottaja.setHakuNimi("Testi Oppilas");
		raportoitavaVastaanottaja.setLahetysalkoi(new Date());
		raportoitavaVastaanottaja.setLahetyspaattyi(new Date());
		raportoitavaVastaanottaja.setEpaonnistumisenSyy("");
		raportoitavaVastaanottaja.setAikaleima(new Date());
		
		return raportoitavaVastaanottaja;
	}

	public static RaportoitavaViesti getRaportoitavaViestiLahetyksenAloitusDTOTiedosta() {
		LahetyksenAloitusDTO lahetyksenAloitus = getLahetyksenAloitusDTO();
		
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
		raportoitavaViesti.setHtmlViesti("");
		raportoitavaViesti.setMerkisto("utf-8");
		raportoitavaViesti.setLahetysAlkoi(lahetyksenAloitus.getLahetysAlkoi());
		raportoitavaViesti.setAikaleima(new Date());
		
		return raportoitavaViesti;
	}

	public static List<RaportoitavaViesti> getRaportoitavaViestiListaLahetyksenAloitusDTOTiedoista() {
		List<RaportoitavaViesti> raportoitavatViestit = new ArrayList<RaportoitavaViesti>();
		
		List<LahetyksenAloitusDTO> lahetyksenAloitukset = getLahetyksenAloitusDTOLista();
		
		for (LahetyksenAloitusDTO lahetyksenAloitus : lahetyksenAloitukset) {
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
			raportoitavaViesti.setHtmlViesti("");
			raportoitavaViesti.setMerkisto("utf-8");
			raportoitavaViesti.setLahetysAlkoi(lahetyksenAloitus.getLahetysAlkoi());
			raportoitavaViesti.setAikaleima(new Date());
			
			raportoitavatViestit.add(raportoitavaViesti);
		}
		
		return raportoitavatViestit;
	}

	public static RaportoitavaViesti getRaportoitavatViesti() {
		RaportoitavaViesti raportoitavaViesti = new RaportoitavaViesti();
		
		raportoitavaViesti.setId(new Long(200));
		raportoitavaViesti.setVersion(new Long(0));
		raportoitavaViesti.setAihe("Kokekutsu");
		raportoitavaViesti.setProsessi("Hakuprosessi");
		raportoitavaViesti.setLahettajanOid("1.2.246.562.24.42645159413");
		raportoitavaViesti.setLahettajanOidTyyppi("virkailija");
		raportoitavaViesti.setLahettajanSahkopostiosoite("testi.virkailija@sposti.fi");
		raportoitavaViesti.setVastauksensaajanOid("1.2.246.562.24.42645159413");
		raportoitavaViesti.setVastauksensaajanOidTyyppi("virkailija");
		raportoitavaViesti.setVastauksensaajanSahkopostiosoite("testi.virkailija@sposti.fi");
		raportoitavaViesti.setViesti("Tämä on koekutsu");
		raportoitavaViesti.setHtmlViesti("");
		raportoitavaViesti.setMerkisto("utf-8");
		raportoitavaViesti.setLahetysAlkoi(null);
		raportoitavaViesti.setAikaleima(new Date());

		RaportoitavanViestinLiite viestinLiite = new RaportoitavanViestinLiite();
		
		viestinLiite.setId(new Long(400));
		viestinLiite.setRaportoitavaliiteID(new Long(100));
		viestinLiite.setRaportoitavaviesti(raportoitavaViesti);
		viestinLiite.setVersion(new Long(0));
		viestinLiite.setAikaleima(new Date());
		
		Set<RaportoitavanViestinLiite> viestinLiitteet = new HashSet<RaportoitavanViestinLiite>();
		viestinLiitteet.add(viestinLiite);
		raportoitavaViesti.setRaportoitavanViestinLiitteet(viestinLiitteet);

		RaportoitavaVastaanottaja raportoitavaVastaanottaja = new RaportoitavaVastaanottaja();
		
		raportoitavaVastaanottaja.setId(new Long(300));
		raportoitavaVastaanottaja.setVersion(new Long(0));
		raportoitavaVastaanottaja.setRaportoitavaViesti(raportoitavaViesti);
		raportoitavaVastaanottaja.setVastaanottajaOid("1.2.246.562.24.34397748041");
		raportoitavaVastaanottaja.setVastaanottajaOidTyyppi("oppilas");
		raportoitavaVastaanottaja.setHenkilotunnus("");
		raportoitavaVastaanottaja.setVastaanottajanSahkoposti("testi.vastaanottaja@sposti.fi");
		raportoitavaVastaanottaja.setKielikoodi("FI");
		raportoitavaVastaanottaja.setHakuNimi("Testi Oppilas");
		raportoitavaVastaanottaja.setLahetysalkoi(null);
		raportoitavaVastaanottaja.setLahetyspaattyi(null);
		raportoitavaVastaanottaja.setEpaonnistumisenSyy("");
		raportoitavaVastaanottaja.setAikaleima(new Date());
		
		Set<RaportoitavaVastaanottaja> vastaanottajat = new HashSet<RaportoitavaVastaanottaja>();
		vastaanottajat.add(raportoitavaVastaanottaja);
		raportoitavaViesti.setRaportoitavatVastaanottajat(vastaanottajat);
		
		
		return raportoitavaViesti;
	}
	
	public static RaportoitavaLiite getRaportoitavaLiite() {
		RaportoitavaLiite raportoitavaLiite = new RaportoitavaLiite();
		
		byte[] sisalto = {'k', 'o', 'e', 'k', 'u', 't', 's', 'u'};
		
		raportoitavaLiite.setId(new Long(100));
		raportoitavaLiite.setVersion(new Long(0));
		raportoitavaLiite.setLiitetiedostonNimi("koekutsu.doc");
		raportoitavaLiite.setSisaltotyyppi("application/pdf");
		raportoitavaLiite.setLiitetiedosto(sisalto);
		raportoitavaLiite.setAikaleima(new Date());
		
		return raportoitavaLiite; 		
	}
	
	public static Long getViestiID() {
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		return timestamp.getTime();
	}
}
