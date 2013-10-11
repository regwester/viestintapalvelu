package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import com.google.inject.Inject;
import com.lowagie.text.DocumentException;
import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.AddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.HtmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.XmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.DocumentMetadata;
import fi.vm.sade.viestintapalvelu.document.MergedPdfDocument;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;
import fi.vm.sade.viestintapalvelu.liite.LiiteBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JalkiohjauskirjeBuilder {

    private DocumentBuilder documentBuilder;
    private LiiteBuilder liiteBuilder;

    @Inject
    public JalkiohjauskirjeBuilder(final DocumentBuilder documentBuilder, final LiiteBuilder liiteBuilder) {
        this.documentBuilder = documentBuilder;
        this.liiteBuilder = liiteBuilder;
    }

    public byte[] printPDF(final JalkiohjauskirjeBatch batch) throws IOException, DocumentException {
        return createJalkiohjauskirjeBatch(batch).toByteArray();
    }

    public byte[] printZIP(final JalkiohjauskirjeBatch batch) throws IOException, DocumentException {
        Map<String, byte[]> subZips = new HashMap<String, byte[]>();
        List<JalkiohjauskirjeBatch> subBatches = batch.split(Constants.IPOST_BATCH_LIMIT);
        for (int i = 0; i < subBatches.size(); i++) {
            JalkiohjauskirjeBatch subBatch = subBatches.get(i);
            MergedPdfDocument pdf = createJalkiohjauskirjeBatch(subBatch);
            Map<String, Object> context = createDataContext(pdf.getDocumentMetadata());
            byte[] ipostXml = documentBuilder.applyTextTemplate(
                    Constants.IPOST_TEMPLATE, context);
            Map<String, byte[]> documents = new HashMap<String, byte[]>();
            documents.put("jalkiohjauskirje.pdf", pdf.toByteArray());
            documents.put("jalkiohjauskirje.xml", ipostXml);
            subZips.put("jalkiohjauskirje_" + (i + 1) + ".zip",
                    documentBuilder.zip(documents));
        }
        return documentBuilder.zip(subZips);
    }

    public MergedPdfDocument createJalkiohjauskirjeBatch(
            JalkiohjauskirjeBatch batch) throws IOException, DocumentException {
        List<PdfDocument> source = new ArrayList<PdfDocument>();
        for (Jalkiohjauskirje kirje : batch.getLetters()) {
            String kirjeTemplateName = Utils.resolveTemplateName(
                    Constants.JALKIOHJAUSKIRJE_TEMPLATE,
                    kirje.getLanguageCode());
            byte[] frontPage = createFirstPagePDF(kirjeTemplateName,
                    kirje.getAddressLabel());
            String liiteTemplateName = Utils.resolveTemplateName(
                    Constants.LIITE_TEMPLATE, kirje.getLanguageCode());
            byte[] attachment = liiteBuilder.printPDF(liiteTemplateName,
                    kirje.getTulokset());
            source.add(new PdfDocument(kirje.getAddressLabel(), frontPage,
                    attachment));
        }
        return documentBuilder.merge(source);
    }

    private byte[] createFirstPagePDF(final String templateName, final AddressLabel addressLabel)
            throws IOException, DocumentException {
        Map<String, Object> dataContext = createDataContext(new HtmlAddressLabelDecorator(
                addressLabel));
        byte[] xhtml = documentBuilder.applyTextTemplate(templateName,
                dataContext);
        return documentBuilder.xhtmlToPDF(xhtml);
    }

    private Map<String, Object> createDataContext(final AddressLabelDecorator decorator) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("osoite", decorator);
        return data;
    }

    private Map<String, Object> createDataContext(final List<DocumentMetadata> documentMetadataList) {
        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, Object>> metadataList = new ArrayList<Map<String, Object>>();
        for (DocumentMetadata documentMetadata : documentMetadataList) {
            Map<String, Object> metadata = new HashMap<String, Object>();
            metadata.put("startPage", documentMetadata.getStartPage());
            metadata.put("pages", documentMetadata.getPages());
            metadata.put("addressLabel", new XmlAddressLabelDecorator(documentMetadata.getAddressLabel()));
            metadataList.add(metadata);
        }
        data.put("metadataList", metadataList);
        data.put("ipostTest", Constants.IPOST_TEST);
        return data;
    }
}
