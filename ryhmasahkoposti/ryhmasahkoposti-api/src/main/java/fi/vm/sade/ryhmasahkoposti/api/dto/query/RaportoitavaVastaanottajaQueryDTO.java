package fi.vm.sade.ryhmasahkoposti.api.dto.query;

public class RaportoitavaVastaanottajaQueryDTO {
	private String vastaanottajanOid;
	private String vastaanottajanHenkilotunnus;
	private String vastaanottajanSahkopostiosoite;
	private String vastaanottajanNimi;
	
	public String getVastaanottajanOid() {
		return vastaanottajanOid;
	}
	
	public void setVastaanottajanOid(String vastaanottajanOid) {
		this.vastaanottajanOid = vastaanottajanOid;
	}
	
	public String getVastaanottajanHenkilotunnus() {
		return vastaanottajanHenkilotunnus;
	}

	public void setVastaanottajanHenkilotunnus(String vastaanottajanHenkilotunnus) {
		this.vastaanottajanHenkilotunnus = vastaanottajanHenkilotunnus;
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
}
