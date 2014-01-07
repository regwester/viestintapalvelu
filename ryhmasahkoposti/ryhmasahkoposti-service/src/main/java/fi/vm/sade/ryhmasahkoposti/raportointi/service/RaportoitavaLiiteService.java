package fi.vm.sade.ryhmasahkoposti.raportointi.service;

import java.io.IOException;
import java.util.List;

import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetyksenAloitusDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaLiite;

public interface RaportoitavaLiiteService {
	/**
	 * Muodostaa kutsujalta saaduista liitetiedoista tietokantaan tallennettavat raportoitavat liitteet
	 * 
	 * @param lahetyksenAloitus Kutsujan välittämä viestin lähetyksen aloitustiedot
	 * @return Lista raportoittavien liitteiden tietoja {@link RaportoitavaLiite}
	 * @throws IOException
	 */
	public List<RaportoitavaLiite> muodostaRaportoitavatLiitteet(LahetyksenAloitusDTO lahetyksenAloitus) 
		throws IOException;
}
