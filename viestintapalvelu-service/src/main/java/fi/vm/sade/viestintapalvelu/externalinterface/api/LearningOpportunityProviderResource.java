package fi.vm.sade.viestintapalvelu.externalinterface.api;

import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.LOPDto;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.*;
import java.util.List;

/**
 * Created by jonimake on 21.11.2014.
 */
@Component
public interface LearningOpportunityProviderResource {
//https://itest-oppija.oph.ware.fi/lop/search/*?asId=1.2.246.562.5.2013112910452702965370
    /*
    @GET
    @Path("/search/*")
    @Produces("application/json;charset=UTF-8")
    public List<LOPDto> getProviderListByApplicationId(@PathParam("asId") String applicationId);
*/
    public static final String BASE_EDUCATION = "baseEducation";
    public static final String VOCATIONAL = "vocational";
    public static final String NON_VOCATIONAL = "nonVocational";
    public static final String ASID = "asId";
    public static final String TERM = "term";
    public static final String LANG = "lang";
    public static final String LANG_FI = "fi";

    @GET
    @Path("search/{" + TERM + "}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public List<LOPDto> searchProviders(@PathParam(TERM) final String term,
                                                         @QueryParam(ASID) final String asId,
                                                         @QueryParam(BASE_EDUCATION) final List<String> baseEducation,
                                                         @DefaultValue(value = "true") @QueryParam(VOCATIONAL) final boolean vocational,
                                                         @DefaultValue(value = "true") @QueryParam(NON_VOCATIONAL) final boolean nonVocational,
                                                         @DefaultValue(value = "0") @QueryParam("start") int start,
                                                         @DefaultValue(value = "50") @QueryParam("rows") int rows,
                                                         @DefaultValue(LANG_FI) @QueryParam(LANG) String lang);
}
