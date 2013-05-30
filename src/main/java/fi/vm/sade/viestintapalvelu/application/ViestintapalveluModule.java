package fi.vm.sade.viestintapalvelu.application;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import fi.vm.sade.viestintapalvelu.ui.AddressLabelResource;
import fi.vm.sade.viestintapalvelu.ui.DownloadResource;
import fi.vm.sade.viestintapalvelu.ui.HyvaksymiskirjeResource;
import fi.vm.sade.viestintapalvelu.ui.JalkiohjauskirjeResource;
import fi.vm.sade.viestintapalvelu.ui.Urls;

public class ViestintapalveluModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(DownloadResource.class);
		bind(AddressLabelResource.class);
		bind(JalkiohjauskirjeResource.class);
		bind(HyvaksymiskirjeResource.class);
		bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());

		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");

		serve("/" + Urls.API_PATH + "/*").with(GuiceContainer.class,
				initParameters);
	}
}
