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
