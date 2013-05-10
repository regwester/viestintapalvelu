package fi.vm.sade.viestintapalvelu;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ViestintapalveluModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(PDFService.class);
		bind(PDFDownloadResource.class);
		bind(AddressLabelResource.class);

		serve("/api/*").with(GuiceContainer.class);
	}
}
