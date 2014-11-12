package fi.vm.sade.viestintapalvelu.letter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.letter.dto.AsyncLetterBatchDto;

@Component
@Path(Urls.TRUSTED_PATH + "/" + Urls.LETTER_PATH)
public class LetterResourceTrusted extends AbstractLetterResource {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/async/letter")
    public Response asyncLetter(@ApiParam(value = "Muodostettavien kirjeiden tiedot (1-n)", required = true) final AsyncLetterBatchDto input) {
        return createAsyncLetter(input, true);
    }

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/async/letter/status/{letterBatchId}")
    @ApiOperation(value = "Palauttaa kirjelähetykseen kuuluvien käsiteltyjen kirjeiden määrän ja kokonaismäärän")
    public Response letterBatchStatus(@PathParam("letterBatchId") @ApiParam(value = "Kirjelähetyksen id")  String prefixedLetterBatchId) {
        return getLetterBatchStatus(prefixedLetterBatchId);
    }

}
