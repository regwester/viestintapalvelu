package fi.vm.sade.viestintapalvelu;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ViestintapalveluModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		bind(PDFService.class);
		bind(DownloadResource.class);
		bind(AddressLabelResource.class);
		bind(JalkiohjauskirjeResource.class);

		bind(GuiceContainer.class);

		// FIXME VP probably not needed
		bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
		bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

		Map<String, String> params = new HashMap<String, String>();

		params.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");

		serve("/*").with(GuiceContainer.class, params);

		System.out.println();

		System.out.println("CONFIGURE SERVLETS END");
	}
}
