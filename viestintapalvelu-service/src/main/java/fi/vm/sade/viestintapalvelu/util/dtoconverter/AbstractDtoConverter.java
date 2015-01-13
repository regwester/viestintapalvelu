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
package fi.vm.sade.viestintapalvelu.util.dtoconverter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

import fi.ratamaa.dtoconverter.AbstractAllInclusiveDtoConveter;
import fi.ratamaa.dtoconverter.typeconverter.TypeConversionContainer;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 9:48
 */
public class AbstractDtoConverter extends AbstractAllInclusiveDtoConveter {
    private EntityManager entityManager;

    @Override
    protected void registerConverters(TypeConversionContainer conversions) {
        super.registerConverters(conversions);
        conversions.add(new XmlDateTypeConversions());
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

}
