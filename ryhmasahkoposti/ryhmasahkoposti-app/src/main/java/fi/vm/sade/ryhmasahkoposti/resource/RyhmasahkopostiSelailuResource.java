package fi.vm.sade.ryhmasahkoposti.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetettyLiiteDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.query.RyhmasahkopostiVastaanottajaQueryDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.query.RyhmasahkopostiViestiQueryDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaViesti;
import fi.vm.sade.ryhmasahkoposti.raportointi.service.RyhmasahkopostinRaportointiService;

@Component
@Path("ryhmasahkoposti")
public class RyhmasahkopostiSelailuResource {
	private RyhmasahkopostinRaportointiService ryhmasahkopostinRaportointiService;

	@Autowired
	public RyhmasahkopostiSelailuResource(RyhmasahkopostinRaportointiService ryhmasahkopostinRaportointiService) {
		this.ryhmasahkopostinRaportointiService = ryhmasahkopostinRaportointiService;
	}
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("query")
	@POST
	public RyhmasahkopostiViestiQueryDTO getRyhmasahkopostiQuery() {
		RyhmasahkopostiViestiQueryDTO viestiQuery = new RyhmasahkopostiViestiQueryDTO();
		viestiQuery.setLahettajanOid("10002000");
		viestiQuery.setLahettajanSahkoposti("lahettaja@sposti.fi");
		
		RyhmasahkopostiVastaanottajaQueryDTO vastaanottajaQuery = new RyhmasahkopostiVastaanottajaQueryDTO();
		vastaanottajaQuery.setLahetysajankohta(new Date());
		vastaanottajaQuery.setSanaHaku("Hae nimella tai tunnuksella");
		vastaanottajaQuery.setVastaanottajanNimi("VastaanottajanNimi");
		vastaanottajaQuery.setVastaanottajanOid("20001000");
		vastaanottajaQuery.setVastaanottajanRooli("VastaanottajanRooli");
		vastaanottajaQuery.setVastaanottajanSahkopostiosoite("vastaanottajanSahkopostiosoite");
		
		viestiQuery.setVastaanottajaQuery(vastaanottajaQuery);
		
		return viestiQuery;
	}
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("aloitus")
	@POST
	public LahetyksenAloitusDTO getLahetykseAloitus() {
		LahetyksenAloitusDTO lahetyksenAloitus  = getLahetyksenAloitusDTO();
		
		List<LahetettyVastaanottajalleDTO> vastaanottajat = new ArrayList<LahetettyVastaanottajalleDTO>();
		
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = getLahetettyVastaanottajalleDTO();
		lahetettyVastaanottajalle.setVastaanottajanSahkoposti("vastaanottajanSahkoposti");
		
		vastaanottajat.add(lahetettyVastaanottajalle);	
		lahetyksenAloitus.setVastaanottajat(vastaanottajat);
				
		List<LahetettyLiiteDTO> liite = new ArrayList<LahetettyLiiteDTO>();
		liite.add(getLahetettyLiiteDTO());
		lahetyksenAloitus.setLahetetynviestinliitteet(liite);
		
		return lahetyksenAloitus;		
	}	
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("alusta")
	@POST
	public List<RaportoitavaViesti> getRyhmasahkopostit(RyhmasahkopostiViestiQueryDTO query) {
		return ryhmasahkopostinRaportointiService.haeRaportoitavatViestit(query);
	}

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("tallenna")
	@POST
	public void raportoiLahetyksenaloitus(LahetyksenAloitusDTO lahetyksenAloitus) {		
		if (ryhmasahkopostinRaportointiService == null) {
			System.out.println("** ryhmasahkopostinRaportointiService == null **");
		} else {
			System.out.println("** raportoiLahetyksenaloitus(LahetyksenAloitusDTO lahetyksenAloitus) on OK **");
		}
		
		try {
			ryhmasahkopostinRaportointiService.raportoiLahetyksenAloitus(lahetyksenAloitus);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private LahetettyVastaanottajalleDTO getLahetettyVastaanottajalleDTO() {
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle = new LahetettyVastaanottajalleDTO();
		
		lahetettyVastaanottajalle.setVastaanottajaOid("102030405100");
		lahetettyVastaanottajalle.setVastaanottajanOidTyyppi("oppilas");
		lahetettyVastaanottajalle.setLahetysalkoi(new Date());
		lahetettyVastaanottajalle.setLahetyspaattyi(new Date());
		lahetettyVastaanottajalle.setEpaonnistumisenSyy("");
		
		return lahetettyVastaanottajalle;
	
	}
	
	private LahetyksenAloitusDTO getLahetyksenAloitusDTO() {
		LahetyksenAloitusDTO lahetyksenAloitus = new LahetyksenAloitusDTO();
		
		lahetyksenAloitus.setProsessi("Haku-prosessi");
		lahetyksenAloitus.setLahettajanOid("102030405000");
		lahetyksenAloitus.setLahettajanOidTyyppi("virkailija");
		lahetyksenAloitus.setLahettajanSahkopostiosoite("testi.lahettaja@oph.fi");
		lahetyksenAloitus.setVastauksensaajaOid("102030405100");
		lahetyksenAloitus.setVastauksenSaajanOidTyyppi("oppilaitos");
		lahetyksenAloitus.setVastauksensaajanSahkoposti("testi.vastauksensaaja@oph.fi");
		lahetyksenAloitus.setAihe("Koekutsu");
		lahetyksenAloitus.setViesti(new String("Kutsu kokeeseen").getBytes());
		lahetyksenAloitus.setLahetysAlkoi(new Date());
		
		return lahetyksenAloitus;
	}
	
	private LahetettyLiiteDTO getLahetettyLiiteDTO() {
		LahetettyLiiteDTO liite = new LahetettyLiiteDTO();
		
		liite.setLiitetiedostonNimi("liitetiedostonNimi");
		liite.setLiitetiedosto(new String("liitetiedoston sisalto").getBytes());
		
		return liite;
	}
}
