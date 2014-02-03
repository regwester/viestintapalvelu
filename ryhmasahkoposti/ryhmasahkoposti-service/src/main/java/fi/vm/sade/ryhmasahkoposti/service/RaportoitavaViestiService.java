package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.api.dto.query.EmailMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaViesti;

public interface RaportoitavaViestiService {
	/**
	 * Hakee käyttäjän ja käyttäjän käyttöoikeusryhmien kaikki raportoitavat viestit
	 * 
	 * @param Käyttäjätunnus
	 * @return Lista raportoitavia viestejä {@link RaportoitavaViesti} 
	 */	
	public List<RaportoitavaViesti> haeRaportoitavatViestit();

	/**
	 * Hakee raportoitavan viestin viestin avaimella
	 * 
	 * @param id Raportoitavan viestin avain
	 * @return Raportoitavan viestin tiedot {@link RaportoitavaViesti}
	 */
	public RaportoitavaViesti haeRaportoitavaViesti(Long id);

	/**
	 * Hakee annettujen parametrien mukaiset ryhmäsähköpostin raportoitavat viestit
	 * 
	 * @param query Hakuparametrit
	 * @return Lists raportoitavia viestejä {@link RaportoitavaViesti}
	 */
	public List<RaportoitavaViesti> haeRaportoitavatViestit(EmailMessageQueryDTO query);
	
	/**
	 * Päivittä raportoitavan viestin tietoja tietokantaan
	 * @param raportoitavaViesti Päivittetävä raportoitava viesti
	 */
	public void paivitaRaportoitavaViesti(RaportoitavaViesti raportoitavaViesti);
	
	/**
	 * Tallentaa raportoitvan ryhmäsähköpostin viestin
	 * 
	 * @param raportoitavaViesti Ryhmäsähköpostin raportoitavaviesti
	 * @return Tallennetu raportoitava viesti
	 */
	public RaportoitavaViesti tallennaRaportoitavaViesti(RaportoitavaViesti raportoitavaViesti);
}
