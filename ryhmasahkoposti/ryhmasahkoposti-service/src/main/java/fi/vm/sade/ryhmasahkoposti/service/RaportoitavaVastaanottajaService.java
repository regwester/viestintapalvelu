package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;
import java.util.Set;

import fi.vm.sade.ryhmasahkoposti.api.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;

/**
 * Rajapinta raportoittavien viestin vastaanottajien liiketoimintalogiikkaa varten
 * 
 * @author vehei1
 *
 */
public interface RaportoitavaVastaanottajaService {
	/**
	 * Hakee kaikkien raportoittavien viestin vastaanottajien tiedot
	 * 
	 * @return Lista raportoittavien viestin vastaanottajien tietoja {@link RaportoitavaVastaanottaja}
	 */
	public List<RaportoitavaVastaanottaja> haeRaportoitavatVastaanottajat();

	/**
	 * Hakee raportoitavan viestin vastaanottajien tiedot, joille viesti on lähettämättä
	 * 
	 * @param vastaanottajienLukumaara Palautettavien vasttanottajien lukumaara
	 * @return Lista raportoitavan viestin vastaanottajien tietoja {@link RaportoitavaVastaanottaja}
	 */
	public List<RaportoitavaVastaanottaja> haeRaportoitavatVastaanottajatViestiLahettamatta(int vastaanottajienLukumaara);

	/**
	 * Hakee raportoitavan viestin vastaanottajan tiedot
	 * 
	 * @param id Raportoitavan viestin vastaanottajan tunnus
	 * @return Raportoitavan viestin vastaanottajan tiedot {@link RaportoitavaVastaanottaja}
	 */
	public RaportoitavaVastaanottaja haeRaportoitavaVastaanottaja(Long id);
	
	/**
	 * Hakee raportoitavan viestin vastaanottajan tiedot viestin tunnuksella ja vastaanottajan sähköpostiosoitteella
	 * 
	 * @param viestiID Raportoitavan viestin tunnus
	 * @param vastaanottajanSahkopostiosoite Viestin vastaanottajan sähköpostiosoite
	 * @return Raportoitavan viestin vastaanottajan tiedot {@link RaportoitavaVastaanottaja}
	 */
	public RaportoitavaVastaanottaja haeRaportoitavaVastaanottaja(Long viestiID, String vastaanottajanSahkopostiosoite);
	
	/**
	 * Hakee viestin vastaanottajien kokonaislukumäärän
	 * 
	 * @param viestiID Raportoitavan viestin tunnus
	 * @return Viestin vastaanottajien kokonaislukumäärä
	 */
	public Long haeRaportoitavienVastaanottajienLukumaara(Long viestiID);
	
	/**
	 * Hakee epäoonnistuneiden tai onnistuneiden viestin vastaanottajien lukumäärän
	 *   
	 * @param viestiID Raportoitavan viestin tunnus
	 * @param lahetysOnnistui true, jos halutaan hakea onnistuneet viestin lähetykset, false haetaan epäonnistuneet
	 * @return Epäonnistuneiden tai onnistuneiden viestien vastaanottajien lukumäärä
	 */
	public Long haeRaportoitavienVastaanottajienLukumaara(Long viestiID, boolean lahetysOnnistui);	
	
	/**
	 * Täydentää raportoitavan viestin vastaanottajan tietoja liittymältä saaduilla tiedoilla
	 *   
	 * @param raportoitavaVastaanottaja Raportoitavan viestin vastaanottajan tiedot {@link RaportoitavaVastaanottaja}
	 * @param lahetettyVastaanottajalle Liittymältä saadut tiedot viestin lähettämisestä vastaanottajalle {@link LahetettyVastaanottajalleDTO}
	 * @return Liittymältä saaduilla tiedoilla täydennetyt raportoitavan viestin vastaanottajan tiedot
	 */
	public RaportoitavaVastaanottaja taydennaRaportoitavaaVastaanottajaa(
		RaportoitavaVastaanottaja raportoitavaVastaanottaja, LahetettyVastaanottajalleDTO lahetettyVastaanottajalle);
	
	/**
	 * Päivittää raportoitavan viestin vastaanottajan tiedot 
	 * 
	 * @param raportoitavaVastaanottaja Raportoitavan viestin vastaanottajan tiedot
	 */
	public void paivitaRaportoitavaVastaanottaja(RaportoitavaVastaanottaja raportoitavaVastaanottaja);

	/**
	 * Tallentaa raportoitavan viestin vastaanottajien tiedot 
	 * 
	 * @param raportoitavatVastaanottajat Lista raportoitavan viestin vastaanottajien tietoja
	 */
	public void tallennaRaportoitavatVastaanottajat(Set<RaportoitavaVastaanottaja> raportoitavatVastaanottajat);
}
