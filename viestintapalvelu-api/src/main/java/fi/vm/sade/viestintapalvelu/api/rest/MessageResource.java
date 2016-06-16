/**
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */
package fi.vm.sade.viestintapalvelu.api.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.security.access.prepost.PreAuthorize;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.api.message.MessageData;
import fi.vm.sade.viestintapalvelu.api.message.MessageStatusResponse;

/**
 * @author risal1
 *
 */
@Path("message")
@Api(value = "/api/v1/message", description = "Resurssi viestien lähetykseen")
@PreAuthorize("isAuthenticated()")
public interface MessageResource {

    
    @POST
    @Path("sendMessageViaAsiointiTiliOrEmail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize(Constants.ASIOINTITILI_CRUD)
    @ApiOperation(value = "Lähettää viestin ensisijaisesti käyttäen asiointitiliä", notes = "Jos asiointitiliä ei ole saatavana vastaanottajalle, lähetetään viesti sähköpostitse mikäli mahdollista",  response = MessageStatusResponse.class)
    MessageStatusResponse sendMessageViaAsiointiTiliOrEmail(MessageData messageData);
}
