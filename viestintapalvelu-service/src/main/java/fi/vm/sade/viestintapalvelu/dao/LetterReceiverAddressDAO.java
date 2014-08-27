package fi.vm.sade.viestintapalvelu.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverAddress;

/**
 * Rajapinta vastaanottajan osoitetietojen tietokantakäsittelyä varten
 * 
 * @author vehei1
 *
 */
public interface LetterReceiverAddressDAO extends JpaDAO<LetterReceiverAddress, Long> {
    /**
     * Hakee vastaanottajien osoitetietoja vastaanottajien tunnuksilla
     *  
     * @param letterReceiverIDs Lista vastaanottajien tunnuksia
     * @param pagingAndSorting Sivutus- ja lajittelutiedot
     * @return Lista vastaanottajien osoitetietoja
     */
    public List<LetterReceiverAddress> getLetterReceiverAddressesByLetterReceiverID(List<Long> letterReceiverIDs, 
        PagingAndSortingDTO pagingAndSorting);
}
