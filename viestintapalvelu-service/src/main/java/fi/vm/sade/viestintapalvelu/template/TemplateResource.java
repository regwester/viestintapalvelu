/*
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 */
package fi.vm.sade.viestintapalvelu.template;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fi.vm.sade.auditlog.Audit;
import fi.vm.sade.auditlog.Changes;
import fi.vm.sade.auditlog.User;
import fi.vm.sade.viestintapalvelu.auditlog.AuditLog;
import fi.vm.sade.viestintapalvelu.auditlog.Target;
import fi.vm.sade.viestintapalvelu.auditlog.ViestintapalveluOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;
import com.lowagie.text.DocumentException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fi.vm.sade.viestintapalvelu.AsynchronousResource;
import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.common.util.BeanValidator;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.externalinterface.component.TarjontaComponent;
import fi.vm.sade.viestintapalvelu.letter.LetterService;
import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.validator.UserRightsValidator;

@Component("TemplateResource")
@PreAuthorize("isAuthenticated()")
@Path(Urls.TEMPLATE_RESOURCE_PATH)
@Api(value = "/" + Urls.API_PATH + "/" + Urls.TEMPLATE_RESOURCE_PATH, description = "Kirjepohjarajapinta")
public class TemplateResource extends AsynchronousResource {

    public static final String DEFAULT_STRUCTURE_TYPE = ContentStructureType.letter.name();

    public static final Logger log = LoggerFactory.getLogger(TemplateResource.class);

    @Autowired
    private TemplateService templateService;

    @Autowired
    private LetterService letterService;

    @Autowired
    TarjontaComponent tarjontaComponent;

    @Autowired
    private UserRightsValidator userRightsValidator;

    @Autowired
    private BeanValidator beanValidator;

    private final static String GetHistory = "Palauttaa kirjepohjan historian";
    private final static String GetHistory2 = "Palauttaa listan MAPeja. Ainakin yksi, tällä hetkellä jopa kolme.<br>"
            + "Kukin sisältää MAPin nimen (name) ja listan korvauskenttiä (templateReplacements) <br>" + " - default: pohjan korvauskentät <br>"
            + " - organizationLatest: organisaatiokohtaiset viimeiset pohjan korvauskentät<br> "
            + " - organizationLatestByTag: edelliseen tarkennettuna tunnisteeella ";
    private final static String GetHistory200 = "Hakijalla ei ole valtuuksia hakea kirjepohjia.";

    private final static String TemplateNames = "Palauttaa valittavissaolesvien kirjepohjien nimet.";
    private final static String TemplateNames2 = "Palauttaa listan MAPeja. Esim: <br>" + "{ <br>" + "'name': 'jalkiohjauskirje', <br>" + "'lang': 'FI' <br>"
            + "}";

    private final static String ApitemplateByName = "Palauttaa kirjepohjan nimen perusteella.";
    private final static String ApitemplateVersionsByName = "Palauttaa kirjepohjan kaikki versiot nimen perusteella.";
    private final static String TemplateByID = "Palauttaa kirjepohjan Id:n perusteella.";
    private final static String TemplateExamples = "Palauttaa saatavilla olevien kirjepohjien nimet ja sisällöt.";
    private final static String Store = "Rajapinnalla voi tallentaa kantaan uuden kirjepohjan.";
    private final static String StoreDraft = "Rajapinnalla voi tallentaa kantaa kirjepohjaluonnoksen.";
    private final static String AttachApplicationPeriod = "Rajapinnalla voi liittää haut kirjepohjaan.";

    private final static String GetDraft = "Rajapinnalla voi hakea kirjepohjien luonnoksia.";
    private final static String GetDraft2 = "Palauttaa kirjepohjan luonnoksen nimen kirjepohjan nimen, kielikoodin ja organisaatioOid:in perusteella. Tarkenteena voidaan antaa vielä haku, hakukohde ja tunniste.";
    private final static String GetDraft400 = "Kirjepohjan luonnoksen palautus epäonnistui.";

    private final static String GetTemplateContent = "Palauttaa kirjepohjan sisällön";
    private final static String GetTemplateContent400 = "Kirjepohjan palautus epäonnistui.";
    private static final String DEFAULT_TEMPLATES = "Palauttaa oletus kirjepohjat annetun tilan mukaan";
    private static final String TEMPLATES_BY_HAKU = "Hakee kirjepohjat hakutunnisteen ja tilan perusteella";

    public static final Audit AUDIT = Utils.ViestintaPalveluAudit;

    @GET
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Path("/ok")
    @Produces("application/json")
    public Response ok() {
        return Response.status(Status.OK).build();
    }
    
    /**
     * @param organizationIds
     *            list of organization ids of templates
     * @return a list of templates matching the organizationIds list
     */
    @GET
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Produces("application/json")
    public List<Template> listByOids(@QueryParam("organizationid") List<String> organizationIds) {
        return templateService.findByOrganizationOIDs(organizationIds);
    }

    @Deprecated
    @GET
    @Path("/getById")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = TemplateByID, notes = TemplateByID, response = Template.class)
    public Template templateByID(@Context HttpServletRequest request) throws IOException, DocumentException {
        return templateService.findById(Long.parseLong(request.getParameter("templateId")), parseStructureType(request.getParameter("type")));
    }

    private ContentStructureType parseStructureType(String type) {
        return ContentStructureType.valueOf(Optional.fromNullable(type).or(DEFAULT_STRUCTURE_TYPE));
    }

    @GET
    @Path("/exampleFiles")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Produces("application/json")
    @ApiOperation(value = TemplateExamples, notes = TemplateExamples)
    public List<String> getTemplateExamples() throws IOException {
        return getResourceList("classpath*:/test_data/*.json");
    }

    @GET
    @Path("/exampleFiles/{name}")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Produces("application/json")
    public String getTemplateExample(@PathParam("name") String name) throws IOException {
        return getResource("/test_data/" + name);
    }

    private List<String> getResourceList(String pattern) {
        List<String> resources = new ArrayList<>();
        for (Resource template : Utils.getResourceList(pattern)) {
            resources.add(template.getFilename());
        }
        return resources;
    }

    private String getResource(String file) {
        return Utils.getResource(file).replaceAll("\\r|\\n|\" \"", "");
    }

    @GET
    @Path("/getNames")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = TemplateNames, notes = TemplateNames2)
    public List<Map<String, String>> templateNames() throws IOException, DocumentException {
        List<String> serviceResult = templateService.getTemplateNamesList();
        return formTemplateNameLanguageMap(serviceResult);
    }

    @GET
    @Path("/getNames/{state}")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @ApiOperation(value = TemplateNames, notes = TemplateNames2)
    public List<Map<String, String>> templateNamesByState(@ApiParam(name = "state", value = "kirjepohjan tila millä haetaan") @PathParam("state") State state) {
        List<String> serviceResult = templateService.getTemplateNamesListByState(state);
        return formTemplateNameLanguageMap(serviceResult);
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
            @ApiImplicitParam(name = "applicationPeriod", value = "Haku (OID)", required = false, dataType = "string", paramType = "query") })
    public Template templateByName(@Context HttpServletRequest request) throws IOException, DocumentException {
        return templateService.getTemplateByName(templateCriteriaParams(request), parseBoolean(request, "content"));
    }

    @GET
    @Path("/getByName/{state}")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @ApiOperation(value = ApitemplateByName, notes = ApitemplateByName, response = Template.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "templateName", value = "kirjepohjan nimi (hyvaksymiskirje, jalkiohjauskirje,..)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "languageCode", value = "kielikoodi (FI, SV, ...)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "YES, jos halutaan, että palautetaan myös viestin sisältö.", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "Kirjepohja tyyppi (doc, email)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "applicationPeriod", value = "Haku (OID)", required = false, dataType = "string", paramType = "query") })
    public Template templateByNameAndState(@Context HttpServletRequest request,
            @ApiParam(name = "state", value = "Kirjepohjan tila") @PathParam("state") State state) throws IOException, DocumentException {
        if (state != null) {
            return templateService.getTemplateByName(templateCriteriaParams(request).withState(state), parseBoolean(request, "content"));
        }
        return templateService.getTemplateByName(templateCriteriaParams(request).withState(null), parseBoolean(request, "content"));
    }

    @GET
    @Path("/defaults")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @ApiOperation(value = DEFAULT_TEMPLATES, notes = DEFAULT_TEMPLATES, response = Template.class)
    public List<TemplateInfo> getDefaultTemplates(@ApiParam(name = "state", value = "kirjepohjan tila millä haetaan") @QueryParam("state") State state) {
        if (state != null) {
            return templateService.findTemplateInfoByCriteria(new TemplateCriteriaImpl().withDefaultRequired().withState(state));
        }
        return templateService.findTemplateInfoByCriteria(new TemplateCriteriaImpl().withDefaultRequired().withState(null));
    }

    @GET
    @Path("/listByApplicationPeriod/{applicationPeriod}/{state}")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @ApiOperation(value = TEMPLATES_BY_HAKU, notes = TEMPLATES_BY_HAKU, response = Template.class)
    public List<Template> getTemplatesByApplicationPeriodAndState(
            @ApiParam(name = "applicationPeriod", value = "hakutunniste mille kirjepohjia haetaan") @PathParam("applicationPeriod") String applicationPeriod,
            @ApiParam(name = "state", value = "kirjepohjan tila millä haetaan") @PathParam("state") State state) {
        return templateService.findByCriteria(new TemplateCriteriaImpl().withApplicationPeriod(applicationPeriod).withState(state));
    }

    @GET
    @Path("/listByApplicationPeriod/{applicationPeriod}")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @ApiOperation(value = "Hakee uusimmat kirjepohjat hakutunnisteen perusteella", notes = "Palauttaa myös suljettuja kirjepohjia, olettaen ettei samanlaista löydy julkaistu tai luonnostilassa")
    public TemplatesByApplicationPeriod listTemplatesByApplicationPeriod(
            @ApiParam(name = "applicationPeriod", value = "hakutunniste millä kirjepohjia haetaan") @PathParam("applicationPeriod") String applicationPeriod) {
        return templateService.findByApplicationPeriod(applicationPeriod);
    }

    private TemplateCriteria templateCriteriaParams(HttpServletRequest request) {
        return new TemplateCriteriaImpl().withName(request.getParameter("templateName")).withLanguage(request.getParameter("languageCode"))
                .withType(parseStructureType(request.getParameter("type"))).withApplicationPeriod(request.getParameter("applicationPeriod"));
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
            @ApiImplicitParam(name = "applicationPeriod", value = "Haku (OID)", required = false, dataType = "string", paramType = "query") })
    public List<Template> listVersionsByName(@Context HttpServletRequest request) throws IOException, DocumentException {
        return templateService.listTemplateVersionsByName(templateCriteriaParams(request), parseBoolean(request, "content"), parseBoolean(request, "periods"));
    }

    @GET
    @Path("/listVersionsByName/{state}")
    @Produces("application/json")
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @ApiOperation(value = ApitemplateVersionsByName, notes = ApitemplateVersionsByName, response = Template.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "templateName", value = "kirjepohjan nimi (hyvaksymiskirje, jalkiohjauskirje,..)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "languageCode", value = "kielikoodi (FI, SV, ...)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "YES, jos halutaan, että palautetaan myös viestin sisältö.", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "periods", value = "YES, jos halutaan, että palautetaan myös viestiin liittyvät haut (OID:t).", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "Kirjepohja tyyppi (doc, email)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "applicationPeriod", value = "Haku (OID)", required = false, dataType = "string", paramType = "query") })
    public List<Template> listVersionsByNameUsingState(@Context HttpServletRequest request,
            @ApiParam(name = "state", value = "kirjepohjan tila, millä haetaan") @PathParam("state") State state) throws IOException, DocumentException {
        return templateService.listTemplateVersionsByName(templateCriteriaParams(request).withState(state), parseBoolean(request, "content"),
                parseBoolean(request, "periods"));
    }

    @POST
    @Path("/insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_TEMPLATE)
    @ApiOperation(value = Store, notes = Store)
    public Response insert(Template template, @Context HttpServletRequest request) throws IOException, DocumentException {
        log.info("audit logging insert template: {}", template.toString());
        User user = AuditLog.getUser(request);
        Changes changes = Changes.addedDto(template);

        Response response = userRightsValidator.checkUserRightsToOrganization(Constants.OPH_ORGANIZATION_OID);
        if (Status.OK.getStatusCode() != response.getStatus()) {
            return response;
        }
        beanValidator.validate(template);
        Long templateId = templateService.storeTemplateDTO(template);
        AuditLog.log(AUDIT, user, ViestintapalveluOperation.KIRJEPOHJA_LUONTI, Target.KIRJEPOHJA, String.valueOf(templateId), changes);
        return Response.status(Status.OK).entity(templateId).build();
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_TEMPLATE)
    @ApiOperation(value = "", notes = "")
    public Response update(Template template, @Context HttpServletRequest request) {
        log.info("audit logging update template");
        User user = AuditLog.getUser(request);
        Template old = templateService.findByIdForEditing(template.getId(), State.luonnos);
        Changes changes = Changes.updatedDto(template, old);
        AuditLog.log(AUDIT, user, ViestintapalveluOperation.KIRJEPOHJA_MUOKKAUS, Target.KIRJEPOHJA, String.valueOf(template.getId()), changes);

        Response response = userRightsValidator.checkUserRightsToOrganization(Constants.OPH_ORGANIZATION_OID);
        if (Status.OK.getStatusCode() != response.getStatus()) {
            return response;
        }
        // beanValidator.validate(template);
        templateService.updateTemplate(template);
        return Response.status(Status.OK).build();
    }

    @POST
    @Path("/saveAttachedApplicationPeriods")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_TEMPLATE)
    @ApiOperation(value = AttachApplicationPeriod, notes = AttachApplicationPeriod)
    public Template saveAttachedApplicationPeriods(ApplicationPeriodsAttachDto dto, @Context HttpServletRequest request) throws IOException, DocumentException {
        log.info("audit logging saveAttachedApplicationPeriods");
        User user = AuditLog.getUser(request);
        Changes changes = Changes.addedDto(dto);
        AuditLog.log(AUDIT, user, ViestintapalveluOperation.KIRJEPOHJA_LIITTAMINEN_HAKUUN, Target.KIRJEPOHJA, dto.getTemplateId().toString(), changes);
        return templateService.saveAttachedApplicationPeriods(dto);
    }

    @POST
    @Path("/storeDraft")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_LETTER)
    @ApiOperation(value = StoreDraft, notes = StoreDraft)
    public Response storeDraft(Draft draft, @Context HttpServletRequest request) throws IOException, DocumentException {
        log.info("audit logging storeDraft");
        User user = AuditLog.getUser(request);
        Changes changes = Changes.addedDto(draft);
        AuditLog.log(AUDIT, user, ViestintapalveluOperation.KIRJEPOHJA_LUONNOS_TALLENNUS, Target.KIRJEPOHJA_LUONNOS, draft.getDraftId().toString(), changes);

        Response response = userRightsValidator.checkUserRightsToOrganization(draft.getOrganizationOid());
        if (Status.OK.getStatusCode() != response.getStatus()) {
            return response;
        }
        templateService.storeDraftDTO(draft);
        return Response.status(Status.OK).build();
    }
    
    @POST
    @Path("/updateDraft")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_CREATE_LETTER)
    @ApiOperation(value = "Päivittää annetun kirjeluonnoksen kantaan", notes = "Vain korvauskentät voi päivittää")
    public Response updateDraft(DraftUpdateDTO draft, @Context HttpServletRequest request) {
        log.info("audit logging updateDraft");
        User user = AuditLog.getUser(request);
        String oldContent = templateService.getDraftContent(draft.id);
        Changes changes = Changes.updatedDto(draft.content, oldContent);
        AuditLog.log(AUDIT, user, ViestintapalveluOperation.KIRJEPOHJA_LUONNOS_MUOKKAUS, Target.KIRJEPOHJA_LUONNOS, String.valueOf(draft.id), changes);

        Response response = userRightsValidator.checkUserRightsToOrganization(draft.orgoid);
        if (Status.OK.getStatusCode() != response.getStatus()) {
            return response;
        }
        templateService.updateDraft(draft);
        return Response.status(Status.OK).build();
    }

    @GET
    @Path("/getDraft")
    @Produces(MediaType.APPLICATION_JSON)
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

        Draft draft = templateService.findDraftByNameOrgTag(templateName, languageCode, oid, applicationPeriod, fetchTarget, tag);
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
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    public Response getTempHistory(@Context HttpServletRequest request) throws IOException, DocumentException {
        // Pick up the organization oid from request and check urer's rights to
        // organization
        String oid = request.getParameter("oid");
        Response response = userRightsValidator.checkUserRightsToOrganization(oid);

        // User isn't authorized to the organization
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            return response;
        }

        TemplateBundle bundle = new TemplateBundle();

        String templateName = request.getParameter("templateName");
        String languageCode = request.getParameter("languageCode");
        String applicationPeriod = request.getParameter("applicationPeriod");
        String content = request.getParameter("content"); // If missing =>
                                                          // content excluded
        ContentStructureType type = parseStructureType(request.getParameter("type"));
        boolean getContent = (content != null && "YES".equalsIgnoreCase(content));

        bundle.setLatestTemplate(templateService.getTemplateByName(new TemplateCriteriaImpl(templateName, languageCode, type), getContent));

        if ((oid != null) && !("".equals(oid))) {
            bundle.setLatestOrganisationReplacements(letterService.findReplacementByNameOrgTag(templateName, languageCode, oid, Optional.<String> absent(),
                    Optional.fromNullable(applicationPeriod)));

            String tag = request.getParameter("tag");
            if ((tag != null) && !("".equals(tag))) {
                bundle.setLatestOrganisationReplacementsWithTag(letterService.findReplacementByNameOrgTag(templateName, languageCode, oid,
                        Optional.of(tag), Optional.fromNullable(applicationPeriod)));
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
    @Produces(MediaType.APPLICATION_JSON)
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
            @ApiImplicitParam(name = "fetchTarget", value = "Hakukohde", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "Rakennetyyppi", required = false, dataType = "string", paramType = "query") })
    public Response getHistory(@Context HttpServletRequest request) throws IOException, DocumentException {
        // Pick up the organization oid from request and check urer's rights to
        // organization
        String oid = request.getParameter("oid");
        Response response = userRightsValidator.checkUserRightsToOrganization(oid);

        // User isn't authorized to the organization
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            return response;
        }

        List<Map<String, Object>> history = new LinkedList<>();

        String templateName = request.getParameter("templateName");
        String languageCode = request.getParameter("languageCode");

        // Drafts replacements
        String applicationPeriod = request.getParameter("applicationPeriod"); // =
                                                                              // Haku

        // OPH default template
        ContentStructureType type = parseStructureType(request.getParameter("type"));
        TemplateCriteria criteria = new TemplateCriteriaImpl(templateName, languageCode, type).withApplicationPeriod(applicationPeriod);
        Template template = templateService.getTemplateByName(criteria, true);

        Map<String, Object> templateRepl = new HashMap<>();
        templateRepl.put("name", "default");
        if (template != null) {
            templateRepl.put("templateReplacements", template.getReplacements());
        } else {
            String msg = String.format("No template found for criteria %s", criteria);
            log.error(msg);
            return Response.status(Status.NOT_FOUND).entity(msg).build();
        }
        history.add(templateRepl);

        if ((oid != null) && !("".equals(oid))) {
            Optional<String> applicationPeriodForTagAndNonTagSeach = Optional.absent();

            List<Replacement> templateReplacements = letterService.findReplacementByNameOrgTag(templateName, languageCode, oid, Optional.<String> absent(),
                    applicationPeriodForTagAndNonTagSeach);

            if (templateReplacements != null && !templateReplacements.isEmpty()) {
                Map<String, Object> organisationRepl = new HashMap<>();
                organisationRepl.put("name", "organizationLatest");
                organisationRepl.put("templateReplacements", templateReplacements);
                history.add(organisationRepl);
            }

            // Latest LetterBatch replacements for that OrganisationOid with a
            // tag
            String tag = request.getParameter("tag");
            if ((tag != null) && !("".equals(tag))) {
                templateReplacements = letterService.findReplacementByNameOrgTag(templateName, languageCode, oid, Optional.of(tag),
                        applicationPeriodForTagAndNonTagSeach);
                if (templateReplacements != null && !templateReplacements.isEmpty()) {
                    Map<String, Object> tagRepl = new HashMap<>();
                    tagRepl.put("name", "organizationLatestByTag");
                    tagRepl.put("templateReplacements", templateReplacements);
                    history.add(tagRepl);
                }
            }

            String fetchTarget = request.getParameter("fetchTarget"); // =
                                                                      // Hakukohde
            templateReplacements = templateService.findDraftReplacement(templateName, languageCode, oid, applicationPeriod, fetchTarget, tag);
            if (templateReplacements != null && !templateReplacements.isEmpty()) {
                Map<String, Object> draftRepl = new HashMap<>();
                draftRepl.put("name", "draft");
                draftRepl.put("templateReplacements", templateReplacements);
                history.add(draftRepl);
            }
        }

        return Response.ok(history).build();
    }

    @GET
    @Path("/{templateId}/{type}/getTemplateContent")
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = TemplateByID, notes = TemplateByID, response = Template.class)
    public Template getTemplateByID(@PathParam("templateId") String templateId, @PathParam("type") String type) {
        Long id = Long.parseLong(templateId);
        ContentStructureType typeEnumValue = ContentStructureType.valueOf(type);
        return templateService.findById(id, typeEnumValue);
    }

    @GET
    @Path("/{templateId}/getTemplateContent")
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = TemplateByID, notes = TemplateByID, response = Template.class)
    public Template getTemplateByID(@PathParam("templateId") String templateId, @Context HttpServletRequest request) {
        final Long id;
        try {
            id = Long.parseLong(templateId);
        } catch (NumberFormatException e) {
            log.error("Error parsing template id {} into Long type", templateId, e);
            return null;
        }

        ContentStructureType type = parseStructureType(request.getParameter("type"));
        return templateService.findById(id, type);
    }

    @GET
    @Deprecated
    @Path("{name}/{languageCode}/edit")
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = "Palauttaa kirjepohjan kirjepohjan sekä rakenteen muokkausta varten", response = Template.class)
    public Template getTemplateByIDForEditing(@PathParam("name") String name, @PathParam("languageCode") String languageCode,
            @Context HttpServletRequest request) {
        return templateService
                .findByIdForEditing(new TemplateCriteriaImpl(name, languageCode).withApplicationPeriod(request.getParameter("applicationPeriod")));
    }

    @GET
    @Deprecated
    @Path("/{templateId}/edit")
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @Transactional
    @ApiOperation(value = "Palauttaa kirjepohjan kirjepohjan sekä rakenteen muokkausta varten", response = Template.class)
    public Template getTemplateByIDForEditing(@PathParam("templateId") Long templateId) {
        return templateService.findByIdForEditing(templateId, null);
    }

    @GET
    @Path("/{templateId}/getTemplateContent/{state}")
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIAKIRJAPALVELU_READ)
    @ApiOperation(value = TemplateByID, notes = TemplateByID, response = Template.class)
    public Template getTemplateByIDAndState(@PathParam("templateId") long templateId,
            @ApiParam(name = "state", value = "Kirjepohjan tila") @PathParam("state") State state, @QueryParam("structureType") ContentStructureType type) {
        type = Optional.fromNullable(type).or(ContentStructureType.letter);
        return templateService.findByIdAndState(templateId, type, state);
    }

    @GET
    @Deprecated
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{templateName}/{languageCode}/{type}/getTemplateContent")
    @ApiOperation(value = GetTemplateContent, notes = GetTemplateContent, response = String.class)
    @ApiResponses({ @ApiResponse(code = 400, message = GetTemplateContent400) })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "templateName", value = "kirjepohjan nimi (hyvaksymiskirje, jalkiohjauskirje,..)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "languageCode", value = "kielikoodi (FI, SV, ...)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "kirjepohjan tyyppi", required = true, dataType = "string", paramType = "query"), })
    public Template getTemplateContent(
            @ApiParam(value = "Muodostettavien kirjeiden tiedot (1-n)", required = true) @PathParam("templateName") String templateName,
            @PathParam("languageCode") String languageCode, @PathParam("type") String type) throws IOException, DocumentException, NoSuchAlgorithmException {

        // Return template content
        ContentStructureType structureType = parseStructureType(type);
        return templateService.getTemplateByName(new TemplateCriteriaImpl(templateName, languageCode, structureType), true);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{templateName}/{languageCode}/{type}/{applicationPeriod}/getTemplateContent")
    @ApiOperation(value = GetTemplateContent, notes = GetTemplateContent, response = String.class)
    @ApiResponses({ @ApiResponse(code = 400, message = GetTemplateContent400) })
    public Template getTemplateContent(
            @ApiParam(name = "templateName", value = "kirjepohjan nimi (hyvaksymiskirje, jalkiohjauskirje,..)", required = true) @PathParam("templateName") String templateName,

            @ApiParam(name = "languageCode", value = "kielikoodi (FI, SV, ...)", required = true) @PathParam("languageCode") String languageCode,

            @ApiParam(name = "type", value = "kirjepohjan tyyppi", required = true) @PathParam("type") String type,

            @ApiParam(name = "applicationPeriod", value = "haku (OID)", required = true) @PathParam("applicationPeriod") String applicationPeriod)
            throws IOException, DocumentException, NoSuchAlgorithmException {
        ContentStructureType structureType = parseStructureType(type);

        // Return template content
        return templateService.getTemplateByName(new TemplateCriteriaImpl(templateName, languageCode, structureType).withApplicationPeriod(applicationPeriod),
                true);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/draft/applicationPeriod/{applicationPeriod}")
    public List<Draft> getDraftsByApplicationPeriod(
            @ApiParam(name = "applicationPeriod", value = "haku (OID)", required = true) @PathParam("applicationPeriod") String applicationPeriod,
            @ApiParam(name = "organizationid", value = "organizaatioiden OID", required = false) @QueryParam("organizationid") List<String> organizationIds) {
        return templateService.getDraftsByOrgOidsAndApplicationPeriod(organizationIds, applicationPeriod);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/draft")
    public List<Draft> getDraftsByTags(@ApiParam(name = "tags", value = "", required = false) @QueryParam("tags") List<String> tags) {
        return templateService.getDraftsByTags(tags);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public List<TemplateListing> getLatestTemplatesByLanguageAndType(
            @ApiParam(name = "type", value = "Kirjepohjan tyyppi (koekutsu-, jälkiohjaus tai hyväksymiskirje)", required = true) @QueryParam("type") String type,
            @ApiParam(name = "language", value = "Kirjepohjan kielikoodi ISO 639-1 muodossa (esim. FI, SV, EN, ...", required = true) @QueryParam("language") String language) {
            return templateService.getTemplateIdsAndApplicationPeriodNames();
    }

    private List<Map<String, String>> formTemplateNameLanguageMap(List<String> serviceResult) {
        List<Map<String, String>> res = new ArrayList<>();
        for (String s : serviceResult) {
            if (s != null && s.trim().length() > 0) {
                if (s.indexOf("::") > 0) {
                    Map<String, String> m = new HashMap<>();
                    String[] sa = s.split("::");
                    m.put("name", sa[0]);
                    m.put("lang", sa[1]);
                    res.add(m);
                }
            }
        }
        return res;
    }

}
