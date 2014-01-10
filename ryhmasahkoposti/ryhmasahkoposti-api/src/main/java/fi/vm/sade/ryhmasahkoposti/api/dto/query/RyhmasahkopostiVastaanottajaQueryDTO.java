package fi.vm.sade.ryhmasahkoposti.api.dto.query;

import java.util.Date;

public class RyhmasahkopostiVastaanottajaQueryDTO {
	private String vastaanottajanOid;
	private String vastaanottajanSahkopostiosoite;
	private String vastaanottajanNimi;
	private String vastaanottajanOrganisaatio;
	private String vastaanottajanRooli;
	private String sanaHaku;
	private Date lahetysajankohta;
	
	public String getVastaanottajanOid() {
		return vastaanottajanOid;
	}
	
	public void setVastaanottajanOid(String vastaanottajanOid) {
		this.vastaanottajanOid = vastaanottajanOid;
	}
	
	public String getVastaanottajanSahkopostiosoite() {
		return vastaanottajanSahkopostiosoite;
	}
	
	public void setVastaanottajanSahkopostiosoite(String vastaanottajanSahkopostiosoite) {
		this.vastaanottajanSahkopostiosoite = vastaanottajanSahkopostiosoite;
	}
	
	public String getVastaanottajanNimi() {
		return vastaanottajanNimi;
	}

	public void setVastaanottajanNimi(String vastaanottajanNimi) {
		this.vastaanottajanNimi = vastaanottajanNimi;
	}

	public String getVastaanottajanOrganisaatio() {
		return vastaanottajanOrganisaatio;
	}

	public void setVastaanottajanOrganisaatio(String vastaanottajanOrganisaatio) {
		this.vastaanottajanOrganisaatio = vastaanottajanOrganisaatio;
	}

	public String getVastaanottajanRooli() {
		return vastaanottajanRooli;
	}

	public void setVastaanottajanRooli(String vastaanottajanRooli) {
		this.vastaanottajanRooli = vastaanottajanRooli;
	}

	public String getSanaHaku() {
		return sanaHaku;
	}

	public void setSanaHaku(String sanaHaku) {
		this.sanaHaku = sanaHaku;
	}

	public Date getLahetysajankohta() {
		return lahetysajankohta;
	}
	
	public void setLahetysajankohta(Date lahetysajankohta) {
		this.lahetysajankohta = lahetysajankohta;
	}
}
