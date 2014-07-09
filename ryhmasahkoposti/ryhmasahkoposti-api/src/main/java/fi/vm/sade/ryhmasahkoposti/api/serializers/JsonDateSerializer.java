package fi.vm.sade.ryhmasahkoposti.api.serializers;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class JsonDateSerializer extends JsonSerializer<DateTime>{
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    
    @Override
    public void serialize(DateTime dt, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
        String date = dateFormat.format(dt);
        gen.writeString(date);
    }
}
