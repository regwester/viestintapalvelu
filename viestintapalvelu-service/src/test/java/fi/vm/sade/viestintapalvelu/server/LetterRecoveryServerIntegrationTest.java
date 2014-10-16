package fi.vm.sade.viestintapalvelu.server;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchDto;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchLetterDto;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterBatchDetails;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

public class LetterRecoveryServerIntegrationTest extends ServerIntegrationTest {
    
    @Test
    public void recoversAndFinishesInterruptedLetterBatchProcess() throws Exception {
        String s = new ObjectMapper().writeValueAsString(DocumentProviderTestData.getTemplate());
        createWebTarget("/api/v1/template/store").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(s));
        String json = new ObjectMapper().writeValueAsString(givenLetterBatchWith100Letters());
        Response response = createWebTarget("/api/v1/letter/async/letter/").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(json));
        String id = response.readEntity(String.class);
        while (!statusIsReady(id)) {
            Thread.sleep(500);
        }
    }

    private boolean statusIsReady(String id) {
        Response statusResponse = createWebTarget("/api/v1/letter/async/letter/status/" + id).request(MediaType.APPLICATION_JSON_TYPE).get();
        return statusResponse.readEntity(String.class).contains("ready");
    }
    
    private LetterBatchDetails givenLetterBatchWith100Letters() {
        AsyncLetterBatchDto batch = DocumentProviderTestData.getAsyncLetterBatch();
        List<AsyncLetterBatchLetterDto> letters = batch.getLetters();
        for (int i = 0; i < 100; i++) {
            letters.addAll(DocumentProviderTestData.getAsyncLetterBatchLetters());
        }
        return batch;
    }
}
