package fi.vm.sade.viestintapalvelu.feature;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.jaxrs.base.JsonMappingExceptionMapper;
import com.fasterxml.jackson.jaxrs.base.JsonParseExceptionMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class JacksonFeature implements Feature {

    private static final ObjectMapper mapper = new ObjectMapper() {{
        registerModule(new JodaModule());
        // True
        enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        // False
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }};

    private static final JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider(){{
        setMapper(mapper);
    }};

    @Override
    public boolean configure(FeatureContext context) {
        // Might need to register additional custom ObjectMapperProvider
        context.register(provider);
        context.register(JsonMappingExceptionMapper.class);
        context.register(JsonParseExceptionMapper.class);
        return true;
    }
}
