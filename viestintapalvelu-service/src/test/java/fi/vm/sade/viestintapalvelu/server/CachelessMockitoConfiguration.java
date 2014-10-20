package fi.vm.sade.viestintapalvelu.server;

import org.mockito.internal.configuration.GlobalConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CachelessMockitoConfiguration extends GlobalConfiguration {

    private static final long serialVersionUID = -8183166407929499508L;

    @Override
    public boolean enableClassCache() {
        return false;
    }
}
