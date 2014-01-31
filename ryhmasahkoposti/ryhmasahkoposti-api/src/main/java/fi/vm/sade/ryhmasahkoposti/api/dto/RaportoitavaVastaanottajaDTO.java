package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;

public class RaportoitavaVastaanottajaDTO implements Serializable {
	private static final long serialVersionUID = 139612329429841338L;
	
	private Long vastaanottajaID;
	private String etunimi;
	private String sukunimi;
	private String organisaationNimi;
	private String vastaanottajanOid;
	private String vastaanottajanSahkopostiosoite;
	private String lahetysOnnistui;
	private Long viestiID;
	private String aikaleima;
	
	public Long getVastaanottajaID() {
		return vastaanottajaID;
	}

	public void setVastaanottajaID(Long vastaanottajaID) {
		this.vastaanottajaID = vastaanottajaID;
	}

	public String getEtunimi() {
		return etunimi;
	}
	
	public void setEtunimi(String etunimi) {
		this.etunimi = etunimi;
	}
	
	public String getSukunimi() {
		return sukunimi;
	}
	
	public void setSukunimi(String sukunimi) {
		this.sukunimi = sukunimi;
	}
	
	public String getOrganisaationNimi() {
		return organisaationNimi;
	}
	
	public void setOrganisaationNimi(String organisaationNimi) {
		this.organisaationNimi = organisaationNimi;
	}
	
	public String getVastaanottajanOid() {
		return vastaanottajanOid;
	}
	
	public void setVastaanottajanOid(String vastaanottajanOid) {
		this.vastaanottajanOid = vastaanottajanOid;
	}
	
	public String getVastaanottajanSahkopostiosoite() {
		return vastaanottajanSahkopostiosoite;
	}
	
	public void setVastaanottajanSahkopostiosoite(
			String vastaanottajanSahkopostiosoite) {
		this.vastaanottajanSahkopostiosoite = vastaanottajanSahkopostiosoite;
	}
	
	public String isLahetysOnnistui() {
		return lahetysOnnistui;
	}
	
	public void setLahetysOnnistui(String lahetysOnnistui) {
		this.lahetysOnnistui = lahetysOnnistui;
	}


	public Long getViestiID() {
		return viestiID;
	}

	public void setViestiID(Long viestiID) {
		this.viestiID = viestiID;
	}

	public String getAikaleima() {
		return aikaleima;
	}

	public void setAikaleima(String aikaleima) {
		this.aikaleima = aikaleima;
	}
}
