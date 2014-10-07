package fi.vm.sade.viestintapalvelu.validator;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.letter.Letter;
import fi.vm.sade.viestintapalvelu.letter.LetterBatch;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterBatchDetails;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;


public class LetterBatchValidatorTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionIfGivenNullLetterBatch() throws Exception {
        LetterBatchValidator.validate((LetterBatchDetails)null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfFirstNameIsNull() throws Exception {
        LetterBatchValidator.validate(givenBatchWithLetters(givenLetterWithAddressLabelWithNullField("firstName")));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfSurnameIsNull() throws Exception {
        LetterBatchValidator.validate(givenBatchWithLetters(givenLetterWithAddressLabelWithNullField("lastName")));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfStreetAddressIsNull() throws Exception {
        LetterBatchValidator.validate(givenBatchWithLetters(givenLetterWithAddressLabelWithNullField("addressline")));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfPostOfficeIsNull() throws Exception {
        LetterBatchValidator.validate(givenBatchWithLetters(givenLetterWithAddressLabelWithNullField("city")));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionIfPostalCodeIsNull() throws Exception {
        LetterBatchValidator.validate(givenBatchWithLetters(givenLetterWithAddressLabelWithNullField("postalCode")));
    }
    
    @Test
    public void returnsFalseForInvalidLetterBatch() {
        assertFalse(LetterBatchValidator.isValid(null));
    }
    
    @Test
    public void returnsTrueForValidLetterBatch() {
        assertTrue(LetterBatchValidator.isValid(DocumentProviderTestData.getAsyncLetterBatch()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionForMissingTemplateName() throws Exception {
        LetterBatchValidator.validate(givenBatchWithLettersAndWithoutTemplate());
    }
    
    
    private AddressLabel givenAddressLabelWithNullField(String fieldName) throws Exception {
        AddressLabel label = DocumentProviderTestData.getAddressLabel();
        Field field = label.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(label, null);
        return label;
    }
    
    private Letter givenLetterWithAddressLabelWithNullField(String fieldName) throws Exception {
        Letter letter = new Letter();
        letter.setAddressLabel(givenAddressLabelWithNullField(fieldName));
        return letter;
    }
    
    private LetterBatch givenBatchWithLetters(Letter ... letters) { 
        LetterBatch batch = givenBatchWithLettersAndWithoutTemplate(letters);
        batch.setTemplateName("template");
        batch.setLanguageCode("358");
        return batch;
    }

    private LetterBatch givenBatchWithLettersAndWithoutTemplate(Letter... letters) {
        LetterBatch batch = new LetterBatch();
        batch.setLetters(Arrays.asList(letters));
        return batch;
    }
    
}
