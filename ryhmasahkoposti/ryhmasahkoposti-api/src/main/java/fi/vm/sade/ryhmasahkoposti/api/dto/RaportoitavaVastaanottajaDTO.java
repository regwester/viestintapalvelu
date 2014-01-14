package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;

public class RaportoitavaVastaanottajaDTO implements Serializable {
	private static final long serialVersionUID = 139612329429841338L;
	
	private String etunimi;
	private String sukunimi;
	private String organisaationNimi;
	private String vastaanottajan_oid;
	private String vastaanottajanSahkopostiosoite;
	private boolean lahetysOnnistui;
	
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
	
	public String getVastaanottajan_oid() {
		return vastaanottajan_oid;
	}
	
	public void setVastaanottajan_oid(String vastaanottajan_oid) {
		this.vastaanottajan_oid = vastaanottajan_oid;
	}
	
	public String getVastaanottajanSahkopostiosoite() {
		return vastaanottajanSahkopostiosoite;
	}
	
	public void setVastaanottajanSahkopostiosoite(
			String vastaanottajanSahkopostiosoite) {
		this.vastaanottajanSahkopostiosoite = vastaanottajanSahkopostiosoite;
	}
	
	public boolean isLahetysOnnistui() {
		return lahetysOnnistui;
	}
	
	public void setLahetysOnnistui(boolean lahetysOnnistui) {
		this.lahetysOnnistui = lahetysOnnistui;
	}
}
