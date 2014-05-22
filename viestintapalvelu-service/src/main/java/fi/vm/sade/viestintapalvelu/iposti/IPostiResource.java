package fi.vm.sade.viestintapalvelu.iposti;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.Urls;

@Component
@PreAuthorize("isAuthenticated()")
@Path(Urls.IPOSTI_RESOURCE_PATH)
public class IPostiResource {
    
    @Autowired
    private IPostiUpload ipostiUpload;
    
    @GET
    @Path("/send")
    @Produces("application/json")
    public Map<String,String> uploadExisting(@Context HttpServletRequest request) {
        
        return new HashMap<String, String>();
    }
    
    @POST
    @Path("/send")
    @Produces("application/json")
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
