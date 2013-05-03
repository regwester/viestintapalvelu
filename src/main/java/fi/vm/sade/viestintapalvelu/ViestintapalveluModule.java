package fi.vm.sade.viestintapalvelu;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ViestintapalveluModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(PDFService.class);
		bind(SpikeResource.class);
		bind(PDFDownloadResource.class);
		bind(AddressLabelResource.class);

		filter("/*").through(GuiceFilter.class);
		serve("/*").with(GuiceContainer.class);
	}
}
