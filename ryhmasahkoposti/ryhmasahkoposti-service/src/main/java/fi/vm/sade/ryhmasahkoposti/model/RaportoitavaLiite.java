package fi.vm.sade.ryhmasahkoposti.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name="raportoitavaliite")
@Entity
public class RaportoitavaLiite extends BaseEntity {
	private static final long serialVersionUID = -1443213045409858837L;

	@Column(name="liitetiedoston_nimi", nullable=false)
	private String liitetiedostonNimi;
	
	@Column(name="liitetiedosto")
	private byte[] liitetiedosto;
	
	@Column(name="sisaltotyyppi", nullable=false)
	private String sisaltotyyppi;

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
