package fi.vm.sade.ryhmasahkoposti.raportointi.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.ryhmasahkoposti.raportointi.dto.query.RyhmasahkopostiVastaanottajaQueryDTO;
import fi.vm.sade.ryhmasahkoposti.raportointi.model.RaportoitavaVastaanottaja;

public interface RaportoitavaVastaanottajaDAO extends JpaDAO<RaportoitavaVastaanottaja, Long> {
	public RaportoitavaVastaanottaja findByLahetettyviestiIdAndVastaanottajanSahkopostiosoite(Long viestiID,
		String vastaanottajanSahkopostiosoite);
	public List<RaportoitavaVastaanottaja> findBySearchCriterias(RyhmasahkopostiVastaanottajaQueryDTO query);
	public Long findVastaanottajienLukumaaraByViestiID(Long viestiID);
	public Long findVastaanottajienLukumaaraByViestiIdJaLahetysonnistui(Long viestiID, boolean lahetysonnistui);
}
