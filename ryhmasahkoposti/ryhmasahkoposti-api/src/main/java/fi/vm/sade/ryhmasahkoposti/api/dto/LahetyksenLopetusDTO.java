package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;
import java.util.Date;

public class LahetyksenLopetusDTO implements Serializable {
	private static final long serialVersionUID = -3156886814655451758L;

	private Long viestiID;
	private Date lahetysPaattyi;

	public Long getViestiID() {
		return viestiID;
	}
	
	public void setViestiID(Long viestiID) {
		this.viestiID = viestiID;
	}
	
	public Date getLahetysPaattyi() {
		return lahetysPaattyi;
	}
	
	public void setLahetysPaattyi(Date lahetysPaattyi) {
		this.lahetysPaattyi = lahetysPaattyi;
	}
}
