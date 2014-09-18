package fi.vm.sade.viestintapalvelu.validator;

import org.apache.commons.lang.StringUtils;

import fi.vm.sade.viestintapalvelu.letter.dto.AddressLabelDetails;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterBatchDetails;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterDetails;

/**
 * Tarkistaa LetterBatchin
 * 
 * @author jkorkala
 *
 */

public class LetterBatchValidator {

    /**
     * Validoi annetun <code>LetterBatch</code>:n.
     * 
     * @param letters
     */
    public static void validate(LetterBatchDetails letters) throws Exception {
        if (letters == null) {
            throw new IllegalArgumentException("Letter to be validated was null");
        }
        for (LetterDetails letter : letters.getLetters()) {
            isValid(letter);
        }
    }

    private static void isValid(LetterDetails letter) throws Exception {
        validateAddress(letter.getAddressLabel());
    }

    private static void validateAddress(AddressLabelDetails address) throws Exception {
        if (address == null) {
            throw new IllegalArgumentException("AddressLabel of the letter to be validated was null");
        }
        if (StringUtils.isBlank(address.getPostalCode()) || address.getPostalCode().length() > 10) {
            throw new IllegalArgumentException("Viallinen postinumero " + address);
        }
        validateAddressLabelField("City", address.getCity());
        validateAddressLabelField("Adressline", address.getAddressline());
        validateAddressLabelField("Firstname", address.getFirstName());
        validateAddressLabelField("Lastname", address.getLastName());
    }

    private static void validateAddressLabelField(String fieldName, String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            throw new IllegalArgumentException(fieldName + " in addresslabel was blank");
        }
    }
}
