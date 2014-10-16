package fi.vm.sade.viestintapalvelu.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;
import com.lowagie.text.DocumentException;
import com.wordnik.swagger.annotations.*;

import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.letter.LetterBuilder;
import fi.vm.sade.viestintapalvelu.letter.LetterService;
import fi.vm.sade.viestintapalvelu.validator.UserRightsValidator;

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
    private UserRightsValidator userRightsValidator;

    @Autowired
    private LetterBuilder letterBuilder;

    private final static String GetHistory = "Palauttaa kirjepohjan historian";
    private final static String GetHistory2 = "Palauttaa listan MAPeja. Ainakin yksi, tällä hetkellä jopa kolme.<br>"
        + "Kukin sisältää MAPin nimen (name) ja listan korvauskenttiä (templateReplacements) <br>"
        + " - default: pohjan korvauskentät <br>"
        + " - organizationLatest: organisaatiokohtaiset viimeiset pohjan korvauskentät<br> "
        + " - organizationLatestByTag: edelliseen tarkennettuna tunnisteeella ";
    private final static String GetHistory200 = "Hakijalla ei ole valtuuksia hakea kirjepohjia.";

    private final static String TemplateNames = "Palauttaa valittavissaolevien kirjepohjien nimet.";
    private final static String TemplateNames2 = "Palauttaa listan MAPeja. Esim: <br>" + "{ <br>"
        + "'name': 'jalkiohjauskirje', <br>" + "'lang': 'FI' <br>" + "}";

    private final static String ApitemplateByName = "Palauttaa kirjepohjan nimen perusteella.";
    private final static String ApitemplateVersionsByName = "Palauttaa kirjepohjan kaikki versiot nimen perusteella.";
    private final static String TemplateByID = "Palauttaa kirjepohjan Id:n perusteella.";
    private final static String TemplateExamples = "Palauttaa saatavilla olevien kirjepohjien nimet ja sisällöt.";
    private final static String Store = "Rajapinnalla voi tallentaa kantaa kirjepohjan.";
    private final static String StoreDraft = "Rajapinnalla voi tallentaa kantaa kirjepohjaluonnoksen.";
    private final static String AttachApplicationPeriod = "Rajapinnalla voi liittää haut kirjepohjaan.";

    private final static String GetDraft = "Rajapinnalla voi hakea kirjepohjien luonnoksia.";
    private final static String GetDraft2 = "Palauttaa kirjepohjan luonnoksen nimen kirjepohjan nimen, kielikoodin ja organisaatioOid:in perusteella. Tarkenteena voidaan antaa vielä haku, hakukohde ja tunniste.";
    private final static String GetDraft400 = "Kirjepohjan luonnoksen palautus epäonnistui.";

    private final static String GetTemplateContent = "Palauttaa kirjepohjan sisällön";
    private final static String GetTemplateContent400 = "Kirjepohjan palautus epäonnistui.";

    @GET
    @Path("/get")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    public Template template(@Context HttpServletRequest request) throws IOException, DocumentException {

        Template result = new Template();
        String[] fileNames = request.getParameter("templateFile").split(",");
        String language = request.getParameter("lang");
        String styleFile = request.getParameter("styleFile");
        if (styleFile != null) {
            result.setStyles(getStyle(styleFile));
        }

        // Read type if provided
        String type = request.getParameter("type");
        if (type != null) {
            if (StringUtils.equalsIgnoreCase(type, "email"))
                type = "doc";
            type = type.toLowerCase();
        }

        List<TemplateContent> contents = new ArrayList<TemplateContent>();
        int order = 1;
        for (String file : fileNames) {
            String templateName = Utils.resolveTemplateName("/templates/" + file + "_{LANG}{TYPE}.html", language, type);
            BufferedReader buff = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(templateName)));
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
        replacement.setName("$sisalto");
        ArrayList<Replacement> rList = new ArrayList<Replacement>();
        rList.add(replacement);
        result.setReplacements(rList);

        //return new Template();
        return result;
    }

    @Deprecated
    @GET
    @Path("/getById")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = TemplateByID, notes = TemplateByID, response = Template.class)
    public Template templateByID(@Context HttpServletRequest request) throws IOException, DocumentException {
        String templateId = request.getParameter("templateId");
        Long id = Long.parseLong(templateId);

        return templateService.findById(id);
    }


    
    @GET
    @Path("/getAvailableExamples")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Produces("application/json")
    @Transactional
    @ApiOperation(value = TemplateExamples, notes = TemplateExamples)
    public List<Map<String, String>> templateExamples(@Context HttpServletRequest request) throws IOException,
        DocumentException {
        List<Map<String, String>> res = new ArrayList<Map<String, String>>();

        String[] templates = {
                "/test_data/hyvaksymiskirje_FI.json", "/test_data/hyvaksymiskirje_SV.json",
                "/test_data/jalkiohjauskirje_FI.json", "/test_data/jalkiohjauskirje_SV.json",
                "/test_data/hyvaksymiskirje_nivel_FI.json", "/test_data/hyvaksymiskirje_nivel_SV.json",
                "/test_data/jalkiohjauskirje_nivel_FI.json", "/test_data/jalkiohjauskirje_nivel_SV.json",
                "/test_data/koekutsukirje_EN.json","/test_data/koekutsukirje_SV.json", "/test_data/koekutsukirje_FI.json",
                "/test_data/osoitepalvelu_email_EN.json","/test_data/osoitepalvelu_email_SV.json", 
                "/test_data/osoitepalvelu_email_FI.json"
                };

        for (String template : templates) {
            Map<String, String> current = new HashMap<String, String>();
            BufferedReader buff = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(template)));
            StringBuilder sb = new StringBuilder();

            String line = buff.readLine();
            while (line != null) {
                sb.append(line);
                line = buff.readLine();
            }
            current.put("name", template);
            current.put("content", sb.toString());
            res.add(current);
        }

        return res;
    }

    @GET
    @Path("/getNames")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = TemplateNames, notes = TemplateNames2)
    public List<Map<String, String>> templateNames(@Context HttpServletRequest request) throws IOException,
        DocumentException {
        List<Map<String, String>> res = new ArrayList<Map<String, String>>();
        List<String> serviceResult = templateService.getTemplateNamesList();
        for (String s : serviceResult) {
            if (s != null && s.trim().length() > 0) {
                if (s.indexOf("::") > 0) {
                    Map<String, String> m = new HashMap<String, String>();
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
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = ApitemplateByName, notes = ApitemplateByName, response = Template.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "templateName", value = "kirjepohjan nimi (hyvaksymiskirje, jalkiohjauskirje,..)", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "languageCode", value = "kielikoodi (FI, SV, ...)", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "content", value = "YES, jos halutaan, että palautetaan myös viestin sisältö.", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "type", value = "Kirjepohja tyyppi (doc, email)", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "applicationPeriod", value = "Haku (OID)", required = false, dataType = "string", paramType = "query")})
    public Template templateByName(@Context HttpServletRequest request) throws IOException, DocumentException {
        return templateService.getTemplateByName(templateCriteriaParams(request), parseBoolean(request, "content"));
    }

    private TemplateCriteria templateCriteriaParams(HttpServletRequest request) {
        return new TemplateCriteriaImpl()
                    .withName(request.getParameter("templateName"))
                    .withLanguage(request.getParameter("languageCode"))
                    .withType(request.getParameter("type"))
                    .withApplicationPeriod(request.getParameter("applicationPeriod"));
    }

    private boolean parseBoolean(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName); // If missing => false
        return (value != null && "YES".equalsIgnoreCase(value));
    }

    @GET
    @Path("/listVersionsByName")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = ApitemplateVersionsByName, notes = ApitemplateVersionsByName, response = Template.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "templateName", value = "kirjepohjan nimi (hyvaksymiskirje, jalkiohjauskirje,..)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "languageCode", value = "kielikoodi (FI, SV, ...)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "YES, jos halutaan, että palautetaan myös viestin sisältö.", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "periods", value = "YES, jos halutaan, että palautetaan myös viestiin liittyvät haut (OID:t).", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "Kirjepohja tyyppi (doc, email)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "applicationPeriod", value = "Haku (OID)", required = false, dataType = "string", paramType = "query")})
    public List<Template> listVersionsByName(@Context HttpServletRequest request) throws IOException, DocumentException {
        return templateService.listTemplateVersionsByName(templateCriteriaParams(request),
                parseBoolean(request, "content"),
                parseBoolean(request, "periods"));
    }

    @POST
    @Path("/store")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8;")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_TEMPLATE)
    @ApiOperation(value = Store, notes = Store)
    public Template store(Template template) throws IOException, DocumentException {
        templateService.storeTemplateDTO(template);
        return new Template(); //TODO: return something more meaningful
    }

    @PUT
    @Path("/saveAttachedApplicationPeriods")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8;")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_TEMPLATE)
    @ApiOperation(value = AttachApplicationPeriod, notes = AttachApplicationPeriod)
    public Template saveAttachedApplicationPeriods(ApplicationPeriodsAttachDto dto) throws IOException, DocumentException {
        return templateService.saveAttachedApplicationPeriods(dto);
    }

    @POST
    @Path("/storeDraft")
    @Consumes("application/json")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_TEMPLATE)
    @ApiOperation(value = StoreDraft, notes = StoreDraft)
    public Draft storeDraft(Draft draft) throws IOException, DocumentException {
        templateService.storeDraftDTO(draft);
        return new Draft(); //TODO: return something more meaningful
    }

    @GET
    @Path("/getDraft")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = GetDraft, notes = GetDraft2, response = Draft.class)
    @ApiResponses(@ApiResponse(code = 400, message = GetDraft400))
    public Response getDraft(@Context HttpServletRequest request) throws IOException, DocumentException {
        // Pick up the organization oid from request and check urer's rights to
        // organization
        String oid = request.getParameter("oid");

        // Response response =
        // userRightsValidator.checkUserRightsToOrganization(oid);
        Response response = userRightsValidator.checkUserRightsToOrganization(null);

        // User isn't authorized to the organization
        if (response.getStatus() != 200) {
            return response;
        }

        String templateName = request.getParameter("templateName");
        String languageCode = request.getParameter("languageCode");
        // String oid = request.getParameter("oid");
        String applicationPeriod = request.getParameter("applicationPeriod");
        String fetchTarget = request.getParameter("fetchTarget");
        String tag = request.getParameter("tag");

        if (applicationPeriod == null) {
            applicationPeriod = "";
        }
        if (fetchTarget == null) {
            fetchTarget = "";
        }
        if (tag == null) {
            tag = "";
        }

        Draft draft = new Draft();
        draft = templateService.findDraftByNameOrgTag(templateName, languageCode, oid, applicationPeriod, fetchTarget,
            tag);
        return Response.ok(draft).build();
    }

    /**
     * http://localhost:8080/viestintapalvelu/api/v1/template/getTempHistory?
     * templateName
     * =hyvaksymiskirje&languageCode=FI&content=YES&oid=123456789&tag=par
     * 
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    @GET
    @Path("/getTempHistory")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    public Response getTempHistory(@Context HttpServletRequest request) throws IOException, DocumentException {
        // Pick up the organization oid from request and check urer's rights to
        // organization
        String oid = request.getParameter("oid");
        Response response = userRightsValidator.checkUserRightsToOrganization(oid);

        // User isn't authorized to the organization
        if (response.getStatus() != 200) {
            return response;
        }

        TemplateBundle bundle = new TemplateBundle();

        String templateName = request.getParameter("templateName");
        String languageCode = request.getParameter("languageCode");
        String applicationPeriod = request.getParameter("applicationPeriod");
        String content = request.getParameter("content"); // If missing =>
                                                          // content excluded
        boolean getContent = (content != null && "YES".equalsIgnoreCase(content));

        bundle.setLatestTemplate(templateService.getTemplateByName(templateName, languageCode, getContent));

        if ((oid != null) && !("".equals(oid))) {
            bundle.setLatestOrganisationReplacements(letterService.findReplacementByNameOrgTag(templateName,
                    languageCode, oid, Optional.<String>absent(), Optional.fromNullable(applicationPeriod)));

            String tag = request.getParameter("tag");
            if ((tag != null) && !("".equals(tag))) {
                bundle.setLatestOrganisationReplacementsWithTag(letterService.findReplacementByNameOrgTag(templateName,
                    languageCode, oid, Optional.fromNullable(tag), Optional.fromNullable(applicationPeriod)));
            }
        }

        return Response.ok(bundle).build();
    }

    /**
     * http://localhost:8080/viestintapalvelu/api/v1/template/getHistory?
     * templateName=hyvaksymiskirje&languageCode=FI&oid=123456789&tag=11111
     * 
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    @GET
    @Path("/getHistory")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = GetHistory, notes = GetHistory2, response = fi.vm.sade.viestintapalvelu.template.Replacement.class)
    @ApiResponses({ @ApiResponse(code = 200, message = GetHistory200) })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "templateName", value = "kirjepohjan nimi (hyvaksymiskirje, jalkiohjauskirje,..)", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "languageCode", value = "kielikoodi (FI, SV, ...)", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "oid", value = "Organisaation Oid", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "tag", value = "Vapaa teksti tunniste", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "applicationPeriod", value = "Haku", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "fetchTarget", value = "Hakukohde", required = false, dataType = "string", paramType = "query")
    })
    public Response getHistory(@Context HttpServletRequest request) throws IOException, DocumentException {
        // Pick up the organization oid from request and check urer's rights to
        // organization
        String oid = request.getParameter("oid");
        Response response = userRightsValidator.checkUserRightsToOrganization(oid);

        // User isn't authorized to the organization
        if (response.getStatus() != 200) {
            return response;
        }

        List<Map<String, Object>> history = new LinkedList<Map<String, Object>>();

        String templateName = request.getParameter("templateName");
        String languageCode = request.getParameter("languageCode");
        // String content = request.getParameter("content"); // If missing =>
        // content excluded
        // boolean getContent = (content != null &&
        // "YES".equalsIgnoreCase(content));
        boolean getContent = false;

        // Drafts replacements
        String applicationPeriod = request.getParameter("applicationPeriod"); // = Haku

        // OPH default template
        Template template = templateService.getTemplateByName(
                new TemplateCriteriaImpl(templateName, languageCode)
                        .withApplicationPeriod(applicationPeriod), getContent);

        Map<String, Object> templateRepl = new HashMap<String, Object>();
        templateRepl.put("name", "default");
        templateRepl.put("templateReplacements", template.getReplacements());
        history.add(templateRepl);


        if ((oid != null) && !("".equals(oid))) {
            Optional<String> applicationPeriodForTagAndNonTagSeach = Optional.absent();

            List<Replacement> templateReplacements = letterService.findReplacementByNameOrgTag(templateName,
                languageCode, oid, Optional.<String>absent(), applicationPeriodForTagAndNonTagSeach);

            if (templateReplacements != null && !templateReplacements.isEmpty()) {
                Map<String, Object> organisationRepl = new HashMap<String, Object>();
                organisationRepl.put("name", "organizationLatest");
                organisationRepl.put("templateReplacements", templateReplacements);
                history.add(organisationRepl);
            }

            // Latest LetterBatch replacements for that OrganisationOid with a
            // tag
            String tag = request.getParameter("tag");
            if ((tag != null) && !("".equals(tag))) {
                templateReplacements = letterService.findReplacementByNameOrgTag(templateName, languageCode, oid,
                        Optional.fromNullable(tag), applicationPeriodForTagAndNonTagSeach);
                if (templateReplacements != null && !templateReplacements.isEmpty()) {
                    Map<String, Object> tagRepl = new HashMap<String, Object>();
                    tagRepl.put("name", "organizationLatestByTag");
                    tagRepl.put("templateReplacements", templateReplacements);
                    history.add(tagRepl);
                }
            }

            String fetchTarget = request.getParameter("fetchTarget"); // = Hakukohde
            templateReplacements = templateService.findDraftReplacement(templateName, languageCode, oid,
                applicationPeriod, fetchTarget, tag);
            if (templateReplacements != null && !templateReplacements.isEmpty()) {
                Map<String, Object> draftRepl = new HashMap<String, Object>();
                draftRepl.put("name", "draft");
                draftRepl.put("templateReplacements", templateReplacements);
                history.add(draftRepl);
            }
        }

        return Response.ok(history).build();
    }

    private String getStyle(String styleFile) throws IOException {
        BufferedReader buf = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
            "/template_styles/" + styleFile + ".css")));
        StringBuilder sb = new StringBuilder();
        String line = buf.readLine();
        while (line != null) {
            sb.append(line);
            line = buf.readLine();
        }
        return sb.toString();
    }

    @GET
    @Path("/{templateId}/getTemplateContent")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = TemplateByID, notes = TemplateByID, response = Template.class)
    public Template getTemplateByID(@PathParam("templateId") String templateId) {
        Long id = Long.parseLong(templateId);
        return templateService.findById(id);
    }
    
    @GET
    @Produces("application/json")
    @Path("/{templateName}/{languageCode}/{type}/getTemplateContent")
    @ApiOperation(value = GetTemplateContent, notes = GetTemplateContent, response = String.class)
    @ApiResponses({ @ApiResponse(code = 400, message = GetTemplateContent400) })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "templateName", value = "kirjepohjan nimi (hyvaksymiskirje, jalkiohjauskirje,..)", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "languageCode", value = "kielikoodi (FI, SV, ...)", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "type", value = "kirjepohjan tyyppi", required = true, dataType = "string", paramType = "query"), })
    public Template getTemplateContent(
        @ApiParam(value = "Muodostettavien kirjeiden tiedot (1-n)", required = true) @PathParam("templateName") String templateName,
        @PathParam("languageCode") String languageCode, @PathParam("type") String type) throws IOException,
        DocumentException, NoSuchAlgorithmException {

        // Return template content
        return templateService.getTemplateByName(templateName, languageCode, true, type);
    }

    @GET
    @Produces("application/json")
    @Path("/{templateName}/{languageCode}/{type}/{applicationPeriod}/getTemplateContent")
    @ApiOperation(value = GetTemplateContent, notes = GetTemplateContent, response = String.class)
    @ApiResponses({ @ApiResponse(code = 400, message = GetTemplateContent400) })
    public Template getTemplateContent(
            @ApiParam(name = "templateName", value = "kirjepohjan nimi (hyvaksymiskirje, jalkiohjauskirje,..)", required = true)
            @PathParam("templateName") String templateName,

            @ApiParam(name = "languageCode", value = "kielikoodi (FI, SV, ...)", required = true)
            @PathParam("languageCode") String languageCode,

            @ApiParam(name = "type", value = "kirjepohjan tyyppi", required = true)
            @PathParam("type") String type,

            @ApiParam(name = "applicationPeriod", value = "haku (OID)", required = true)
            @PathParam("applicationPeriod") String applicationPeriod)
                throws IOException, DocumentException, NoSuchAlgorithmException {
        // Return template content
        return templateService.getTemplateByName(new TemplateCriteriaImpl()
                        .withName(templateName)
                        .withLanguage(languageCode)
                        .withType(type)
                        .withApplicationPeriod(applicationPeriod), true);
    }
}
