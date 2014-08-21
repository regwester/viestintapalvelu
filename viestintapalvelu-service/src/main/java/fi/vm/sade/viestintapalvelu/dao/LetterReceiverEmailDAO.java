package fi.vm.sade.viestintapalvelu.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverEmail;

public interface LetterReceiverEmailDAO extends JpaDAO<LetterReceiverEmail, Long> {
    /**
     * Hakee vastaanottajien osoitetietoja vastaanottajien tunnuksilla
     *  
     * @param letterReceiverIDs Lista vastaanottajien tunnuksia
     * @param pagingAndSorting Sivutus- ja lajittelutiedot
     * @return Lista vastaanottajien osoitetietoja
     */
    public List<LetterReceiverEmail> getLetterReceiverEmailByLetterReceiverID(List<Long> letterReceiverIDs, 
        PagingAndSortingDTO pagingAndSorting);
    
}
