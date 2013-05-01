package fi.vm.sade.viestintapalvelu;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ViestintapalveluModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(GuiceFilter.class);
		bind(GuiceContainer.class);
		bind(IPDFService.class).toInstance(new PDFService());
		bind(SpikeResource.class);

		Map<String, String> jerseyParameters = new HashMap<String, String>();
		jerseyParameters.put("com.sun.jersey.config.feature.Trace", "true");
		jerseyParameters.put("com.sun.jersey.config.feature.TracePerRequest",
				"true");
		jerseyParameters.put(
				"com.sun.jersey.spi.container.ContainerRequestFilters",
				"com.sun.jersey.api.container.filter.LoggingFilter");
		jerseyParameters.put(
				"com.sun.jersey.spi.container.ContainerResponseFilters",
				"com.sun.jersey.api.container.filter.LoggingFilter");

		filter("/*").through(GuiceFilter.class);
		serve("/*").with(GuiceContainer.class, jerseyParameters);
	}
}
