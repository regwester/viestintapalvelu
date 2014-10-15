package fi.vm.sade.viestintapalvelu.server;

import javax.ws.rs.core.MediaType;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class LetterRecoveryServerIntegrationTest extends ServerIntegrationTest {
    
    @Test
    public void quickTest() {
        assertNotNull(createWebTarget("/api/v1/letter/async/letter/status/1").request(MediaType.APPLICATION_JSON_TYPE).get());
    }
}
