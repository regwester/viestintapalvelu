package fi.vm.sade.viestintapalvelu.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import com.wordnik.swagger.annotations.ApiParam;

import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.dao.TemplateDAO;
import fi.vm.sade.viestintapalvelu.model.Template;

@Component
@Path(Urls.TEMPLATE_RESOURCE_PATH)
@Api(value = "/" + Urls.API_PATH + "/" + Urls.TEMPLATE_RESOURCE_PATH, description = "Kirjepohjarajapinta")
public class TemplateResource extends AsynchronousResource {

    @Autowired
    private TemplateDAO templateDAO;

    @GET
    // @Consumes("application/json")
    // @PreAuthorize("isAuthenticated()")
    @Transactional
    @Produces("application/json")
    @Path("/get")
    public Template template(
            @ApiParam(value = "Template tunnisteet", required = true) String nimi,
            @Context HttpServletRequest request) throws IOException,
            DocumentException {

        Template result = new Template();
        String templateName = Utils.resolveTemplateName(
                Constants.LETTER_TEMPLATE, "FI");
        BufferedReader buff = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(templateName)));
        StringBuilder sb = new StringBuilder();

        String line = buff.readLine();
        while (line != null) {
            sb.append(line);
            line = buff.readLine();
        }
        TemplateContent content = new TemplateContent();
        content.setName(templateName);
        content.setContent(sb.toString());

        List<TemplateContent> contents = new ArrayList<TemplateContent>();
        contents.add(content);

        Replacement replacement = new Replacement();
        replacement.setName("$letterBodyText");
        ArrayList<Replacement> rList = new ArrayList<Replacement>();
        rList.add(replacement);
        return result;
    }

    @POST
    @Consumes("application/json")
    // @PreAuthorize("isAuthenticated()")
    @Produces("application/json")
    @Path("/template")
    public Template store(
            @ApiParam(value = "Template tunnisteet", required = true) Template template,
            @Context HttpServletRequest request) throws IOException,
            DocumentException {

        /*
         * fi.vm.sade.viestintapalvelu.model.Template model = new
         * fi.vm.sade.viestintapalvelu.model.Template(); //TemplateDAO
         * templateDAO = new TemplateDAOImpl(); model.setName("test");
         * model.setTimestamp(new Date());
         * 
         * templateDAO.insert(model);
         */

        throw new DocumentException("not implemented");
    }

}
