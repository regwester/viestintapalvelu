package fi.vm.sade.ryhmasahkoposti.api.resource;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.security.access.prepost.PreAuthorize;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fi.vm.sade.ryhmasahkoposti.api.constants.SecurityConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailSendId;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;
import org.springframework.stereotype.Component;

/**
 * REST-rajapinta ryhmäsähköpostin käsittelyä varten
 * 
 * @author vehei1
 */
@Component
@Path("email")
@PreAuthorize(SecurityConstants.USER_IS_AUTHENTICATED)
@Api(value = "/email", description = "Ryhm&auml;s&auml;hk&oumlpostin l&auml;hett&auml;minen")
public interface EmailResource {
    /**
     * Lisää ryhmäshköpostin liitteen 
     * 
     * @param request Http pyyntö
     * @param response Http vastaus
     * @return Lisätyn liitteen tiedot
     * @throws IOException
     * @throws URISyntaxException
     * @throws ServletException
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/plain")
    @Path("attachment")
    @PreAuthorize(SecurityConstants.SEND)
    @ApiOperation(value = "Lisää käyttäjän valitsemat liitetiedostot tietokantaan", 
        notes = "Käytäjän valitsemat liitetiedosto pitää olla multipart-tyyppisiä",  
        response = String.class)
    @ApiResponses({@ApiResponse(code = 400, message = "Not a multipart request")})
    public String addAttachment(@Context HttpServletRequest request, @Context HttpServletResponse response)	
        throws IOException, URISyntaxException, ServletException ;

    /**
     * Alustaa ryhmäsähköpostilähetyksen palauttamalla OK-vastauksen käyttöliittymälle
     * 
     * @return OK-vastaus
     */
    @GET
    @Produces("application/json")
    @Path("ok")
    @PreAuthorize(SecurityConstants.SEND)
    @ApiOperation(value = "Palauttaa OK-vastauksen käyttöliittymälle")
    public Response ok();
    
    /**
     * Lähettää ryhmäsähköpostin vastaanottajille ilman alaviitettä
     *
     * @param emailData Lähetettävän ryhmäsähköpostin tiedot
     * @return Lähetettävän ryhmäsähköpostiviestin tunnus
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/")
    @PreAuthorize(SecurityConstants.SEND)
    @ApiOperation(value = "Lähettää ryhmäsähköpostin vastaanottajille", 
        notes = "Lähetettävä sähköposti ei sisällä alaviitettä", response = EmailSendId.class)
    @ApiResponses({@ApiResponse(code = 500, 
        message = "Internal service error tai liittymävirheen, jos yhteys henkilo- tai organisaatiopalveluun ei toimi")})
    public Response sendEmail(@ApiParam(value = "Lähettetävän sähköpostin ja vastaanottajien tiedot", required = true)
        EmailData emailData) throws Exception;

    /**
     * Pyytää lähetettävän ryhmäsähköpostin tilannetiedot
     * 
     * @param sendId Ryhmäsähköpostin tunnus
     * @return Lähetettävän ryhmäsähköpostin tilannetiedot
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("status")
    @PreAuthorize(SecurityConstants.SEND)
    @ApiOperation(value = "Palauttaa halutun ryhmäsähköpostin lähetyksen tilannetiedot", response = SendingStatusDTO.class)
    public Response getStatus(@ApiParam(value = "Ryhmäsähköpostin avain", required = true) String sendId) throws Exception;

    /**
     * Pyytää tiedot raportoittavista ryhmäsähköposteista
     * 
     * @param sendId Ryhmäsähköpostin tunnus
     * @return Raportoitavan ryhmäsähköpostin tiedot
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("result")
    @PreAuthorize(SecurityConstants.SEND)
    @ApiOperation(value = "Palauttaa lähetetyn ryhmäsähköpostin raportin", response = ReportedMessageDTO.class)
    @ApiResponses({@ApiResponse(code = 500, message = "Internal service error tai liittymävirhe")})
    public Response getResult(@ApiParam(value = "Ryhmäsähköpostiviestin avain", required = true) String sendId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("count")
    @PreAuthorize(SecurityConstants.READ)
    @ApiOperation(value = "Palauttaa sähköpostien lukumäärän")
    @ApiResponses({@ApiResponse(code = 500, message = "Internal service error")})
    public Response getCount() throws Exception;
}