package fi.vm.sade.viestintapalvelu.application;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.Jalkiohjauskirje;
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
		final ObjectMapper mapper = new ObjectMapper();

		// FIXME VP
		//		SimpleModule jacksonMappingModule = new SimpleModule(
		//				"Viestintapalvelu-Jackson-Module", new Version(1, 0, 0,
		//						"SNAPSHOT"));
		//		jacksonMappingModule.addDeserializer(AddressLabelBatch.class,
		//				new JsonDeserializer<AddressLabelBatch>() {
		//					@Override
		//					public AddressLabelBatch deserialize(JsonParser parser,
		//							DeserializationContext context) throws IOException,
		//							JsonProcessingException {
		//						JsonNode node = parser.readValueAsTree();
		//						JsonNode addressLabels = node
		//								.findValue("addressLabels");
		//						Iterator<JsonNode> iterator = addressLabels.iterator();
		//						List<AddressLabel> labels = new ArrayList<AddressLabel>();
		//						while (iterator.hasNext()) {
		//							// FIXME vp
		//							//JsonNode node2 = iterator.next();
		//							//							PostalAddress postalAddress = mapper.readValue(
		//							//									node2, PostalAddress.class);
		//							//							labels.add(new AddressLabel(postalAddress));
		//						}
		//
		//						return new AddressLabelBatch(labels);
		//					}
		//				});
		//
		//mapper.registerModule(jacksonMappingModule);

		MrBeanModule mrBean = new MrBeanModule();
		mapper.registerModule(mrBean);
		mapper.addMixInAnnotations(Jalkiohjauskirje.class, Mixin.class);
		mapper.disable(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS);
		mapper.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
		mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);

		return mapper;
	}

	public static abstract class Mixin {
		@JsonProperty("addressLabel")
		@JsonAnyGetter
		public abstract PostalAddress getPostalAddress();

		@JsonProperty("addressLabel")
		public abstract void setPostalAddress();
	}

	@Provides
	@Singleton
	JacksonJsonProvider jacksonJsonProvider(ObjectMapper mapper) {
		return new JacksonJsonProvider(mapper);
	}
}
