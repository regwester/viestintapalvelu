package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;
import java.util.Date;

public class LahetyksenTulosDTO implements Serializable {
	private static final long serialVersionUID = 6159889196702219857L;

	private Long viestiID;
	private Date lahetysAlkoi;
	private Date lahetysPaattyi;
	private Long vastaanottajienLukumaara;
	private Long lahetysOnnistuiLukumaara;
	private Long lahetysEpaonnistuiLukumaara;
	
	public Long getViestiID() {
		return viestiID;
	}
	
	public void setViestiID(Long viestiID) {
		this.viestiID = viestiID;
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
	
	public Long getVastaanottajienLukumaara() {
		return vastaanottajienLukumaara;
	}
	
	public void setVastaanottajienLukumaara(Long vastaanottajienLukumaara) {
		this.vastaanottajienLukumaara = vastaanottajienLukumaara;
	}
	
	public Long getLahetysOnnistuiLukumaara() {
		return lahetysOnnistuiLukumaara;
	}
	
	public void setLahetysOnnistuiLukumaara(Long lahetysOnnistuiLukumaara) {
		this.lahetysOnnistuiLukumaara = lahetysOnnistuiLukumaara;
	}
	
	public Long getLahetysEpaonnistuiLukumaara() {
		return lahetysEpaonnistuiLukumaara;
	}
	
	public void setLahetysEpaonnistuiLukumaara(Long lahetysEpaonnistuiLukumaara) {
		this.lahetysEpaonnistuiLukumaara = lahetysEpaonnistuiLukumaara;
	}
}
