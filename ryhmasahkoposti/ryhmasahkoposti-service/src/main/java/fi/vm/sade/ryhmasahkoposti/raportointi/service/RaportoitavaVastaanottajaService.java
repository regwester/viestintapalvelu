package fi.vm.sade.ryhmasahkoposti.raportointi.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.raportointi.dto.LahetettyVastaanottajalleDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaVastaanottaja;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaViesti;

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
	 * Muodostaa raprtoitavan viestin vastaanottajan tiedot liittymältä saaduista tiedoista 
	 * 
	 * @param lahetettyVastaanottajalle Liittymältä saadut tiedot viestin lähettämisestä vastaanottajalle {@link LahetettyVastaanottajalleDTO}
	 * @return Raportoitavan viestin vastaanottajan tiedot {@link RaportoitavaVastaanottaja}
	 */
	public RaportoitavaVastaanottaja muodostaRaportoitavaVastaanottaja(
		LahetettyVastaanottajalleDTO lahetettyVastaanottajalle);
	
	/**
	 * Muodostaa raprtoitavan viestin vastaanottajien tiedot liittymältä saaduista tiedoista 
	 * @param raportoitavaViesti TODO
	 * @param lahetettyVastaanottajalle Lista liittymältä saadut tiedot viestin lähettämisestä vastaanottajalle {@link LahetettyVastaanottajalleDTO}
	 * 
	 * @return Lista raportoitavan viestin vastaanottajan tietoja {@link RaportoitavaVastaanottaja}
	 */
	public List<RaportoitavaVastaanottaja> muodostaRaportoitavatVastaanottajat(
		RaportoitavaViesti raportoitavaViesti, List<LahetettyVastaanottajalleDTO> lahetettyVastaanottajille);
	
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
	public void tallennaRaportoitavatVastaanottajat(List<RaportoitavaVastaanottaja> raportoitavatVastaanottajat);
}
