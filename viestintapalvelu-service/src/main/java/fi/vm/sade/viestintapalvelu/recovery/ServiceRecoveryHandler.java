package fi.vm.sade.viestintapalvelu.recovery;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Singleton
@Component
public class ServiceRecoveryHandler {
    
    private final List<Recoverer> recoverers = new ArrayList<Recoverer>();
    
    @Autowired
    public ServiceRecoveryHandler(LetterPDFRecoverer letterPDFRecoverer) {
        recoverers.add(letterPDFRecoverer);
    }
    
    public void recover() {
        for (Recoverer recoverer : recoverers) {
            recoverer.recover();
        }
    }
}
