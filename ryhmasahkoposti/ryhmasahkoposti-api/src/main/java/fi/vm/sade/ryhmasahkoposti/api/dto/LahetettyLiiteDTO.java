package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;

public class LahetettyLiiteDTO implements Serializable {
	private static final long serialVersionUID = -3491620065722103252L;

	private String liitetiedostonNimi;
	private byte[] liitetiedosto;
	
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
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
