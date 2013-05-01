package fi.vm.sade.viestintapalvelu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class GuiceServletConfiguration extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		System.out.println("getInjector");
		return Guice.createInjector(Stage.TOOL, new JerseyServletModule() {
			@Override
			protected void configureServlets() {

				System.out.println("configureServlets");

				bind(GuiceFilter.class);

				bind(GuiceContainer.class);

				bind(IPDFService.class).toInstance(new PDFService());

				bind(ViestintapalveluApplication.class);

				bind(SpikeResource.class);

				/*
				 * * &lt;init-param&gt;
				 * &lt;param-name&gt;com.sun.jersey.spi.container
				 * .ContainerRequestFilters&lt;/param-name&gt;
				 * &lt;param-value&gt
				 * ;com.sun.jersey.api.container.filter.LoggingFilter
				 * &lt;/param-value&gt; &lt;/init-param&gt &lt;init-param&gt
				 * &lt;param-name&gt;com.sun.jersey.spi.container.
				 * ContainerResponseFilters&lt;/param-name&gt;
				 * &lt;param-value&gt
				 * ;com.sun.jersey.api.container.filter.LoggingFilter
				 * &lt;/param-value&gt; &lt;/init-param&gt;
				 */

				Map<String, String> jerseyParameters = new HashMap<String, String>();
				jerseyParameters.put(ResourceConfig.FEATURE_TRACE,
						Boolean.TRUE.toString());
				jerseyParameters.put(ResourceConfig.FEATURE_TRACE_PER_REQUEST,
						Boolean.TRUE.toString());
				jerseyParameters.put(
						"com.sun.jersey.spi.container.ContainerRequestFilters",
						"com.sun.jersey.api.container.filter.LoggingFilter");
				jerseyParameters
						.put("com.sun.jersey.spi.container.ContainerResponseFilters",
								"com.sun.jersey.api.container.filter.LoggingFilter");

				filter("/*").through(new GuiceFilter() {
					@Override
					public void doFilter(ServletRequest servletRequest,
							ServletResponse servletResponse,
							FilterChain filterChain) throws IOException,
							ServletException {
						System.out.println("GuiceFilter.doFilter");
						super.doFilter(servletRequest, servletResponse,
								filterChain);
					}
				});
				serve("/*").with(GuiceContainer.class, jerseyParameters);

			}
		});
	}
}
