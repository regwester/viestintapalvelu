package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;
import java.util.Set;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaLiite;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavanViestinLiite;

public interface RaportoitavaLiiteService {
	/**
	 * Hakee raportoitavat liitteet aiemmin tallennetujen liitteiden tietojen perusteella
	 * 
	 * @param lahetetytLiitteet Kokoelma lähetettävien liitteiden tietoja
	 * @return Kokoelma raportoitavia liitteitä
	 */
	public List<RaportoitavaLiite> haeRaportoitavatLiitteet(List<AttachmentResponse> attachmentResponses); 

	/**
	 * Hakee raportoitavat liitteet aiemmin tallennetujen liitteiden tietojen perusteella
	 * 
	 * @param viestinLiitteet Kokoelma lähetettävien viestin liitteiden avaintietoja
	 * @return Kokoelma raportoitavia liitteitä
	 */
	public List<RaportoitavaLiite> haeRaportoitavanViestinLiitteet(Set<RaportoitavanViestinLiite> viestinLiitteet); 

	/**
	 * Tallentaa ryhmäsähköpostin raportoitavat liitteet
	 * 
	 * @param Raportoitavan liitteen tiedot {@link RaportoitavaLiite}
	 * @return Liitteen generoitu avain
	 */
	public Long tallennaRaportoitavaLiite(RaportoitavaLiite raportoitavaLiite);
}
