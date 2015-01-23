/**
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
 **/
package fi.vm.sade.viestintapalvelu;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import fi.vm.sade.viestintapalvelu.download.DownloadResource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class AsynchronousResource {

    protected static final String AsyncResponseLogicDocumentation = "" + "Asynkroninen dokumentin luonti palauttaa välittömästi URLin download-palvelulla "
            + "ladattavaan dokumenttiin. Asynkronisen palvelun luonteen takia dokumentti ei välttämättä "
            + "ole välittömästi ladattavissa. Kutsuvan osapuolen on tarvittaessa osattava uusia haku "
            + "mahdollisesti toistuvasti kunnes dokumentti on valmistunut. Huom! Jos asynkroninen "
            + "dokumentin luonti epäonnistuu, ei siitä tule erikseen mitään muuta indikaatiota kuin "
            + "palvelimelle lokiin merkintä. Jos dokumenttia ei siis ala kuulua download-palvelusta "
            + "järkevän ajan kuluessa, on kutsuvan osapuolen osattava päätellä sopivan ajan kuluttua, "
            + "ettei enää kannata yrittää hakea, koska todennäköisesti dokumentti ei tule koskaan valmistumaan.";

    protected Response createResponse(HttpServletRequest request, String documentId) {
        String resultUrl = urlTo(request, DownloadResource.class);
        URI contentLocation = URI.create(resultUrl + "/" + documentId);
        return Response.status(Status.ACCEPTED).contentLocation(contentLocation).entity(contentLocation.toString()).build();
    }

    protected Response createFailureResponse(HttpServletRequest request) {
        return Response.status(Status.BAD_REQUEST).entity("Document creation failed").build();
    }

    String urlTo(HttpServletRequest request, Class<DownloadResource> resourceClass) {
        String path = chompSlashes(request.getContextPath().trim().equals("") ? "" : "/" + request.getContextPath()) + "/"
                + chompSlashes((request.getServletPath().trim().equals("") ? "" : "/" + request.getServletPath())) + "/"
                + chompSlashes((UriBuilder.fromResource(resourceClass).build().toString()));
        return UriBuilder.fromUri(request.getRequestURL().toString()).replacePath(path).build().toString();
    }

    private static String chompSlashes(final String input) {
        return Joiner.on("/").skipNulls().join(Splitter.on('/').omitEmptyStrings().trimResults().split(input));
    }
}
