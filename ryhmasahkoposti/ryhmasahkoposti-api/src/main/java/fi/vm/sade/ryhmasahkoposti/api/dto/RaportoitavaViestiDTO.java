package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RaportoitavaViestiDTO extends EmailMessageDTO implements Serializable {
	private static final long serialVersionUID = 1899969471276794216L;

	private Long viestiID;
	private String prosessi;
	private String lahettajanSahkopostiosoite;
	private String vastauksenSaajanSahkopostiosoite;
	private Date lahetysAlkoi;
	private Date lahetysPaattyi;
	private String lahetysraportti;
	private String aihe;
	private String viestinSisalto;
	private List<RaportoitavaLiiteDTO> liitetiedostot;
	private List<RaportoitavaVastaanottajaDTO> vastaanottajat;
	
	public Long getViestiID() {
		return viestiID;
	}

	public void setViestiID(Long viestiID) {
		this.viestiID = viestiID;
	}

	public String getProsessi() {
		return prosessi;
	}
	
	public void setProsessi(String prosessi) {
		this.prosessi = prosessi;
	}
	
	public String getLahettajanSahkopostiosoite() {
		return lahettajanSahkopostiosoite;
	}
	
	public void setLahettajanSahkopostiosoite(String lahettajanSahkopostiosoite) {
		this.lahettajanSahkopostiosoite = lahettajanSahkopostiosoite;
	}
	
	public String getVastauksenSaajanSahkopostiosoite() {
		return vastauksenSaajanSahkopostiosoite;
	}
	
	public void setVastauksenSaajanSahkopostiosoite(
			String vastauksenSaajanSahkopostiosoite) {
		this.vastauksenSaajanSahkopostiosoite = vastauksenSaajanSahkopostiosoite;
	}
	
	public Date getLahetysAlkoi() {
		return lahetysAlkoi;
	}
	
	public void setLahetysAlkoi(Date lahetysAlkoi) {
		this.lahetysAlkoi = lahetysAlkoi;
	}
	
	public Date getLahetysPaattyi() {
		return lahetysPaattyi;
	}
	
	public void setLahetysPaattyi(Date lahetysPaattyi) {
		this.lahetysPaattyi = lahetysPaattyi;
	}
	
	public String getLahetysraportti() {
		return lahetysraportti;
	}
	
	public void setLahetysraportti(String lahetysraportti) {
		this.lahetysraportti = lahetysraportti;
	}
	
	public String getAihe() {
		return aihe;
	}
	
	public void setAihe(String aihe) {
		this.aihe = aihe;
	}
	
	public String getViestinSisalto() {
		return viestinSisalto;
	}
	
	public void setViestinSisalto(String viestinSisalto) {
		this.viestinSisalto = viestinSisalto;
	}

	public List<RaportoitavaLiiteDTO> getLiitetiedostot() {
		return liitetiedostot;
	}

	public void setLiitetiedostot(List<RaportoitavaLiiteDTO> liitetiedostot) {
		this.liitetiedostot = liitetiedostot;
	}

	public List<RaportoitavaVastaanottajaDTO> getVastaanottajat() {
		return vastaanottajat;
	}

	public void setVastaanottajat(List<RaportoitavaVastaanottajaDTO> vastaanottajat) {
		this.vastaanottajat = vastaanottajat;
	}


}
