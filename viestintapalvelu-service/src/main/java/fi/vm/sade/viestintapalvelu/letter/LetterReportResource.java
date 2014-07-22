package fi.vm.sade.viestintapalvelu.letter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.convert.PagingAndSortingDTOConverter;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;
import fi.vm.sade.viestintapalvelu.dto.OrganizationDTO;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterBatchReportDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterBatchesReportDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterReceiverLetterDTO;
import fi.vm.sade.viestintapalvelu.dto.query.LetterReportQueryDTO;

/**
 * Kirjelähetysten raportoinnin REST-toteutus
 * 
 * @author vehei1
 *
 */
@Component
@PreAuthorize("isAuthenticated()")
@Path(Urls.REPORTING_PATH)
@Api(value = "/reporting", description = "Kirjel&auml;hetysten raportointi")
public class LetterReportResource extends AsynchronousResource {
    @Autowired
    private LetterReportService letterReportService;
    @Autowired
    private PagingAndSortingDTOConverter pagingAndSortingDTOConverter;
    @Autowired
    private DownloadCache downloadCache;
    
    /**
     * Hakee käyttäjän organisaation kirjelähetysten tiedot
     * 
     * @param organizationOid Organisaation OID
     * @param nbrOfRows Haettavien rivien lukumäärä
     * @param page Sivu, mistä kohdasta haluttu määrä rivejä haetaan
     * @param sortedBy Taulun sarake, minkä mukaan tiedot lajitellaan
     * @param order Lajittelujärjestys
     * @return Lista kirjelähetysten raporttirivejä
     */
    @GET
    @Path(Urls.REPORTING_LIST_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Hakee käyttäjän organisaation kirjelähetysten tiedot", notes = "Hakee halutun määrän käyttäjän "
        + "ja hänen organisaantionsa kirjelähetyksiä. Haku voidaan aloittaa tietystä kohtaa ja ne voidaan hakea lajiteltuna "
        + "nousevasti tai laskevasti tietyn sarakkeen mukaan.", response = LetterBatchesReportDTO.class, responseContainer = "List")
    public Response getLetterBatchReports(@ApiParam(value="Organisaation oid-tunnus", required=false) 
        @QueryParam(Constants.PARAM_ORGANIZATION_OID) String organizationOid, 
        @ApiParam(value="Haettavien rivien lukumäärä", required=true)
        @QueryParam(Constants.PARAM_NUMBER_OF_ROWS) Integer nbrOfRows, 
        @ApiParam(value="Sivu, mistä kohdasta haluttu määrä rivejä haetaan", required=true) 
        @QueryParam(Constants.PARAM_PAGE) Integer page, 
        @ApiParam(value="Taulun sarake, minkä mukaan tiedot lajitellaan", required=false)
        @QueryParam(Constants.PARAM_SORTED_BY) String sortedBy, 
        @ApiParam(value="Lajittelujärjestys", allowableValues="asc, desc" , required=false) 
        @QueryParam(Constants.PARAM_ORDER) String order) {
        List<OrganizationDTO> organizations = letterReportService.getUserOrganizations();
        
        if (organizationOid == null || organizationOid.isEmpty()) {
            organizationOid = organizations.get(0).getOid();
        }
        
        PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
        LetterBatchesReportDTO letterBatchesReport = 
            letterReportService.getLetterBatchesReport(organizationOid, pagingAndSorting);
        
        letterBatchesReport.setOrganizations(organizations);
        for (int i = 0; i < organizations.size(); i++) {
            OrganizationDTO organization = organizations.get(i);
            if (organization.getOid().equals(organizationOid)) {
                letterBatchesReport.setSelectedOrganization(i);
                break;
            }
        }

        return Response.ok(letterBatchesReport).build();
    }

    /**
     * Hakee yksittäisen kirjelähetyksen tiedot
     * 
     * @param id Kirjelähetyksen ID
     * @param nbrOfRows Haettavien rivien lukumäärä
     * @param page Sivu, mistä kohdasta haluttu määrä rivejä haetaan
     * @param sortedBy Taulun sarake, minkä mukaan tiedot lajitellaan
     * @param order Lajittelujärjestys
     * @return Näytettävän kirjelähetyksen tiedot
     */
    @GET
    @Path(Urls.REPORTING_VIEW_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Hakee yksittäisen kirjelähetyksen tiedot", notes = "Haettavat tiedot sisältävät "
        + "vastaanottajien ja kirjeiden tiedot sekä iPosti-lähetyksen tiedot", response=LetterBatchReportDTO.class)
    public Response getLetterBatchReport(@ApiParam(value="Kirjelähetyksen avain", required=true) 
        @QueryParam(Constants.PARAM_ID) Long id,         
        @ApiParam(value="Haettavien rivien lukumäärä", required=true)
        @QueryParam(Constants.PARAM_NUMBER_OF_ROWS) Integer nbrOfRows, 
        @ApiParam(value="Sivu, mistä kohdasta haluttu määrä rivejä haetaan", required=true) 
        @QueryParam(Constants.PARAM_PAGE) Integer page, 
        @ApiParam(value="Taulun sarake, minkä mukaan tiedot lajitellaan", required=false)
        @QueryParam(Constants.PARAM_SORTED_BY) String sortedBy, 
        @ApiParam(value="Lajittelujärjestys", allowableValues="asc, desc" , required=false) 
        @QueryParam(Constants.PARAM_ORDER) String order) {
        PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
        LetterBatchReportDTO letterBatchReport = letterReportService.getLetterBatchReport(id, pagingAndSorting);
        return Response.ok(letterBatchReport).build();
    }

    /**
     * Hakee vastaanottajan kirjeen ja palauttaa linkin ko. kirjeeseen 
     * 
     * @param id Vastaanottajan kirjeen avain
     * @param request HTTP pyyntö
     * @return Linkin vastaanottajan kirjeen
     */
    @GET
    @Path(Urls.REPORTING_LETTER_PATH)
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Hakee vastaanottajan kirjeen ja palauttaa linkin ko. kirjeeseen", notes = "Hakee "
        + "vastaanottajan kirjeen. Kirjeelle tehdään unzipo ja sisältö laitetaan talteen cacheen. Palautetaan linkin"
        + " ko. kirjeeseen", response=String.class)
    public Response getReceiversLetter(@ApiParam(value="Vastaanottajan kirjeen avain") 
        @QueryParam(Constants.PARAM_ID) Long id, @Context HttpServletRequest request) {
        try {
           LetterReceiverLetterDTO letterReceiverLetter = letterReportService.getLetterReceiverLetter(id);
           String documentId = downloadCache.addDocument(new Download(
               letterReceiverLetter.getContentType(), letterReceiverLetter.getTemplateName(), letterReceiverLetter.getLetter()));       
           return createResponse(request, documentId);
        } catch (Exception e) {
           return Response.status(Status.INTERNAL_SERVER_ERROR).entity(Constants.INTERNAL_SERVICE_ERROR).build(); 
        }
    }
    
    /**
     * Hakee hakuparametrien mukaiset kirjelähetysten tiedot
     * 
     * @param organizationOid Organisaation OID
     * @param searchArgument Hakuparametri
     * @param nbrOfRows Haettavien rivien lukumäärä
     * @param page Sivu, mistä kohdasta haluttu määrä rivejä haetaan
     * @param sortedBy Taulun sarake, minkä mukaan tiedot lajitellaan
     * @param order Lajittelujärjestys
     * @return Lista kirjelähetysten raporttirivejä
     */
    @GET
    @Path(Urls.REPORTING_SEARCH_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Hakee hakuparametrien mukaiset kirjelähetysten tiedot", notes = "Hakee hakuparametrien "
        + "mukaisesti halutun määrän käyttäjän ja hänen organisaantionsa kirjelähetyksiä. Haku voidaan aloittaa "
        + "tietystä kohtaa ja ne voidaan hakea lajiteltuna nousevasti tai laskevasti tietyn sarakkeen mukaan.", 
        response = LetterBatchesReportDTO.class, responseContainer = "List")
    public Response searchLetterBatchReports(@ApiParam(value="Organisaation oid-tunnus", required=false) 
        @QueryParam(Constants.PARAM_ORGANIZATION_OID) String organizationOid, 
        @ApiParam(value="Näytöllä annettu hakutekijä esim. kirjeen saajan nimi", required=false) 
        @QueryParam(Constants.PARAM_SEARCH_ARGUMENT) String searchArgument,         
        @ApiParam(value="Haettavien rivien lukumäärä", required=true)
        @QueryParam(Constants.PARAM_NUMBER_OF_ROWS) Integer nbrOfRows, 
        @ApiParam(value="Sivu, mistä kohdasta haluttu määrä rivejä haetaan", required=true) 
        @QueryParam(Constants.PARAM_PAGE) Integer page, 
        @ApiParam(value="Taulun sarake, minkä mukaan tiedot lajitellaan", required=false)
        @QueryParam(Constants.PARAM_SORTED_BY) String sortedBy, 
        @ApiParam(value="Lajittelujärjestys", allowableValues="asc, desc" , required=false) 
        @QueryParam(Constants.PARAM_ORDER) String order) {
        List<OrganizationDTO> organizations = letterReportService.getUserOrganizations();
        
        if (organizationOid == null || organizationOid.isEmpty()) {
            organizationOid = organizations.get(0).getOid();
        }
        
        LetterReportQueryDTO query = new LetterReportQueryDTO();
        query.setOrganizationOid(organizationOid);
        query.setSearchArgument(searchArgument);
        
        PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);

        LetterBatchesReportDTO letterBatchesReport = letterReportService.getLetterBatchesReport(query, pagingAndSorting);

        letterBatchesReport.setOrganizations(organizations);
        for (int i = 0; i < organizations.size(); i++) {
            OrganizationDTO organization = organizations.get(i);
            if (organization.getOid().equals(organizationOid)) {
                letterBatchesReport.setSelectedOrganization(i);
                break;
            }
        }

        return Response.ok(letterBatchesReport).build();
    }
}
