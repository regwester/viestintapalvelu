package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyLiiteDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;

public interface RaportoitavaLiiteService {
	/**
	 * Hakee raportoitavat liitteet 
	 * 
	 * @param liiteidenIDt Lista raportoitavien liitteiden tunnuksia
	 * @return Lista raportoitavia liitteitä
	 */
	public List<RaportoitavaLiite> haeRaportoitavatLiitteet(List<LahetettyLiiteDTO> lahetetytLiitteet); 
	
	/**
	 * Tallentaa ryhmäsähköpostin raportoitavat liitteet
	 * 
	 * @param Raportoitavan liitteen tiedot {@link RaportoitavaLiite}
	 * @return Liitteen generoitu avain
	 */
	public Long tallennaRaportoitavaLiite(RaportoitavaLiite raportoitavaLiite);
}
