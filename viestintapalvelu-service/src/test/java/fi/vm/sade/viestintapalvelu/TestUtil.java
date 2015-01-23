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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFText2HTML;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.vm.sade.viestintapalvelu.address.AddressLabelBatch;
import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;

public class TestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtil.class);

    private static final String ADDRESS_LABEL_PDF_URL = "http://localhost:" + 1024 + "/api/v1/addresslabel/pdf";
    private static final String ADDRESS_LABEL_XLS_URL = "http://localhost:" + 1024 + "/api/v1/addresslabel/xls";

    public static List<List<String>> generateAddressLabelsPDF(List<AddressLabel> labels) throws Exception {
        AddressLabelBatch batch = new AddressLabelBatch(labels);
        return readPDF(get(batch, ADDRESS_LABEL_PDF_URL), -1, -1);
    }

    public static List<List<String>> generateAddressLabelsXLS(List<AddressLabel> labels) throws Exception {
        AddressLabelBatch batch = new AddressLabelBatch(labels);
        return readXLS(get(batch, ADDRESS_LABEL_XLS_URL));
    }

    private static byte[] get(Object json, String url) throws IOException, DocumentException {
        //Post
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json;charset=utf-8");
        post.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(json), ContentType.APPLICATION_JSON));
        HttpResponse response = client.execute(post);

        //Get
        LOGGER.info("Response " + response.toString());
        String resultUrl = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        HttpGet get = new HttpGet(resultUrl);
        response = client.execute(get);
        return IOUtils.toByteArray(response.getEntity().getContent());
    }

    private static List<List<String>> readPDF(byte[] byteDocument, int startPage, int endPage) throws IOException {
        return parseHTML(readAsHtml(byteDocument, startPage, endPage).getBytes());
    }

    private static String readAsHtml(byte[] byteDocument, int startPage, int endPage) throws IOException {
        PDDocument document = PDDocument.load(new ByteArrayInputStream(byteDocument));
        PDFText2HTML stripper = new PDFText2HTML("UTF-8");
        StringWriter writer = new StringWriter();
        if (startPage > 0) {
            stripper.setStartPage(startPage);
        }
        if (endPage > 0) {
            stripper.setEndPage(endPage);
        }
        stripper.setLineSeparator("<br/>");
        stripper.writeText(document, writer);
        document.close();
        return writer.toString();
    }

    @SuppressWarnings("unchecked")
    private static List<List<String>> readXLS(byte[] byteDocument) throws Exception {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new ByteArrayInputStream(byteDocument));
        List<List<String>> labels = new ArrayList<List<String>>();
        List<Node> rows = xpath("//html40:tr").selectNodes(doc);
        for (Node row : rows) {
            List<String> rowContent = new ArrayList<String>();
            labels.add(rowContent);
            List<Node> columns = xpath("./html40:*").selectNodes(row);
            for (Node column : columns) {
                rowContent.add(column.getText());
            }
        }
        return labels;
    }

    private static XPath xpath(String selector) {
        Map<String, String> namespaceUris = new HashMap<String, String>();
        namespaceUris.put("html40", "http://www.w3.org/TR/REC-html40");
        XPath xPath = DocumentHelper.createXPath(selector);
        xPath.setNamespaceURIs(namespaceUris);
        return xPath;
    }

    private static Tidy newTidy() {
        Tidy tidy = new Tidy();
        tidy.setTidyMark(false);
        tidy.setDocType("omit");
        tidy.setXHTML(true);
        tidy.setCharEncoding(Configuration.UTF8);
        tidy.setQuiet(true);
        return tidy;
    }

    private static List<List<String>> parseHTML(byte[] document) {
        org.w3c.dom.Document doc = newTidy().parseDOM(new ByteArrayInputStream(document), new ByteArrayOutputStream());
        List<List<String>> nodes = new ArrayList<List<String>>();
        NodeList p = doc.getElementsByTagName("p");
        int i = 0;
        while (i < p.getLength()) {
            NodeList textNodes = p.item(i).getChildNodes();
            int j = 0;
            List<String> texts = new ArrayList<String>();
            while (j < textNodes.getLength()) {
                if (textNodes.item(j).getNodeType() == Node.TEXT_NODE) {
                    texts.add(textNodes.item(j).getNodeValue());
                }
                j++;
            }
            nodes.add(texts);
            i++;
        }
        return nodes;
    }

    //private static final Condition<String> url = new Condition<String>("url")

}
