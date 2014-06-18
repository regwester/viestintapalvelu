package fi.vm.sade.viestintapalvelu.iposti;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;
import fi.vm.sade.viestintapalvelu.model.IPosti;

@Component
@PreAuthorize("isAuthenticated()")
@Path(Urls.IPOSTI_RESOURCE_PATH)

//Use HTML-entities instead of scandinavian letters in @Api-description, since
//swagger-ui.js treats model's description as HTML and does not escape it
//properly

@Api(value = "/" + Urls.API_PATH + "/" + Urls.IPOSTI_RESOURCE_PATH, description = "IPostien lähetys- ja hakurajapinnat.")
public class IPostiResource {
    
    @Autowired
    private DownloadCache downloadCache;
    
    @Autowired
    private IPostiUpload ipostiUpload;
    
    @Autowired
    private IPostiService iPostiService;
    
    //Descriptions
    private final static String ApiReadUnSentItems = "Palauttaa JSON-objektin lähettämättömistä IPosteista.; synkroninen";   
    private final static String ApiSendExisting = "Palauttaa tyhjän JSON-objektin?; synkroninen";
    private final static String ApiSend = "Palauttaa tyhjän JSON-objektin?; synkroninen";
    private final static String ApiReadItem = "Palauttaa ZIP tiedoston; synkroninen";
    
    private final static String ApiParamValue = "Lähetettävän IPostin ID";
    
    //Responses
    private final static String SendResponse200 = "OK; IPostin lähetys onnistui.";
    private final static String SendResponse400 = "BAD_REQUEST; IPostin lähetys epäonnistui.";
    private final static String ReadResponse200 = "OK; IPostin hakeminen onnistui.";
    private final static String ReadResponse400 = "BAD_REQUEST; IPostin hakeminen epäonnistui.";
    private final static String UnSentItemsResponse400 = "BAD_REQUEST; Lähettämättömien IPostien hakeminen epäonnistui.";
    
    @GET
    @Path("/unSentItems")
    //@Secured(Constants.IPOSTI_READ_META_INFO)
    @Produces("application/json")
    @ApiOperation(value = ApiReadUnSentItems, notes = ApiReadUnSentItems)
    @ApiResponses(@ApiResponse(code = 400, message = UnSentItemsResponse400))
    public List<Map<String,String>> unsentIPostiItems(@Context HttpServletRequest request) {
        
        List<Map<String, String>> result = new ArrayList<Map<String, String>>(); 
        List<IPosti>  unsent = iPostiService.findUnsent();
        
        //Prosessoidaan lista IPosteja
        for (IPosti ip : unsent) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("id", ""+ip.getId());
            item.put("date", ""+ip.getCreateDate());
            item.put("templateId", String.valueOf(ip.getLetterBatch().getId()));
            result.add(item);
        }
        return result;
    }
    
    @GET
    @Path("/getById/{ipostiId}")
    //@Secured(Constants.IPOSTI_READ_DATA)
    @Produces("application/zip")
    @ApiOperation(value = ApiReadItem, notes = ApiReadItem)
    @ApiResponses({@ApiResponse(code = 400, message = ReadResponse400), @ApiResponse(code = 200, message = ReadResponse200)})
    public Response unsentIPostiItem(@ApiParam(value = ApiParamValue, required = true) @PathParam("ipostiId") Long id, @Context HttpServletRequest request) throws Exception {
        try {
            IPosti iposti = iPostiService.findById(id);
            byte[] zip = iposti.getContent();
            return Response.ok(zip).build();
        } catch(Exception e) {
            return Response.status(400).build();
        }       
    }
    
    @GET
    @Path("/send/{ipostiId}")
    //@Secured(Constants.IPOSTI_SEND)
    @Produces("application/json")
    @ApiOperation(value = ApiSendExisting, notes = ApiSendExisting)
    @ApiResponses({@ApiResponse(code = 400, message = SendResponse400), @ApiResponse(code = 200, message = SendResponse200)})
    public Map<String,String> uploadExisting(@ApiParam(value = ApiParamValue, required = true) @PathParam("ipostiId") Long id, @Context HttpServletRequest request) throws Exception {
        IPosti iposti = iPostiService.findById(id);
        ipostiUpload.upload(iposti.getContent(), "iposti-"+iposti.getId()+".zip");
        iposti.setSentDate(new Date());
        iPostiService.update(iposti);
        return new HashMap<String, String>();
    }
    
    @POST
    @Path("/send")
    //@Secured(Constants.IPOSTI_SEND)
    @Produces("application/json")
    @ApiOperation(value = ApiSend, notes = ApiSend)
    @ApiResponses({@ApiResponse(code = 400, message = SendResponse400), @ApiResponse(code = 200, message = SendResponse200)})
    public Map<String,String> upload(@Context HttpServletRequest request) {
        
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        Map<String,String> result = new HashMap<String, String>();
        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                List<FileItem> items = upload.parseRequest(request);
                Iterator<FileItem> iterator = items.iterator();
                int count = 0;
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    if (item.getName() != null) {
                        ipostiUpload.upload(item.get(), item.getName());
                        count++;
                    }
                }
                result.put("handledFileCount", ""+count);
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.put("result", "OK");
        return result;
    }
}
