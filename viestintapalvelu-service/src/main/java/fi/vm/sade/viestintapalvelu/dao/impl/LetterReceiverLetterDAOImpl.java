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

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.QLetterReceiverLetter;

@Repository
public class LetterReceiverLetterDAOImpl extends AbstractJpaDAOImpl<LetterReceiverLetter, Long>
            implements LetterReceiverLetterDAO {

    @Override
    public List<LetterReceiverLetter> getLetterReceiverLettersByLetterReceiverIds(List<Long> letterReceiverIDs) {
        if (letterReceiverIDs.isEmpty()) {
            return new ArrayList<>();
        }
        QLetterReceiverLetter letterReceiverLetter = QLetterReceiverLetter.letterReceiverLetter;

        BooleanExpression whereExpression = letterReceiverLetter.letterReceivers.id.in(letterReceiverIDs);
        JPAQuery findLetterReceiverLetter = from(letterReceiverLetter)
                .where(whereExpression);
        
        return findLetterReceiverLetter.list(letterReceiverLetter);
    }

    protected JPAQuery from(EntityPath<?>... o) {
        return new JPAQuery(getEntityManager()).from(o);
    }
    
}
