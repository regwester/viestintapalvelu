package fi.vm.sade.viestintapalvelu.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.DocumentException;
import com.wordnik.swagger.annotations.Api;

import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.letter.LetterService;

@Component
@PreAuthorize("isAuthenticated()")
@Path(Urls.TEMPLATE_RESOURCE_PATH)
@Api(value = "/" + Urls.API_PATH + "/" + Urls.TEMPLATE_RESOURCE_PATH, description = "Kirjepohjarajapinta")
public class TemplateResource extends AsynchronousResource {  
    @Autowired 
    private TemplateService templateService;
    
    @Autowired 
    private LetterService letterService;
    
    @Autowired
    private CurrentUserComponent currentUserComponent;

    @GET
    @Path("/get")
    @Produces("application/json")
//    @Secured(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
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

    @GET
    @Path("/getById")
    @Produces("application/json")
//    @Secured(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    public Template templateByID(@Context HttpServletRequest request) throws IOException, DocumentException {        
       String templateId = request.getParameter("templateId");
       Long id = Long.parseLong(templateId);
       
       return templateService.findById(id);
    }

    @GET
    @Path("/getNames")
    @Produces("application/json")
//    @Secured(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
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
    @Path("/getByName")
    @Produces("application/json")
//    @Secured(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    public Template templateByName(@Context HttpServletRequest request) throws IOException, DocumentException {       
       String templateName = request.getParameter("templateName");
       String languageCode = request.getParameter("languageCode");
       String content 	   = request.getParameter("content");		// If missing => content excluded
       boolean getContent = (content != null && "YES".equalsIgnoreCase(content));
       return templateService.getTemplateByName(templateName, languageCode, getContent);
    }

    @POST
    @Path("/store")
    @Consumes("application/json")
    @Produces("application/json")
//    @Secured(Constants.ASIAKIRJAPALVELU_CREATE_TEMPLATE)
    public Template store(Template template) throws IOException, DocumentException {
        templateService.storeTemplateDTO(template);
        return new Template();
    }
    
    
    /**
     * http://localhost:8080/viestintapalvelu/api/v1/template/getTempHistory?templateName=hyvaksymiskirje&languageCode=FI&content=YES&oid=123456789&tag=par
     * 
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    @GET
    @Path("/getTempHistory")
    @Produces("application/json")
//    @Secured(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    public Response getTempHistory(@Context HttpServletRequest request) throws IOException, DocumentException {
        // Pick up the organization oid from request and check urer's rights to organization
        String oid = request.getParameter("oid");
        Response response = checkUserRights(oid); 
        
        // User isn't authorized to the organization
        if (response.getStatus() != 200) {
            return response;
        }

    	TemplateBundle bundle = new TemplateBundle();
    	
        String templateName = request.getParameter("templateName");
        String languageCode = request.getParameter("languageCode");
        String content 	   = request.getParameter("content");		// If missing => content excluded
        boolean getContent = (content != null && "YES".equalsIgnoreCase(content));
        
        bundle.setLatestTemplate(templateService.getTemplateByName(templateName, languageCode, getContent));
    	
        
        if ((oid!=null) && !("".equals(oid)) ) {			

        	bundle.setLatestOrganisationReplacements(letterService.findReplacementByNameOrgTag(templateName, oid, "%%"));	
			
			String tag = request.getParameter("tag");
	        if ((tag!=null) && !("".equals(tag)) ) {
	        	bundle.setLatestOrganisationReplacementsWithTag(letterService.findReplacementByNameOrgTag(templateName, oid, tag));
	        }
		}
		
		return Response.ok(bundle).build();
    }

    
    /**
     * http://localhost:8080/viestintapalvelu/api/v1/template/getHistory?templateName=hyvaksymiskirje&languageCode=FI&oid=123456789&tag=11111
     * 
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    @GET
    @Path("/getHistory")
    @Produces("application/json")
//    @Secured(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    public Response getHistory(@Context HttpServletRequest request) throws IOException, DocumentException {
        // Pick up the organization oid from request and check urer's rights to organization
        String oid = request.getParameter("oid");
        Response response = checkUserRights(oid); 
        
        // User isn't authorized to the organization
        if (response.getStatus() != 200) {
            return response;
        }

    	List<Map<String, Object>> history = new LinkedList<Map<String, Object>>();
    	
        String templateName = request.getParameter("templateName");
        String languageCode = request.getParameter("languageCode");
//        String content 	   = request.getParameter("content");		// If missing => content excluded
//        boolean getContent = (content != null && "YES".equalsIgnoreCase(content));
        boolean getContent = false;
               
        // OPH default template
        Template template = templateService.getTemplateByName(templateName, languageCode, getContent);
        
        Map<String, Object> templateRepl = new HashMap<String, Object>();
        templateRepl.put("default", template.getReplacements());
        history.add(templateRepl);       
                        
        
        if ((oid!=null) && !("".equals(oid)) ) {			
			
			// Latest LetterBatch replacements for that OrganisationOid
	        Map<String, Object> organisationRepl = new HashMap<String, Object>();
	        organisationRepl.put("organisationOid", letterService.findReplacementByNameOrgTag(templateName, oid, "%%") );
	        history.add(organisationRepl);
	
			String tag = request.getParameter("tag");
	        if ((tag!=null) && !("".equals(tag)) ) {
	        	
				// Latest LetterBatch replacements for that OrganisationOid
		        Map<String, Object> tagRepl = new HashMap<String, Object>();
		        tagRepl.put("organisationOidTag", letterService.findReplacementByNameOrgTag(templateName, oid, tag)  );
		        history.add(tagRepl);
	        }
        }
	     
        return Response.ok(history).build();
    }

    private Response checkUserRights(String oid) {
        if (oid == null) {
            return Response.status(Status.OK).build();
        }
        
        List<OrganisaatioHenkilo> organisaatioHenkiloList = currentUserComponent.getCurrentUserOrganizations();
        
        for (OrganisaatioHenkilo organisaatioHenkilo : organisaatioHenkiloList) {
            if (oid.equals(organisaatioHenkilo.getOrganisaatioOid())) {
                return Response.status(Status.OK).build();
            }
        }
        
        return Response.status(Status.FORBIDDEN).entity("User is not authorized to the organization " + oid).build();
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
    
}
