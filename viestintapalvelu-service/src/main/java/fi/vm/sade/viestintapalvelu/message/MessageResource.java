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
package fi.vm.sade.viestintapalvelu.message;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import fi.vm.sade.viestintapalvelu.Urls;
import fi.vm.sade.viestintapalvelu.download.Download;
import fi.vm.sade.viestintapalvelu.download.DownloadCache;

/**
 * 
 * @author Jussi Jartamo
 * 
 *         Temporary resource for messaging
 */
@Service("MessageResource")
@Singleton
@Path(Urls.MESSAGE_RESOURCE_PATH)
public class MessageResource {
    private DownloadCache downloadCache;

    @Inject
    public MessageResource(DownloadCache downloadCache) {
        this.downloadCache = downloadCache;
    }

    @POST
    @Produces(APPLICATION_JSON)
    public Response message(String message) {
        downloadCache.addDocument(new Download("text/plain", message, new byte[] {}));
        return Response.ok().build();
    }
}
