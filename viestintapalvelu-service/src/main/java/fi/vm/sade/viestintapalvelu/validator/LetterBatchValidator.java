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
     * @return <code>true</code> mikäli kaikki on kunnossa.
     */
    public static boolean validate(LetterBatch letters) throws Exception {
        if (letters != null) {
            for (Letter letter : letters.getLetters()) {
                isValid(letter);
            }
        }
        return true;
    }

    private static boolean isValid(Letter letter) throws Exception {
        if (letter != null) {
            isValid(letter.getAddressLabel());
        }
        return true;
    }

    private static boolean isValid(AddressLabel address) throws Exception {
        if (address != null) {
            if (address.getPostalCode().length() > 10) {
                throw new Exception("Viallinen postinumero " + address);
            }
        }
        return true;
    }
}
