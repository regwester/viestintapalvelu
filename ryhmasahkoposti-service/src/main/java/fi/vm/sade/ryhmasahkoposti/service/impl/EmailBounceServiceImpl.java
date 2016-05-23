package fi.vm.sade.ryhmasahkoposti.service.impl;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailBounce;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailBounces;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.BounceResource;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.BounceComponent;
import fi.vm.sade.ryhmasahkoposti.service.EmailBounceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class EmailBounceServiceImpl implements EmailBounceService {
    private static final Logger log = LoggerFactory.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailBounceServiceImpl.class);

    @Autowired
    private BounceComponent bounceComponent;

    @Override
    public void checkEmailBounces() {
        final EmailBounces bounces = bounceComponent.getBounces();
        System.out.println("Checking email bounces...");
        for (EmailBounce b: bounces.getBouncedEmails()) {
            System.out.println(b);
        }
    }
}
