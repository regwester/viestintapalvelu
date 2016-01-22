package fi.vm.sade.ryhmasahkoposti.api.dto;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReportedMessageDTOTest {

    @Test
    public void testGetBodyReturnsRetractedPersonalURLs() throws Exception {
        ReportedMessageDTO reportedMessageDTO = new ReportedMessageDTO();
        String token = "a02f12b22a21ce3";
        String body = "Header\n" +
                "Url https://itest-oppija.oph.ware.fi/hakuperusteet/app/1.2.246.562.11.00000001342#/token/" + token + "\n" +
                "Footer";
        assertTrue(body.contains(token));
        reportedMessageDTO.setBody(body);
        assertFalse(reportedMessageDTO.getBody().contains(token));
    }
}