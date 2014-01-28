package fi.vm.sade.ryhmasahkoposti.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name="raportoitavaviesti_raportoitavaliite")
@Entity
public class RaportoitavanViestinLiite extends BaseEntity {
	private static final long serialVersionUID = -8639217659820696701L;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="lahetettyviesti_id")
	private RaportoitavaViesti raportoitavaviesti;
	
	@Column(name="raportoitavaliite_id")
	private Long raportoitavaliiteID;
	
	@Column(name="aikaleima", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date aikaleima;

	public RaportoitavaViesti getRaportoitavaviesti() {
		return raportoitavaviesti;
	}

	public void setRaportoitavaviesti(RaportoitavaViesti raportoitavaviesti) {
		this.raportoitavaviesti = raportoitavaviesti;
	}

	public Long getRaportoitavaliiteID() {
		return raportoitavaliiteID;
	}

	public void setRaportoitavaliiteID(Long raportoitavaliiteID) {
		this.raportoitavaliiteID = raportoitavaliiteID;
	}

	public Date getAikaleima() {
		return aikaleima;
	}

	public void setAikaleima(Date aikaleima) {
		this.aikaleima = aikaleima;
	}
}
