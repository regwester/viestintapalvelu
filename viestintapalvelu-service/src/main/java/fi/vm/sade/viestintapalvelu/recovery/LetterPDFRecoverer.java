package fi.vm.sade.viestintapalvelu.recovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.letter.LetterBatchPDFProcessor;
import fi.vm.sade.viestintapalvelu.letter.LetterService;

@Component
public class LetterPDFRecoverer implements Recoverer {
    
    @Autowired
    private LetterBatchPDFProcessor letterPDFProcessor;
    
    @Autowired
    private LetterService letterService;
    
    @Override
    public void recover() {
        //execute these in thread?
        //get letterbatches that require processing from letterService
        //use PDFprocessor to handle batches
        //error handling
    }

}
