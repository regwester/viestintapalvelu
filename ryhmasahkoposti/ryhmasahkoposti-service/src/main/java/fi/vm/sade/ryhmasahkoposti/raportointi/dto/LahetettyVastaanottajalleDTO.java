package fi.vm.sade.ryhmasahkoposti.raportointi.dto;

import java.io.Serializable;
import java.util.Date;

public class LahetettyVastaanottajalleDTO implements Serializable {
	private static final long serialVersionUID = -8589712958175761263L;

	private Long viestiID;
	private String vastaanottajaOid;
	private String vastaanottajanOidTyyppi;
	private String vastaanottajanSahkoposti;
	private Date lahetysalkoi;
	private Date lahetyspaattyi;
	private String epaonnistumisenSyy;

	public Long getViestiID() {
		return viestiID;
	}
	
	public void setViestiID(Long viestiID) {
		this.viestiID = viestiID;
	}
	
	public String getVastaanottajaOid() {
		return vastaanottajaOid;
	}
	
	public void setVastaanottajaOid(String vastaanottajaOid) {
		this.vastaanottajaOid = vastaanottajaOid;
	}
	
	public String getVastaanottajanOidTyyppi() {
		return vastaanottajanOidTyyppi;
	}

	public void setVastaanottajanOidTyyppi(String vastaanottajanOidTyyppi) {
		this.vastaanottajanOidTyyppi = vastaanottajanOidTyyppi;
	}

	public String getVastaanottajanSahkoposti() {
		return vastaanottajanSahkoposti;
	}
	
	public void setVastaanottajanSahkoposti(String vastaanottajanSahkoposti) {
		this.vastaanottajanSahkoposti = vastaanottajanSahkoposti;
	}

	public Date getLahetysalkoi() {
		return lahetysalkoi;
	}

	public void setLahetysalkoi(Date lahetysalkoi) {
		this.lahetysalkoi = lahetysalkoi;
	}

	public Date getLahetyspaattyi() {
		return lahetyspaattyi;
	}

	public void setLahetyspaattyi(Date lahetyspaattyi) {
		this.lahetyspaattyi = lahetyspaattyi;
	}

	public String getEpaonnistumisenSyy() {
		return epaonnistumisenSyy;
	}

	public void setEpaonnistumisenSyy(String epaonnistumisenSyy) {
		this.epaonnistumisenSyy = epaonnistumisenSyy;
	}
}
