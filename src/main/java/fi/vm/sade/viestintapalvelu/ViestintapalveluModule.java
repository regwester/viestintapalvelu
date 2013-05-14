package fi.vm.sade.viestintapalvelu;

import com.google.inject.Inject;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ViestintapalveluModule extends JerseyServletModule {

	@Inject
	private ResourceConfig rc;

	@Override
	protected void configureServlets() {
		bind(PDFService.class);
		bind(DownloadResource.class);
		bind(AddressLabelResource.class);
		bind(JalkiohjauskirjeResource.class);
		bind(GuiceContainer.class);

		serve("/*").with(GuiceContainer.class);

		System.out.println("CONFIGURE SERVLETS END");
		System.out.println("RC: " + rc);
	}

}
