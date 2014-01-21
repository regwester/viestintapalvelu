package fi.vm.sade.ryhmasahkoposti.api.dto.query;

public class RaportoitavaViestiQueryDTO {
	private String sanaHaku;
	private RaportoitavaVastaanottajaQueryDTO vastaanottajaQuery;
	
	public String getSanaHaku() {
		return sanaHaku;
	}

	public void setSanaHaku(String sanaHaku) {
		this.sanaHaku = sanaHaku;
	}

	public RaportoitavaVastaanottajaQueryDTO getVastaanottajaQuery() {
		return vastaanottajaQuery;
	}

	public void setVastaanottajaQuery(RaportoitavaVastaanottajaQueryDTO vastaanottajaQuery) {
		this.vastaanottajaQuery = vastaanottajaQuery;
	}	
}
