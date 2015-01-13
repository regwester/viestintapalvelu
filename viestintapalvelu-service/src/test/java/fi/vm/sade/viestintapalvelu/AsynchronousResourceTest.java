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

import fi.vm.sade.viestintapalvelu.download.DownloadResource;
import org.jretrofit.Retrofit;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;

public class AsynchronousResourceTest {
    private AsynchronousResource resource = new AsynchronousResource();

    @Test
    public void urlTo() {
        @SuppressWarnings("unused")
        Object target = new Object() {
            public String getContextPath() {
                return "/viestintapalvelu/";
            }

            public StringBuffer getRequestURL() {
                StringBuffer buffer = new StringBuffer();
                buffer.append("https://itest-virkailija.oph.ware.fi/viestintapalvelu/api/v1/download/1a6c5852-52c3-46d3-a649-861c8e887300");
                return buffer;
            }

            public String getServletPath() {
                return "/api/v1/";
            }
        };
        HttpServletRequest request = Retrofit.partial(target,
                HttpServletRequest.class);
        assertEquals(
                "https://itest-virkailija.oph.ware.fi/viestintapalvelu/api/v1/download",
                resource.urlTo(request, DownloadResource.class));
    }
}
