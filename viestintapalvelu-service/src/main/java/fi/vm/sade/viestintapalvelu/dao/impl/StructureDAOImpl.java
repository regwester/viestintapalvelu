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
package fi.vm.sade.viestintapalvelu.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.StructureDAO;
import fi.vm.sade.viestintapalvelu.dao.dto.StructureListDto;
import fi.vm.sade.viestintapalvelu.model.Structure;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 13:19
 */
@Repository
public class StructureDAOImpl extends AbstractJpaDAOImpl<Structure,Long> implements StructureDAO {

    @Override
    public List<StructureListDto> findLatestStructuresVersionsForList() {
        return getEntityManager().createQuery("select new fi.vm.sade.viestintapalvelu.dao.dto.StructureListDto(" +
            "  s.id, s.description, s.name, s.language, s.timestamp " +
            ") from Structure s where s.timestamp = (select max(s2.timestamp) from " +
            "       Structure s2 where s2.name = s.name and s2.language = s.language )" +
            " and s.description is not null " +
            " order by s.description, s.timestamp desc, s.id desc", StructureListDto.class)
        .getResultList();
    }

    @Override
    public Optional<Structure> findLatestStructrueByNameAndLanguage(String name, String language) {
        List<Structure> structures = getEntityManager()
                .createQuery("select s from Structure s " +
                        "where s.name = :name and s.language = :language " +
                        "order by s.timestamp desc, s.id desc", Structure.class)
                .setParameter("name", name)
                .setParameter("language", language)
                .setMaxResults(1)
                .getResultList();
        if (structures.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(structures.get(0));
    }

}
