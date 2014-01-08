package fi.vm.sade.viestintapalvelu;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import fi.vm.sade.viestintapalvelu.address.AddressLabelResource;
import fi.vm.sade.viestintapalvelu.download.DownloadResource;
import fi.vm.sade.viestintapalvelu.hyvaksymiskirje.HyvaksymiskirjeResource;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.JalkiohjauskirjeResource;
import fi.vm.sade.viestintapalvelu.message.MessageResource;

public class ViestintapaveluConfiguration extends ResourceConfig {

    public ViestintapaveluConfiguration() {
        register(JacksonFeature.class);
        register(MessageResource.class);
        register(DownloadResource.class);
        register(AddressLabelResource.class);
        register(JalkiohjauskirjeResource.class);
        register(HyvaksymiskirjeResource.class);
    }
}
