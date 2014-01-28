package fi.vm.sade.ryhmasahkoposti.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	@Column(name="aikaleima", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date aikaleima;

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

	public Date getAikaleima() {
		return aikaleima;
	}

	public void setAikaleima(Date aikaleima) {
		this.aikaleima = aikaleima;
	}
	
	
}
