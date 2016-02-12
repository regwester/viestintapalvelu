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
package fi.vm.sade.viestintapalvelu.download;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Collection;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Service;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.common.util.FilenameHelper;

@Service("DownloadResource")
@Singleton
@Path(Urls.DOWNLOAD_RESOURCE_PATH)
@Api(value = "/" + Urls.API_PATH + "/" + Urls.DOWNLOAD_RESOURCE_PATH, description = "Valmiin PDF/ZIP-tiedoston haku")
public class DownloadResource {
    private DownloadCache downloadCache;

    @Inject
    public DownloadResource(DownloadCache downloadCache) {
        this.downloadCache = downloadCache;
    }

    /**
     * @Deprecated Viestintapalvelun tiedostolistausta ja tiedostocachea ei
     *             kayteta tuotannossa. Tiedostot siirretaan valintojen
     *             dokumenttipalveluun tilapaistaltiointiin ja suojaukseen.
     */
    @Deprecated
    @GET
    @Produces(APPLICATION_JSON)
    public Collection<Header> available() {
        // Wrapping to TreeSet automatically sorts the elements as TreeSet is
        // SortedSet. Header is sorted by createdAt value.
        return new TreeSet<Header>(downloadCache.getListOfAvailableDocuments());
    }

    @GET
    @Path("/{documentId}")
    @ApiOperation(value = "Lataa valmis PDF/ZIP-tiedosto", notes = "Lataa valmis PDF/ZIP-tiedosto kirje- tai osoitetarra-palveluiden palauttamalla dokumentin tunnisteella")
    @ApiResponses(@ApiResponse(code = 400, message = "BAD_REQUEST; annetulla ID:llä ei löydy ladattavaa dokumenttia"))
    public Response download(@ApiParam(value = "Ladattavan dokumentin ID", required = true) @PathParam("documentId") String input,
            @Context HttpServletResponse response) {
        Download download = downloadCache.get(FilenameHelper.withoutExtension(input));
        if (download == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        response.setHeader("Content-Type", download.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + download.getFilename() + "\"");
        response.setHeader("Content-Length", String.valueOf(download.toByteArray().length));
        response.setHeader("Cache-Control", "private");
        return Response.ok(download.toByteArray()).type(download.getContentType()).build();
    }
}
