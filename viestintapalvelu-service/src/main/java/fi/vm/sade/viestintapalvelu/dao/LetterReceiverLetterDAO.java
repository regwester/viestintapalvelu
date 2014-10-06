package fi.vm.sade.viestintapalvelu.dao;

import java.util.Collection;
import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;

/**
 * Rajapinta vastaanottajien kirjeiden tietokantakäsittelyä varten
 * 
 * @author vehei1
 *
 */
public interface LetterReceiverLetterDAO extends JpaDAO<LetterReceiverLetter, Long> {
    /**
     * Hakee vastaanottajille lähetetyt kirjeet vastaanottajien tunnuksilla
     * 
     * @param letterReceiverIDs Lista vasttanottajien tunnuksia
     * @return Lista vastaanottajille lähetettyjä kirjeitä
     */
    List<LetterReceiverLetter> getLetterReceiverLettersByLetterReceiverID(List<Long> letterReceiverIDs);

    List<Long> findLetterReceiverLetterIdsByLetterReceiverIds(List<Long> letterReceiverIds);

    List<LetterReceiverLetter> findByIds(List<Long> letterRreceiverLetterIds);
}
