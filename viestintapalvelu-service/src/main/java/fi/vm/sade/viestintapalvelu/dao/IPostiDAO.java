package fi.vm.sade.viestintapalvelu.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.model.IPosti;

/**
 * Rajapinta iPosti-lähetysten tietokantakäsittelyä varten
 * 
 * @author vehei1
 *
 */
public interface IPostiDAO extends JpaDAO<IPosti, Long>{
    /**
     * Hakee iPosti-tiedot, joita ei ole vielä lähetetty
     * 
     * @return Lista IPosti-luokan ilmentymiä
     */
    public List<IPosti> findUnSent();

    /**
     * Hakee iPostien lähetystiedot kirjelähetyksen tunnuksella
     * 
     * @param id Kirjelähetyksen tunnus
     * @return Lista IPosti-luokan ilmentymiä
     */
    public List<IPosti> findMailById(Long id);

    /**
     * Hakee iPostien lähetystiedot kirjelähetyksen tunnuksella
     * 
     * @param id Kirjelähetyksen tunnus
     * @return Lista IPosti-luokan ilmentymiä ilman binaari sisältöä
     */
    public List<IPosti> findByLetterBatchId(Long id);
    
    public int markAsSent(IPosti iposti);
}
