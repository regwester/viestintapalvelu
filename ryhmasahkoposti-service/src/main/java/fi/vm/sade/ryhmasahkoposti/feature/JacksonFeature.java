package fi.vm.sade.ryhmasahkoposti.feature;

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
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }};

    private static final JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider(){{
        setMapper(mapper);
    }};

    @Override
    public boolean configure(FeatureContext context) {
        context.register(provider);
        context.register(JsonMappingExceptionMapper.class);
        context.register(JsonParseExceptionMapper.class);
        return true;
    }
}
