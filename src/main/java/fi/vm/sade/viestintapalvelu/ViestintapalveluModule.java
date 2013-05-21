package fi.vm.sade.viestintapalvelu;

import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import fi.vm.sade.viestintapalvelu.address.AddressLabelResource;
import fi.vm.sade.viestintapalvelu.download.DownloadResource;
import fi.vm.sade.viestintapalvelu.hyvaksymiskirje.HyvaksymiskirjeResource;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.JalkiohjauskirjeResource;

public class ViestintapalveluModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(DownloadResource.class);
		bind(AddressLabelResource.class);
		bind(JalkiohjauskirjeResource.class);
		bind(HyvaksymiskirjeResource.class);

		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");

		serve("/" + Urls.API_PATH + "/*").with(GuiceContainer.class,
				initParameters);
	}
}
