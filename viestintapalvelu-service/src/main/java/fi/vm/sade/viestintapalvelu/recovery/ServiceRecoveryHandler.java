/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
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

    @SuppressWarnings("unchecked")
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
