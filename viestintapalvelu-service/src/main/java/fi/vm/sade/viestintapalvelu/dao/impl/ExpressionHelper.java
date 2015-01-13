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

import static com.mysema.query.types.expr.BooleanExpression.anyOf;

import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.types.expr.BooleanExpression;

import fi.vm.sade.viestintapalvelu.model.QLetterReceiverAddress;
import fi.vm.sade.viestintapalvelu.model.QLetterReceiverReplacement;
import fi.vm.sade.viestintapalvelu.model.QLetterReceivers;

public class ExpressionHelper {

    static String[] words(String searchArgument) {
        return searchArgument.trim().split("\\s+");
    }

    static BooleanExpression anyOfReceiverAddressFieldsContains(QLetterReceiverAddress letterReceiverAddress, String word) {
        return anyOf(
                letterReceiverAddress.firstName.trim().containsIgnoreCase(word),
                letterReceiverAddress.lastName.trim().containsIgnoreCase(word),
                letterReceiverAddress.addressline.trim().containsIgnoreCase(word),
                letterReceiverAddress.addressline2.trim().containsIgnoreCase(word),
                letterReceiverAddress.addressline3.trim().containsIgnoreCase(word),
                letterReceiverAddress.city.trim().containsIgnoreCase(word),
                letterReceiverAddress.postalCode.trim().contains(word)
        );
    }

    static BooleanExpression receiverReplacementsContain(QLetterReceivers letterReceivers, String word, int subQueryNumber) {
        QLetterReceivers subQueryLetterReceiver = new QLetterReceivers("sqLetterReceiver_"+subQueryNumber);
        QLetterReceiverReplacement replacement = new QLetterReceiverReplacement("sqLetterReceiverReplacement_"+subQueryNumber);
        return new JPASubQuery().from(subQueryLetterReceiver)
                .innerJoin(subQueryLetterReceiver.letterReceiverReplacement, replacement)
                .where(subQueryLetterReceiver.id.eq(letterReceivers.id)
                        .andAnyOf(
                                replacement.defaultValue.containsIgnoreCase(word),
                                replacement.jsonValue.containsIgnoreCase(word)
                        )
                ).exists();
    }

}
