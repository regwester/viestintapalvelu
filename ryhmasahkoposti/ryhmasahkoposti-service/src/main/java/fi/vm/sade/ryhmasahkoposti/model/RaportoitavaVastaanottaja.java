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

@Table(name="raportoitavavastaanottaja")
@Entity
public class RaportoitavaVastaanottaja extends BaseEntity {
	private static final long serialVersionUID = -4957288730521500299L;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="lahetettyviesti_id")
	private RaportoitavaViesti raportoitavaviesti;
	
	@Column(name="vastaanottajan_oid", nullable=false)
	private String vastaanottajaOid;

	@Column(name="vastaanottajan_oid_tyyppi", nullable=false)
	private String vastaanottajaOidTyyppi;

	@Column(name="vastaanottajan_sahkopostiosoite", nullable=false)
	private String vastaanottajanSahkoposti;
	
	@Column(name="kielikoodi", nullable=false)
	private String kielikoodi;
	
	@Column(name="hakunimi", nullable=false)
	private String hakuNimi;
	
	@Column(name="lahetysalkoi", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lahetysalkoi;

	@Column(name="lahetyspaattyi", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lahetyspaattyi;

	@Column(name="lahetysonnistui", nullable=true)
	private String lahetysOnnistui;
	
	@Column(name="epaonnistumisensyy", nullable=true)
	private String epaonnistumisenSyy;
	
	@Column(name="aikaleima", nullable=false)
	private Date aikaleima;

	public RaportoitavaViesti getRaportoitavaviesti() {
		return raportoitavaviesti;
	}

	public void setRaportoitavaViesti(RaportoitavaViesti raportoitavaViesti) {
		this.raportoitavaviesti = raportoitavaViesti;
	}

	public String getVastaanottajaOid() {
		return vastaanottajaOid;
	}

	public void setVastaanottajaOid(String vastaanottajaOid) {
		this.vastaanottajaOid = vastaanottajaOid;
	}

	public String getVastaanottajaOidTyyppi() {
		return vastaanottajaOidTyyppi;
	}

	public void setVastaanottajaOidTyyppi(String vastaanottajaOidTyyppi) {
		this.vastaanottajaOidTyyppi = vastaanottajaOidTyyppi;
	}

	public String getVastaanottajanSahkoposti() {
		return vastaanottajanSahkoposti;
	}

	public void setVastaanottajanSahkoposti(String vastaanottajanSahkoposti) {
		this.vastaanottajanSahkoposti = vastaanottajanSahkoposti;
	}

	public String getKielikoodi() {
		return kielikoodi;
	}

	public void setKielikoodi(String kielikoodi) {
		this.kielikoodi = kielikoodi;
	}

	public String getHakuNimi() {
		return hakuNimi;
	}

	public void setHakuNimi(String hakuNimi) {
		this.hakuNimi = hakuNimi;
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

	public String getLahetysOnnistui() {
		return lahetysOnnistui;
	}

	public void setLahetysOnnistui(String lahetysOnnistui) {
		this.lahetysOnnistui = lahetysOnnistui;
	}

	public String getEpaonnistumisenSyy() {
		return epaonnistumisenSyy;
	}

	public void setEpaonnistumisenSyy(String epaonnistumisenSyy) {
		this.epaonnistumisenSyy = epaonnistumisenSyy;
	}

	public Date getAikaleima() {
		return aikaleima;
	}

	public void setAikaleima(Date aikaleima) {
		this.aikaleima = aikaleima;
	}

}
