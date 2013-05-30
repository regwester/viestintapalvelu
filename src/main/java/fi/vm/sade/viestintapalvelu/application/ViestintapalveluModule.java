package fi.vm.sade.viestintapalvelu.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import fi.vm.sade.viestintapalvelu.domain.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.domain.address.AddressLabelBatch;
import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;
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

	@Provides
	@Singleton
	ObjectMapper objectMapper() {
		System.out.println("objectMapper()");
		final ObjectMapper mapper = new ObjectMapper();
		SimpleModule jacksonMappingModule = new SimpleModule(
				"Viestintapalvelu-Jackson-Module", new Version(1, 0, 0,
						"SNAPSHOT"));

		jacksonMappingModule.addDeserializer(AddressLabelBatch.class,
				new JsonDeserializer<AddressLabelBatch>() {
					@Override
					public AddressLabelBatch deserialize(JsonParser parser,
							DeserializationContext context) throws IOException,
							JsonProcessingException {
						System.out.println("deserialize addresslabelbatch");
						System.out.println("current token: "
								+ parser.getCurrentToken());
						System.out.println("current location: "
								+ parser.getCurrentLocation());

						System.out.println(parser);
						System.out.println(context);
						JsonNode node = parser.readValueAsTree();
						System.out.println("node: " + node);
						JsonNode addressLabels = node
								.findValue("addressLabels");
						System.out.println("isArray? "
								+ addressLabels.isArray());

						System.out.println("current token: "
								+ parser.getCurrentToken());
						System.out.println("current location: "
								+ parser.getCurrentLocation());

						Iterator<JsonNode> iterator = addressLabels.iterator();
						List<AddressLabel> labels = new ArrayList<AddressLabel>();
						while (iterator.hasNext()) {
							JsonNode node2 = iterator.next();
							PostalAddress postalAddress = mapper.readValue(
									node2, PostalAddress.class);
							System.out.println(postalAddress);
							labels.add(new AddressLabel(postalAddress));
						}

						return new AddressLabelBatch(labels);
					}
				});
		mapper.registerModule(jacksonMappingModule);
		return mapper;
	}

	@Provides
	@Singleton
	JacksonJsonProvider jacksonJsonProvider(ObjectMapper mapper) {
		return new JacksonJsonProvider(mapper);
	}
}
