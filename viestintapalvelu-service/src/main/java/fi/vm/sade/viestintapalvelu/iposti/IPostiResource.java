package fi.vm.sade.viestintapalvelu.iposti;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordnik.swagger.annotations.*;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.common.util.FilenameHelper;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;
import fi.vm.sade.viestintapalvelu.model.IPosti;

@Component
@PreAuthorize("isAuthenticated()")
@Path(Urls.IPOSTI_RESOURCE_PATH)
// Use HTML-entities instead of scandinavian letters in @Api-description, since
// swagger-ui.js treats model's description as HTML and does not escape it
// properly
@Api(value = "/" + Urls.API_PATH + "/" + Urls.IPOSTI_RESOURCE_PATH, description = "IPostien lähetys- ja hakurajapinnat.")
public class IPostiResource {

    @Autowired
    private DownloadCache downloadCache;

    @Autowired
    private IPostiUpload ipostiUpload;

    @Autowired
    private IPostiService iPostiService;

    @Autowired
    private DocumentBuilder documentBuilder;

    // Descriptions
    private final static String ApiReadUnSentItems = "Palauttaa JSON-objektin lähettämättömistä IPosteista.; synkroninen";
    private final static String ApiSendExisting = "Palauttaa tyhjän JSON-objektin?; synkroninen";
    private final static String ApiSend = "Palauttaa tyhjän JSON-objektin?; synkroninen";
    private final static String ApiReadItem = "Palauttaa ZIP tiedoston; synkroninen";

    private final static String ApiParamValue = "Lähetettävän IPostin ID";

    // Responses
    private final static String SendResponse200 = "OK; IPostin lähetys onnistui.";
    private final static String SendResponse400 = "BAD_REQUEST; IPostin lähetys epäonnistui.";
    private final static String ReadResponse200 = "OK; IPostin hakeminen onnistui.";
    private final static String ReadResponse400 = "BAD_REQUEST; IPostin hakeminen epäonnistui.";
    private final static String UnSentItemsResponse400 = "BAD_REQUEST; Lähettämättömien IPostien hakeminen epäonnistui.";

    @GET
    @Path("/unSentItems")
    @PreAuthorize(Constants.IPOSTI_READ)
    @Produces("application/json")
    @Transactional(readOnly = true)
    @ApiOperation(value = ApiReadUnSentItems, notes = ApiReadUnSentItems)
    @ApiResponses(@ApiResponse(code = 400, message = UnSentItemsResponse400))
    public List<Map<String, String>> unsentIPostiItems(@Context HttpServletRequest request) {

        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<IPosti> unsent = iPostiService.findUnsent();

        // Prosessoidaan lista IPosteja
        for (IPosti ip : unsent) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("id", "" + ip.getId());
            item.put("date", "" + ip.getCreateDate());
            item.put("ipostiId", String.valueOf(ip.getLetterBatch().getId()));
            item.put("template", "" + ip.getLetterBatch().getTemplateName());
            item.put("language", "" + ip.getLetterBatch().getLanguage());
            item.put("name", ip.getContentName());
            result.add(item);
        }
        return result;
    }

    @GET
    @Path("/getBatchById/{ipostiId}")
    @PreAuthorize(Constants.IPOSTI_READ)
    @Produces("application/zip")
    @Transactional(readOnly = true)
    @ApiOperation(value = ApiReadItem, notes = ApiReadItem)
    @ApiResponses({ @ApiResponse(code = 400, message = ReadResponse400), @ApiResponse(code = 200, message = ReadResponse200) })
    public Response getBatchById(@ApiParam(value = ApiParamValue, required = true) @PathParam("ipostiId") String idStr, @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws Exception {
        Long id = Long.parseLong(FilenameHelper.withoutExtension(idStr));
        try {
            IPosti iposti = iPostiService.findBatchById(id);
            byte[] zip = iposti.getContent();
            return downloadZipResponse(id, response, zip);
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

    @GET
    @Path("/getIPostiById/{mailId}")
    @PreAuthorize(Constants.IPOSTI_READ)
    @Produces("application/zip")
    @Transactional(readOnly = true)
    @ApiOperation(value = ApiReadItem, notes = ApiReadItem)
    @ApiResponses({ @ApiResponse(code = 400, message = ReadResponse400), @ApiResponse(code = 200, message = ReadResponse200) })
    public Response getIPostiById(@ApiParam(value = ApiParamValue, required = true) @PathParam("mailId") String idStr, @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws Exception {
        Long id = Long.parseLong(FilenameHelper.withoutExtension(idStr));
        try {
            Map<String, byte[]> batches = new LinkedHashMap<String, byte[]>();
            List<IPosti> iposts = iPostiService.findMailById(id);
            for (IPosti iposti : iposts) {
                IPosti batch = iPostiService.findBatchById(iposti.getId());
                batches.put(batch.getContentName(), batch.getContent());
            }
            byte[] zip = documentBuilder.zip(batches);
            return downloadZipResponse(id, response, zip);
        } catch (Exception e) {

            return Response.status(400).build();
        }
    }

    private Response downloadZipResponse(Long id, HttpServletResponse response, byte[] zip) {
        response.setHeader("Content-Type", "application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + id + ".zip\"");
        response.setHeader("Content-Length", String.valueOf(zip.length));
        response.setHeader("Cache-Control", "private");
        return Response.ok(zip).type("application/zip").build();
    }

    /**
     * Sends single iposti item
     * 
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @GET
    @Path("/sendBatch/{ipostiId}")
    @PreAuthorize(Constants.IPOSTI_SEND)
    @Produces("application/json")
    @Transactional
    @ApiOperation(value = ApiSendExisting, notes = ApiSendExisting)
    @ApiResponses({ @ApiResponse(code = 400, message = SendResponse400), @ApiResponse(code = 200, message = SendResponse200) })
    public Map<String, String> uploadExistingBatch(@ApiParam(value = ApiParamValue, required = true) @PathParam("ipostiId") Long id,
            @Context HttpServletRequest request) throws Exception {
        IPosti iposti = iPostiService.findBatchById(id);
        String forceS = request.getParameter("forceUpload");
        boolean force = (forceS != null || "true".equalsIgnoreCase(forceS));
        upload(iposti, force);
        return new HashMap<String, String>(); // what is this supposed to
                                              // return?
    }

    /**
     * Sends all iposti items for single mail creations
     * 
     * @param id
     * @param request
     * @return
     * @throws Exception
     */

    @GET
    @Path("/sendMail/{mailId}")
    @PreAuthorize(Constants.IPOSTI_SEND)
    @Produces("application/json")
    @Transactional
    @ApiOperation(value = ApiSendExisting, notes = ApiSendExisting)
    @ApiResponses({ @ApiResponse(code = 400, message = SendResponse400), @ApiResponse(code = 200, message = SendResponse200) })
    public Response uploadExistingMail(@ApiParam(value = ApiParamValue, required = true) @PathParam("mailId") Long id, @Context HttpServletRequest request)
            throws Exception {
        try {
            String forceS = request.getParameter("forceUpload");
            boolean force = (forceS != null || "true".equalsIgnoreCase(forceS));
            List<IPosti> iposts = iPostiService.findMailById(id);
            if (iposts != null) {
                for (IPosti iposti : iposts) {
                    upload(iposti, force);
                }
                return Response.status(Status.OK).build();
            }
            return Response.status(Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    private void upload(IPosti iposti, Boolean force) throws Exception {
        if (iposti.getSentDate() == null || (force != null && true == force)) {
            IPosti iPostiWithContent = iPostiService.findBatchById(iposti.getId());
            ipostiUpload.upload(iPostiWithContent.getContent(), "iposti-" + iPostiWithContent.getId() + ".zip");
            iposti.setSentDate(new Date());
            iPostiService.markAsSent(iPostiWithContent);
        }
    }

}
