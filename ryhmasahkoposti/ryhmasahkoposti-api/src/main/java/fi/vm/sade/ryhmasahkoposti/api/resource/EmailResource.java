package fi.vm.sade.ryhmasahkoposti.api.resource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import fi.vm.sade.ryhmasahkoposti.api.constants.SecurityConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailResponse;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailSendId;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.SendingStatusDTO;

/**
 * REST-rajapinta ryhmäsähköpostin käsittelyä varten
 * 
 * @author vehei1
 */
@Path("email")
@PreAuthorize(SecurityConstants.USER_IS_AUTHENTICATED)
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
    @Produces("application/json")
    @Path("addAttachment")
    @Secured({SecurityConstants.SEND})
    public AttachmentResponse addAttachment(@Context HttpServletRequest request, @Context HttpServletResponse response) 
    											throws IOException, URISyntaxException, ServletException ;

	/**
	 * Lähettää sähköpostin
	 * 
	 * @param input Lähetettävän ryhmäsähköpostin tiedot
	 * @return Tiedot lähetettävästä sähköpostista
	 */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("sendEmail")
    @Secured({SecurityConstants.SEND})
	public EmailResponse sendEmail(EmailMessage input);

    /**
     * Lähettää kokoelman sähköposteja
     * 
     * @param input Kokoelma lähetettävien sähköpostien tietoja
     * @return Lista lähetettävien sähköpostien tietoja
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("sendEmails")
    @Secured({SecurityConstants.SEND})
	public List<EmailResponse> sendEmails(List<EmailMessage> input);

    /**
     * Pyytää lähetettävän ryhmäsähköpostin tilannetiedot
     * 
     * @param sendId Ryhmäsähköpostin tunnus
     * @return Lähetettävän ryhmäsähköpostin tilannetiedot
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("sendEmailStatus")
    @Secured({SecurityConstants.SEND})
	public SendingStatusDTO sendEmailStatus(String sendId);

    /**
     * Lähettää ryhmäsähköpostin 
     * 
     * @param emailData Ryhmäsähköpostin tiedot
     * @return Ryhmäsähköpostin tunnus
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")   
    @Path("sendGroupEmail")
    @Secured({SecurityConstants.SEND})
	public EmailSendId sendGroupEmail(EmailData emailData);

    /**
     * Pyytää tiedot raportoittavista ryhmäsähköposteista
     * 
     * @param sendId Ryhmäsähköpostin tunnus
     * @return Raportoitavan ryhmäsähköpostin tiedot
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("sendResult")
    @Secured({SecurityConstants.SEND})
	public ReportedMessageDTO sendResult(String sendId);
}