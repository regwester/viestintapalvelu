package fi.vm.sade.viestintapalvelu.feature.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class Mapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public Mapper()
    {
        super();
        registerModule(new JodaModule());
        // True
        enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        // False
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
