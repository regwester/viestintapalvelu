package fi.vm.sade.viestintapalvelu.validator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final static Logger LOGGER = LoggerFactory.getLogger(LetterBatchValidator.class);

    /**
     * Validoi annetun <code>LetterBatch</code>:n.
     * 
     * @param letters
     */

    /**
     * Validoi annetun <code>LetterBatch</code>:n.
     * 
     * @param letters
     */
    public static Map<String, String> validate(LetterBatchDetails letters) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        validateLetterBatch(letters);

        for (LetterDetails letter : letters.getLetters()) {
            try {
                validate(letter);
            } catch (Throwable t) {
                String key = null;
                if (letter.getTemplateReplacements() != null) {
                    key = (String)letter.getTemplateReplacements().get("hakemusOid");
                }
                if (key == null) {
                    key = " " + letter.getAddressLabel().getFirstName() + " " +letter.getAddressLabel().getLastName();
                }
                result.put(key, t.getMessage());
            }
        }
        if (result.size() > 0) {
            return result;
        } else {
            return null;
        }
    }

    public static boolean isValid(LetterBatchDetails letters) {
        try {
            validate(letters);
        } catch (Exception e) {
            LOGGER.error("Invalid LetterBatchDetails: " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    private static void validateLetterBatch(LetterBatchDetails letters) {
        if (letters == null) {
            throw new IllegalArgumentException("Letter to be validated was null");
        }
        if (letters.getTemplateId() == null && letters.getTemplate() == null
                && (StringUtils.isBlank(letters.getTemplateName()) || StringUtils.isBlank(letters.getLanguageCode()))) {
            throw new IllegalArgumentException("Invalid template parameters, name of template " + letters.getTemplateName() + ", language code: "
                    + letters.getLanguageCode());
        }
    }

    private static void validate(LetterDetails letter) throws Exception {
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
