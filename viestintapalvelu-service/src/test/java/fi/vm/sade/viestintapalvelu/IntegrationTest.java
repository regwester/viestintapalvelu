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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.ClassRule;
import org.junit.Test;

public class IntegrationTest {
    //@ClassRule
    //public static TomcatRule tomcat = new TomcatRule();
    /*private String responseBody;

    @Test
    public void staticResourcesWork() throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(Urls.localhost(1024).index());
        HttpResponse response = httpClient.execute(httpGet);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void addressLabelPrinting() throws Exception {
        HttpResponse response = get("/addresslabel_pdf.json", Urls.localhost(1024).addresslabel()
                + "/pdf");
        assertStatusCodeEquals(200, response);
        assertEquals("Content-Type: application/pdf;charset=utf-8", response.getFirstHeader("Content-Type").toString());
        assertEquals("Content-Disposition: attachment; filename=\"addresslabels.pdf\"",
                response.getFirstHeader("Content-Disposition").toString());
    }

    @Test
    public void addressLabelXLSPrinting() throws Exception {
        HttpResponse response = get("/addresslabel_xls.json", Urls.localhost(1024).addresslabel()
                + "/xls");
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Content-Type: application/vnd.ms-excel", response.getFirstHeader("Content-Type").toString());
        assertEquals("Content-Disposition: attachment; filename=\"addresslabels.xls\"",
                response.getFirstHeader("Content-Disposition").toString());
    }

    private HttpResponse get(String jsonFile, String url) throws UnsupportedEncodingException, IOException,
            ClientProtocolException {
        String json = new Scanner(getClass().getResourceAsStream(jsonFile), "UTF-8").useDelimiter("\u001a").next();
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        post.setEntity(new StringEntity(json));
        HttpResponse response = client.execute(post);
        assertStatusCodeEquals(202, response);
        String downloadLink = readResponseBody(response);
        HttpGet get = new HttpGet(downloadLink);
        response = client.execute(get);
        return response;
    }

    private String readResponseBody(HttpResponse response) throws IOException {
        if (responseBody == null) {
            responseBody = IOUtils.toString(response.getEntity().getContent());
        }
        return responseBody;
    }

    private void assertStatusCodeEquals(int expected, HttpResponse response) throws IOException {
        assertEquals("HTTP status code " + expected + " expected, HTTP response was: " + response
                + readResponseBody(response), expected, response.getStatusLine().getStatusCode());
    }*/
}
