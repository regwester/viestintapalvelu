package fi.vm.sade.viestintapalvelu.iposti;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
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
    private IPostiUpload ipostiUpload;
    
    @Autowired
    private IPostiService iPostiService;
    
    private final static String ApiReadUnSent = "Palauttaa JSON-objektin lähettämättömistä IPosteista.; synkroninen";
    private final static String UnSendResponse400 = "BAD_REQUEST; Lähettämättömien IPostien hakeminen epäonnistui.";
    
    private final static String ApiUploadExisting = "Palauttaa tyhjän JSON-objektin?; synkroninen";
    private final static String ApiUpload = "Palauttaa tyhjän JSON-objektin?; synkroninen";
    private final static String UploadResponse400 = "BAD_REQUEST; IPostin lähetys epäonnistui.";
    
    @GET
    @Path("/unSentItems")
    @Secured(Constants.IPOSTI_READ)
    @Produces("application/json")
    @ApiOperation(value = ApiReadUnSent, notes = ApiReadUnSent)
    @ApiResponses(@ApiResponse(code = 400, message = UnSendResponse400))
    public Map<String,String> unsentIPostiItems(@Context HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();
        
        List<IPosti>  unsent = iPostiService.findUnsent();
        for (IPosti ip : unsent) {
            result.put("id", ""+ip.getId());
            result.put("date", ""+ip.getCreateDate());
        }
        return result;
    }
    
    @GET
    @Path("/send")
    @Secured(Constants.IPOSTI_SEND)
    @Produces("application/json")
    @ApiOperation(value = ApiUploadExisting, notes = ApiUploadExisting)
    @ApiResponses(@ApiResponse(code = 400, message = UploadResponse400))
    public Map<String,String> uploadExisting(@Context HttpServletRequest request) throws Exception {
        String id = request.getParameter("ipostiId");
        IPosti iposti = iPostiService.findById(Long.parseLong(id));
        ipostiUpload.upload(iposti.getContent(), "iposti-"+iposti.getId()+".zip");
        iposti.setSentDate(new Date());
        iPostiService.update(iposti);
        return new HashMap<String, String>();
    }
    
    @POST
    @Path("/send")
    @Secured(Constants.IPOSTI_SEND)
    @Produces("application/json")
    @ApiOperation(value = ApiUpload, notes = ApiUpload)
    @ApiResponses(@ApiResponse(code = 400, message = UploadResponse400))
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
