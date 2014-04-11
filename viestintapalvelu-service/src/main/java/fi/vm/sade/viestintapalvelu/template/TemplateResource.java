package fi.vm.sade.viestintapalvelu.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.DocumentException;
import com.wordnik.swagger.annotations.Api;

import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.template.Template;

@Component
@Path(Urls.TEMPLATE_RESOURCE_PATH)
@Api(value = "/" + Urls.API_PATH + "/" + Urls.TEMPLATE_RESOURCE_PATH, description = "Kirjepohjarajapinta")
public class TemplateResource extends AsynchronousResource {

    @Autowired
    private TemplateDAO templateDAO;
    
    @Autowired 
    private TemplateService templateService;

    @GET
    // @Consumes("application/json")
    // @PreAuthorize("isAuthenticated()")
    @Transactional
    @Produces("application/json")
    @Path("/get")
    public Template template(@Context HttpServletRequest request) throws IOException,
            DocumentException {

        Template result = new Template();
        String[] fileNames = request.getParameter("templateFile").split(",");
        String language = request.getParameter("lang");
        String styleFile = request.getParameter("styleFile");
        if (styleFile != null) {
            result.setStyles(getStyle(styleFile));
        }

        List<TemplateContent> contents = new ArrayList<TemplateContent>();
        int order = 1;
        for (String file : fileNames) {
            String templateName = Utils.resolveTemplateName("/"+file+"_{LANG}.html", language);
            BufferedReader buff = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(templateName)));
            StringBuilder sb = new StringBuilder();

            String line = buff.readLine();
            while (line != null) {
                sb.append(line);
                line = buff.readLine();
            }
            TemplateContent content = new TemplateContent();
            content.setName(templateName);
            content.setContent(sb.toString());
            content.setOrder(order);
            contents.add(content);
            order++;
        }
        result.setContents(contents);
        Replacement replacement = new Replacement();
        replacement.setName("$letterBodyText");
        ArrayList<Replacement> rList = new ArrayList<Replacement>();
        rList.add(replacement);
        result.setReplacements(rList);
        return result;
    }

    private String getStyle(String styleFile) throws IOException {
        BufferedReader buf = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/"+styleFile+".css")));
        StringBuilder sb = new StringBuilder();
        String line = buf.readLine();
        while (line != null) {
            sb.append(line);
            line = buf.readLine();
        }
        return sb.toString();
    }
    
    @GET
    // @Consumes("application/json")
    // @PreAuthorize("isAuthenticated()")
    @Transactional
    @Produces("application/json")
    @Path("/getById")
    public Template templateByID(@Context HttpServletRequest request) throws IOException,
            DocumentException {
        
       String templateId = request.getParameter("templateId");
       Long id = Long.parseLong(templateId);
       
       return templateService.findById(id);
    }

    @GET
    // @PreAuthorize("isAuthenticated()")
    @Transactional
    @Produces("application/json")
    @Path("/getNames")
    public List<Map<String,String>> templateNames(@Context HttpServletRequest request) throws IOException, DocumentException {
       List<Map<String,String>> res = new ArrayList<Map<String,String>>();
       List<String> serviceResult = templateService.getTemplateNamesList();
       for (String s : serviceResult) {
           if (s != null && s.trim().length() > 0) {
               if (s.indexOf("::") > 0) {
                   Map<String,String> m = new HashMap<String,String>();
                   String[] sa = s.split("::");
                   m.put("name", sa[0]);
                   m.put("lang", sa[1]);
                   res.add(m);
               }
           }
       }
       return res;
    }

    @GET
    // @PreAuthorize("isAuthenticated()")
    @Transactional
    @Produces("application/json")
    @Path("/getByName")
    public Template templateByName(@Context HttpServletRequest request) throws IOException, DocumentException {
        
       String templateName = request.getParameter("templateName");
       String languageCode = request.getParameter("languageCode");
       String content 	   = request.getParameter("content");		// If missing => content included
       boolean getContent = (content != null && "YES".equalsIgnoreCase(content));
       return templateService.getTemplateByName(templateName, languageCode, getContent);
    }

    @POST
    @Consumes("application/json")
    // @PreAuthorize("isAuthenticated()")
    @Produces("application/json")
    @Path("/store")
    public Template store(Template template) throws IOException,
            DocumentException {

        templateService.storeTemplateDTO(template);
        return new Template();
   }

}
