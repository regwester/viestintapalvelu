package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;

public class LahetettyLiiteDTO implements Serializable {
	private static final long serialVersionUID = -3491620065722103252L;

	private Long liitetiedostonID;
	private String liitetiedostonNimi;
	private byte[] liitetiedosto;
	private String sisaltotyyppi;
	
	public Long getLiitetiedostonID() {
		return liitetiedostonID;
	}

	public void setLiitetiedostonID(Long liitetiedostonID) {
		this.liitetiedostonID = liitetiedostonID;
	}

	public String getLiitetiedostonNimi() {
		return liitetiedostonNimi;
	}
	
	public void setLiitetiedostonNimi(String liitetiedostonNimi) {
		this.liitetiedostonNimi = liitetiedostonNimi;
	}
	
	public byte[] getLiitetiedosto() {
		return liitetiedosto;
	}
	
	public void setLiitetiedosto(byte[] liitetiedosto) {
		this.liitetiedosto = liitetiedosto;
	}

	public String getSisaltotyyppi() {
		return sisaltotyyppi;
	}

	public void setSisaltotyyppi(String sisaltotyyppi) {
		this.sisaltotyyppi = sisaltotyyppi;
	}
}
