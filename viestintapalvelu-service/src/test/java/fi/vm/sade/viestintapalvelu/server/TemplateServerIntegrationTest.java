package fi.vm.sade.viestintapalvelu.server;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import static org.junit.Assert.assertEquals;

public class TemplateServerIntegrationTest extends ServerIntegrationTest {
    
    @Test
    public void createsTemplate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(DocumentProviderTestData.getTemplate());
        Response resp = createWebTarget("/api/v1/template/store").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(s));
        assertEquals(200, resp.getStatus());
    }
}
