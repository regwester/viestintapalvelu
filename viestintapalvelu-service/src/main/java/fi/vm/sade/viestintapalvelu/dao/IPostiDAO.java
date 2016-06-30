/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
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
    List<IPosti> findUnSent();

    /**
     * Hakee iPostien lähetystiedot kirjelähetyksen tunnuksella
     * 
     * @param id Kirjelähetyksen tunnus
     * @return Lista IPosti-luokan ilmentymiä
     */
    List<IPosti> findMailById(Long id);

    /**
     * Hakee iPostien lähetystiedot kirjelähetyksen tunnuksella
     * 
     * @param id Kirjelähetyksen tunnus
     * @return Lista IPosti-luokan ilmentymiä ilman binaari sisältöä
     */
    List<IPosti> findByLetterBatchId(Long id);
    
    int markAsSent(Long id, Long version);
}
