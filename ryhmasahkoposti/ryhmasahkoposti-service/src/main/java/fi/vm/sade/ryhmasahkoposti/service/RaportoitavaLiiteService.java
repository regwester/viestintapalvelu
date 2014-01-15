package fi.vm.sade.ryhmasahkoposti.service;

import java.io.IOException;
import java.util.List;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyLiiteDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;

public interface RaportoitavaLiiteService {
	/**
	 * Muodostaa kutsujalta saaduista liitetiedoista tietokantaan tallennettavat raportoitavat liitteet
	 * 
	 * @param tallennettuRaportoitavaViesti Tietokantaan tallennetun ryhmäsähköpostin tiedot
	 * @param lahetetynviestinliitteet Lista lähetetyn sähköpostiin liitetiedot
	 * @return Lista raportoittavien liitteiden tietoja {@link RaportoitavaLiite}
	 * @throws IOException
	 */
	public List<RaportoitavaLiite> muodostaRaportoitavatLiitteet(RaportoitavaViesti tallennettuRaportoitavaViesti,
		List<LahetettyLiiteDTO> lahetetynviestinliitteet) throws IOException;

	/**
	 * Tallentaa ryhmäsähköpostin raportoitavat liitteet
	 * 
	 * @param Lista raportoittavien liitteiden tietoja {@link RaportoitavaLiite}
	 */
	public void tallennaRaportoitavatLiitteet(List<RaportoitavaLiite> raportoitavatLiitteet);
}
