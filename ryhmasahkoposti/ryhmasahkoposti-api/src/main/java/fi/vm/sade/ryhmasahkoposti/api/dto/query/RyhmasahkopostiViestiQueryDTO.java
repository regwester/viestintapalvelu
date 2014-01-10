package fi.vm.sade.ryhmasahkoposti.api.dto.query;

import java.util.Date;

public class RyhmasahkopostiViestiQueryDTO {
	private String lahettajanOid;
	private String lahettajanSahkoposti;
	private Date lahetysAjankohta;
	private RyhmasahkopostiVastaanottajaQueryDTO vastaanottajaQuery;
	
	public String getLahettajanOid() {
		return lahettajanOid;
	}
	
	public void setLahettajanOid(String lahettajanOid) {
		this.lahettajanOid = lahettajanOid;
	}
	
	public String getLahettajanSahkoposti() {
		return lahettajanSahkoposti;
	}
	
	public void setLahettajanSahkoposti(String lahettajanSahkoposti) {
		this.lahettajanSahkoposti = lahettajanSahkoposti;
	}
	
	public Date getLahetysAjankohta() {
		return lahetysAjankohta;
	}
	
	public void setLahetysAjankohta(Date lahetysAjankohta) {
		this.lahetysAjankohta = lahetysAjankohta;
	}

	public RyhmasahkopostiVastaanottajaQueryDTO getVastaanottajaQuery() {
		return vastaanottajaQuery;
	}

	public void setVastaanottajaQuery(RyhmasahkopostiVastaanottajaQueryDTO vastaanottajaQuery) {
		this.vastaanottajaQuery = vastaanottajaQuery;
	}	
}
