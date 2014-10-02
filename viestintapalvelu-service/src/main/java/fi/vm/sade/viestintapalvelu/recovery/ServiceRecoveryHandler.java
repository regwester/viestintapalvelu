package fi.vm.sade.viestintapalvelu.recovery;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;
import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Singleton
@Component
public class ServiceRecoveryHandler {
    @Autowired
    private List<Recoverer> recoverers;

    @Resource(name="recoveryHandlerExecutorService")
    private ExecutorService executor;

    public ServiceRecoveryHandler() {
    }

    public ServiceRecoveryHandler(ExecutorService executor, List<Recoverer> recoverers) {
        this.recoverers = recoverers;
        this.executor = executor;
    }
    
    public void recover() {
        for (Recoverer recoverer : recoverers) {
            executor.execute(recoverer.getTask());
        }
    }
}
