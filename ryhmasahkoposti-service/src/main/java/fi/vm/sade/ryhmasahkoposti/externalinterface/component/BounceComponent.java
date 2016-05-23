package fi.vm.sade.ryhmasahkoposti.externalinterface.component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailBounce;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailBounces;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.BounceResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class BounceComponent {
    private static Logger LOGGER = LoggerFactory.getLogger(BounceComponent.class);
    @Resource
    private BounceResource bounceResourceClient;

    public EmailBounces getBounces() {
        return bounceResourceClient.getBounces();
    }
}
