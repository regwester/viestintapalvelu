package fi.vm.sade.viestintapalvelu.recovery;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Singleton;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Singleton
@Component
public class ServiceRecoveryHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRecoveryHandler.class);

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

    @PostConstruct
    public void initialize() {
        //noinspection unchecked
        Collections.sort(this.recoverers, new ReverseComparator(new Comparator<Recoverer>() {
            @Override
            public int compare(Recoverer o1, Recoverer o2) {
                return getPriority(o1).compareTo(getPriority(o2));
            }
            private Integer getPriority(Recoverer recoverer) {
                RecovererPriority priority = recoverer.getClass().getAnnotation(RecovererPriority.class);
                if (priority != null) {
                    return priority.value();
                } else {
                    LOGGER.warn("Recovery bean class {} does not have RecovererPriority.",
                            recoverer.getClass().getCanonicalName());
                }
                return 0;
            }
        }));
    }
    
    public void recover() {
        for (Recoverer recoverer : recoverers) {
            executor.execute(recoverer.getTask());
        }
    }
}
