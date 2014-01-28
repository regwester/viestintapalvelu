package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;

public class RaportoitavaLiiteDTO implements Serializable {
	private static final long serialVersionUID = -5608940509445472898L;
	
	private Long liiteID;
	private String liitetiedostonNimi;
	private byte[] liitetiedosto;
	private String sisaltotyyppi;
	
	public Long getLiiteID() {
		return liiteID;
	}
	
	public void setLiiteID(Long liiteID) {
		this.liiteID = liiteID;
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
