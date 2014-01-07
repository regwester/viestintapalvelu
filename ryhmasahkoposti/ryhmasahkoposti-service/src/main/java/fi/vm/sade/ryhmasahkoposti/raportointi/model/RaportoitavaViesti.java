package fi.vm.sade.ryhmasahkoposti.raportointi.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name="raportoitavaviesti")
@Entity()
public class RaportoitavaViesti extends BaseEntity {
	private static final long serialVersionUID = 7511140604535983187L;

	@Column(name="prosessi", nullable=false)
	private String prosessi;
	
	@Column(name="lahettajan_oid", nullable=false)
	private String lahettajanOid;

	@Column(name="lahettajan_oid_tyyppi", nullable=false)
	private String lahettajanOidTyyppi;

	@Column(name="lahettajan_sahkopostiosoite", nullable=false)
	private String lahettajanSahkopostiosoite;

	@Column(name="vastauksensaajan_oid", nullable=true)
	private String vastauksensaajanOid;

	@Column(name="vastauksensaajan_oid_tyyppi", nullable=true)
	private String vastauksensaajanOidTyyppi;

	@Column(name="vastauksensaajan_sahkopostiosoite", nullable=true)
	private String vastauksensaajanSahkopostiosoite;

	@Column(name="aihe", nullable=false)
	private String aihe;
	
	@Column(name="viesti", nullable=false)
	private byte[] viesti;

	@OneToMany(mappedBy="raportoitavaviesti", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	List<RaportoitavaVastaanottaja> raportoitavatVastaanottajat;
	
	@OneToMany(mappedBy="raportoitavaviesti", fetch=FetchType.LAZY)
	List<RaportoitavaLiite> raportoitavatLiitteet;	
	
	@Column(name="lahetysalkoi", nullable=false)
	private Date lahetysAlkoi;
	
	@Column(name="lahetyspaattyi", nullable=true)
	private Date lahetysPaattyi;
	
	public String getProsessi() {
		return prosessi;
	}

	public void setProsessi(String prosessi) {
		this.prosessi = prosessi;
	}

	public String getLahettajanOid() {
		return lahettajanOid;
	}

	public void setLahettajanOid(String lahettajanOid) {
		this.lahettajanOid = lahettajanOid;
	}

	public String getLahettajanOidTyyppi() {
		return lahettajanOidTyyppi;
	}

	public void setLahettajanOidTyyppi(String lahettajanOidTyyppi) {
		this.lahettajanOidTyyppi = lahettajanOidTyyppi;
	}

	public String getLahettajanSahkopostiosoite() {
		return lahettajanSahkopostiosoite;
	}

	public void setLahettajanSahkopostiosoite(String lahettajanSahkopostiosoite) {
		this.lahettajanSahkopostiosoite = lahettajanSahkopostiosoite;
	}

	public String getVastauksensaajanOid() {
		return vastauksensaajanOid;
	}

	public void setVastauksensaajanOid(String vastauksensaajanOid) {
		this.vastauksensaajanOid = vastauksensaajanOid;
	}

	public String getVastauksensaajanOidTyyppi() {
		return vastauksensaajanOidTyyppi;
	}

	public void setVastauksensaajanOidTyyppi(String vastauksensaajanOidTyyppi) {
		this.vastauksensaajanOidTyyppi = vastauksensaajanOidTyyppi;
	}

	public String getVastauksensaajanSahkopostiosoite() {
		return vastauksensaajanSahkopostiosoite;
	}

	public void setVastauksensaajanSahkopostiosoite(String vastauksensaajanSahkopostiosoite) {
		this.vastauksensaajanSahkopostiosoite = vastauksensaajanSahkopostiosoite;
	}

	public String getAihe() {
		return aihe;
	}

	public void setAihe(String aihe) {
		this.aihe = aihe;
	}

	public byte[] getViesti() {
		return viesti;
	}

	public void setViesti(byte[] viesti) {
		this.viesti = viesti;
	}

	public List<RaportoitavaVastaanottaja> getRaportoitavatVastaanottajat() {
		return raportoitavatVastaanottajat;
	}

	public void setRaportoitavatVastaanottajat(List<RaportoitavaVastaanottaja> raportoitavatVastaanottajat) {
		this.raportoitavatVastaanottajat = raportoitavatVastaanottajat;
	}

	public List<RaportoitavaLiite> getRaportoitavatLiitteet() {
		return raportoitavatLiitteet;
	}

	public void setRaportoitavatLiitteet(List<RaportoitavaLiite> raportoitavatLiitteet) {
		this.raportoitavatLiitteet = raportoitavatLiitteet;
	}

	public Date getLahetysAlkoi() {
		return lahetysAlkoi;
	}

	public void setLahetysAlkoi(Date lahetysAlkoi) {
		this.lahetysAlkoi = lahetysAlkoi;
	}

	public Date getLahetysPaattyi() {
		return lahetysPaattyi;
	}

	public void setLahetysPaattyi(Date lahetysPaattyi) {
		this.lahetysPaattyi = lahetysPaattyi;
	}

	@Override
	public String toString() {
		return "RaportoitavaViesti [prosessi=" + prosessi + ", lahettajanOid=" + lahettajanOid
			+ ", lahettajanSahkopostiosoite=" + lahettajanSahkopostiosoite + ", vastauksensaajanOid="
			+ vastauksensaajanOid + ", vastauksensaajanSahkopostiosoite=" + vastauksensaajanSahkopostiosoite
			+ ", aihe=" + aihe + ", viesti=" + Arrays.toString(viesti) + ", raportoitavatVastaanottajat="
			+ raportoitavatVastaanottajat + ", raportoitavatLiitteet=" + raportoitavatLiitteet + ", lahetysAlkoi="
			+ lahetysAlkoi + ", lahetysPaattyi=" + lahetysPaattyi + ", getId()=" + getId() + "]";
	}
}
