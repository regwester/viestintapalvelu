package fi.vm.sade.viestintapalvelu.externalinterface.component;

import fi.vm.sade.viestintapalvelu.exception.ExternalInterfaceException;
import fi.vm.sade.viestintapalvelu.externalinterface.api.LearningOpportunityProviderResource;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.LOPDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jonimake on 21.11.2014.
 */
@Component
public class LearningOpportunityProviderComponent {

    public static final Logger log = LoggerFactory.getLogger(LearningOpportunityProviderComponent.class);

    @Resource
    LearningOpportunityProviderResource learningOpportunityProviderResourceClient;

    public List<LOPDto> searchProviders(String id, Locale locale) {
        try {
            return learningOpportunityProviderResourceClient.searchProviders("*", id,
                    null, true, true, 0, 10000, locale.getLanguage());
        } catch (Exception e) {
            log.error("Error in external interface", e);
            throw new ExternalInterfaceException(e);
        }
    }
}
