/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.StyleDAO;
import fi.vm.sade.viestintapalvelu.model.Style;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 10:27
 */
@Repository
public class StyleDAOImpl extends AbstractJpaDAOImpl<Style,Long> implements StyleDAO {

    @Override
    public Optional<Style> findLatestByName(String name) {
        List<Style> styles = getEntityManager().createQuery(
                "select s from Style s where s.name = :name order by s.timestamp desc, s.id desc", Style.class)
                .setParameter("name", name).setMaxResults(1).getResultList();
        if (styles.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(styles.get(0));
    }

}
