package fi.vm.sade.viestintapalvelu.validator;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.letter.Letter;
import fi.vm.sade.viestintapalvelu.letter.LetterBatch;

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
    public static void validate(LetterBatch letters) throws Exception {
        if (letters != null) {
            for (Letter letter : letters.getLetters()) {
                isValid(letter);
            }
        }
    }

    private static void isValid(Letter letter) throws Exception {
        if (letter != null) {
            isValid(letter.getAddressLabel());
        }
    }

    private static void isValid(AddressLabel address) throws Exception {
        if (address != null) {
            if (address.getPostalCode().length() > 10) {
                throw new IllegalArgumentException("Viallinen postinumero " + address);
            }
        }
    }
}
