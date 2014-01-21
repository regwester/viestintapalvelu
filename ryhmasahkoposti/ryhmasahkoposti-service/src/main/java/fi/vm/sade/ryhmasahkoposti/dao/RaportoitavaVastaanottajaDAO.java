package fi.vm.sade.ryhmasahkoposti.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.model.RaportoitavaVastaanottaja;

/**
 * Rajapinta raportoitavien ryhmäsähköpostiviestin vastaanottajien tietokantakäsittelyyn
 * 
 * @author vehei1
 *
 */
public interface RaportoitavaVastaanottajaDAO extends JpaDAO<RaportoitavaVastaanottaja, Long> {
	/**
	 * Hakee vastaanottajan tiedot viestintunnuksella ja sähköpostiosoitteella
	 * 
	 * @param viestiID Viestintunnus
	 * @param vastaanottajanSahkopostiosoite Vastaanottajansähköpostiosoite
	 * @return Raportoitavan vastaanottajan tiedot
	 */
	public RaportoitavaVastaanottaja findByLahetettyviestiIdAndVastaanottajanSahkopostiosoite(Long viestiID,
		String vastaanottajanSahkopostiosoite);

	/**
	 * Hakee raportoitavat vastaanottajat, joille viestiä ei ole lähetetty 
	 *  
	 * @return Lista raportoitavan vastaanottajien tietoja
	 */	
	public List<RaportoitavaVastaanottaja> findLahettamattomat();
	
	/**
	 * Hakee vastaanottajien lukumäärän viestintunnuksella
	 * 
	 * @param viestiID Viestintunnus
	 * @return Viestin vastaanottajien lukumäärän
	 */
	public Long findVastaanottajienLukumaaraByViestiID(Long viestiID);
	
	/**
	 * Hakee vastaanottajien lukumäärän viestintunnuksella, joille viestin lähetys on onnistunut tai epäonnistunut 
	 * 
	 * @param viestiID Viestintunnus
	 * @param lahetysonnistui true, jos halutaan hakea onnistuneet lähetykset, muuten false
	 * @return Viestin vastaanottajien lukumäärän
	 */
	public Long findVastaanottajienLukumaaraByViestiIdJaLahetysonnistui(Long viestiID, boolean lahetysonnistui);
}
