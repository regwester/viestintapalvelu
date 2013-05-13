package fi.vm.sade.viestintapalvelu;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ViestintapalveluModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(PDFService.class);
		bind(DownloadResource.class);
		bind(AddressLabelResource.class);
		bind(JalkiohjauskirjeResource.class);

		serve("/api/*").with(GuiceContainer.class);
	}
}
