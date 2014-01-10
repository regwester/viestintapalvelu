package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LahetyksenAloitusDTO implements Serializable {
	private static final long serialVersionUID = 7381739634771992056L;

	private String prosessi;
	private String lahettajanOid;
	private String lahettajanOidTyyppi;
	private String lahettajanSahkopostiosoite;
	private String vastauksensaajaOid;
	private String vastauksenSaajanOidTyyppi;
	private String vastauksensaajanSahkoposti;
	private List<LahetettyVastaanottajalleDTO> vastaanottajat;
	private String aihe;
	private byte[] viesti;
	private List<LahetettyLiiteDTO> lahetetynviestinliitteet;	
	private Date lahetysAlkoi;
	
	public String getProsessi() {
		return prosessi;
	}
	
	public void setProsessi(String prosessi) {
		this.prosessi = prosessi;
	}
	
	public String getLahettajanOid() {
		return lahettajanOid;
	}
	
	public void setLahettajanOid(String lahettajanOid) {
		this.lahettajanOid = lahettajanOid;
	}
	
	public String getLahettajanOidTyyppi() {
		return lahettajanOidTyyppi;
	}

	public void setLahettajanOidTyyppi(String lahettajanOidTyyppi) {
		this.lahettajanOidTyyppi = lahettajanOidTyyppi;
	}

	public String getLahettajanSahkopostiosoite() {
		return lahettajanSahkopostiosoite;
	}
	
	public void setLahettajanSahkopostiosoite(String lahettajanSahkopostiosoite) {
		this.lahettajanSahkopostiosoite = lahettajanSahkopostiosoite;
	}
		
	public String getVastauksensaajaOid() {
		return vastauksensaajaOid;
	}
	
	public void setVastauksensaajaOid(String vastauksensaajaOid) {
		this.vastauksensaajaOid = vastauksensaajaOid;
	}
	
	public String getVastauksenSaajanOidTyyppi() {
		return vastauksenSaajanOidTyyppi;
	}

	public void setVastauksenSaajanOidTyyppi(String vastauksenSaajanOidTyyppi) {
		this.vastauksenSaajanOidTyyppi = vastauksenSaajanOidTyyppi;
	}

	public String getVastauksensaajanSahkoposti() {
		return vastauksensaajanSahkoposti;
	}
	
	public void setVastauksensaajanSahkoposti(String vastauksensaajanSahkoposti) {
		this.vastauksensaajanSahkoposti = vastauksensaajanSahkoposti;
	}

	public List<LahetettyVastaanottajalleDTO> getVastaanottajat() {
		return vastaanottajat;
	}

	public void setVastaanottajat(List<LahetettyVastaanottajalleDTO> vastaanottajat) {
		this.vastaanottajat = vastaanottajat;
	}

	public String getAihe() {
		return aihe;
	}
	
	public void setAihe(String aihe) {
		this.aihe = aihe;
	}
	
	public byte[] getViesti() {
		return viesti;
	}
	
	public void setViesti(byte[] viesti) {
		this.viesti = viesti;
	}
	
	public List<LahetettyLiiteDTO> getLahetetynviestinliitteet() {
		return lahetetynviestinliitteet;
	}
	
	public void setLahetetynviestinliitteet(List<LahetettyLiiteDTO> lahetetynviestinliitteet) {
		this.lahetetynviestinliitteet = lahetetynviestinliitteet;
	}
	
	public Date getLahetysAlkoi() {
		return lahetysAlkoi;
	}
	
	public void setLahetysAlkoi(Date lahetysAlkoi) {
		this.lahetysAlkoi = lahetysAlkoi;
	}
}
