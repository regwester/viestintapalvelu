package fi.vm.sade.viestintapalvelu.letter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.HtmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.XmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.DocumentMetadata;
import fi.vm.sade.viestintapalvelu.document.MergedPdfDocument;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;
import fi.vm.sade.viestintapalvelu.externalinterface.component.EmailComponent;
import fi.vm.sade.viestintapalvelu.template.Replacement;
import fi.vm.sade.viestintapalvelu.template.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateContent;
import fi.vm.sade.viestintapalvelu.template.TemplateService;
import fi.vm.sade.viestintapalvelu.validator.LetterBatchValidator;

@Service
@Singleton
public class LetterBuilder {

    private final Logger LOG = LoggerFactory.getLogger(LetterBuilder.class);

    private DocumentBuilder documentBuilder;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private LetterService letterService;

    @Autowired
    private EmailComponent emailComponent;
    
    @Inject
    public LetterBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    
    public byte[] printZIP(LetterBatch batch) throws IOException, DocumentException, Exception {
        LetterBatchValidator.validate(batch);
        LOG.debug("Validated batch result");
        
        Map<String, byte[]> subZips = new HashMap<String, byte[]>();
        List<LetterBatch> subBatches = batch.split(Constants.IPOST_BATCH_LIMIT);
        for (int i = 0; i < subBatches.size(); i++) {
            LetterBatch subBatch = subBatches.get(i);
            MergedPdfDocument pdf = buildPDF(subBatch);
            batch.setTemplateId(subBatch.getTemplateId()); // buildPDF fetches template id
            
            Map<String, Object> context = createIPostDataContext(pdf.getDocumentMetadata());
            String templateName = batch.getTemplateName();
            context.put("filename", templateName + ".pdf");
            byte[] ipostXml = documentBuilder.applyTextTemplate(Constants.LETTER_IPOST_TEMPLATE, context);
            
            Map<String, byte[]> documents = new HashMap<String, byte[]>();
            documents.put(templateName + ".pdf", pdf.toByteArray());
            documents.put(templateName + ".xml", ipostXml);
            String zipName = templateName + "_" + batch.getLanguageCode() + "_" + (i + 1) + ".zip";
            byte[] zip = documentBuilder.zip(documents);
            subZips.put(zipName,zip);
            batch.addIPostiData(zipName,zip);
        }
        byte[] resultZip = documentBuilder.zip(subZips);
        letterService.createLetter(batch);
        return resultZip;
    }

    public byte[] printPDF(LetterBatch batch) throws IOException,
            DocumentException, Exception {

        LetterBatchValidator.validate(batch);
        LOG.debug("Validated batch result");

        MergedPdfDocument resultPDF = buildPDF(batch);
        // store batch to database
        letterService.createLetter(batch);
        return resultPDF.toByteArray();
    }

    private MergedPdfDocument buildPDF(LetterBatch batch) throws IOException,
            DocumentException {

        Template template = batch.getTemplate();

        template = initTemplateId(batch, template);

        if (template == null) {
            // still null ??
            throw new IOException("could not locate template resource.");
        }

        Map<String, Object> templReplacements = new HashMap<String, Object>();
        for (Replacement templRepl : template.getReplacements()) {
            templReplacements.put(templRepl.getName(),
                    templRepl.getDefaultValue());
        }

        List<PdfDocument> source = new ArrayList<PdfDocument>();

        // For updating letters content with the generated PdfDocument
        List<Letter> updateLetters = new LinkedList<Letter>();

        
        // loop trough
        for (Letter letter : batch.getLetters()) {
            // letter.getTemplateReplacements(); ????????????
            // By default use LetterBatch template
            Template letterTemplate = template;
            Map<String, Object> letterTemplReplacements = templReplacements;

            // If user specific language is defined use recipient specific
            // template language
            if (letter.getLanguageCode() != null
                    && !letter.getLanguageCode().equalsIgnoreCase(
                            template.getLanguage())) {
                Template temp = templateService.getTemplateByName(letterTemplate.getName(), letter.getLanguageCode());
                if (temp != null) {
                    letterTemplate = temp;
                    letterTemplReplacements = new HashMap<String, Object>();
                    for (Replacement templRepl : letterTemplate.getReplacements()) {
                        letterTemplReplacements.put(templRepl.getName(),
                                templRepl.getDefaultValue());
                    }
                }
            }

            if (letterTemplate != null) {
                List<TemplateContent> contents = letterTemplate.getContents();
                PdfDocument currentDocument = new PdfDocument(letter.getAddressLabel());
                Collections.sort(contents);
                for (TemplateContent tc : contents) {
                    byte[] page = createPagePdf(letterTemplate, tc.getContent()
                            .getBytes(), letter.getAddressLabel(),
                            letterTemplReplacements, // Template, basic replacement
                            batch.getTemplateReplacements(), // LetterBatch, (last) sent replacements
                            // that might have overwritten the template values
                            letter.getTemplateReplacements()); // Letter, e.g. student results, addressLabel, ...
                    currentDocument.addContent(page);
                }
                source.add(currentDocument);
                letter.setLetterContent(new LetterContent(documentBuilder.merge(currentDocument).toByteArray(), "application/pdf", new Date()));
            }
            sendEmail(letter);
            updateLetters.add(letter);
        }

        // Write LetterBatch to DB
        batch.setLetters(updateLetters); // Contains now the generated PdfDocuments
        // letterService.createLetter(batch);

        return documentBuilder.merge(source);
    }
    
    public void initTemplateId(LetterBatch batch) {
        initTemplateId(batch, batch.getTemplate());
    }

    private Template initTemplateId(LetterBatch batch, Template template) {
        if (template == null && batch.getTemplateName() != null
                && batch.getLanguageCode() != null) {
            template = templateService.getTemplateByName(
                    batch.getTemplateName(), batch.getLanguageCode());

            batch.setTemplateId(template.getId()); // Search was by name ==> update also to template Id
        }

        if (template == null && batch.getTemplateId() != null) { // If not found by name
            long templateId = batch.getTemplateId();
            template = templateService.findById(templateId);
        }
        return template;
    }

    /**
     * Create content
     * 
     * @param templateName
     * @param languageCode
     * @param type
     * @return email content
     */
    public String getTemplateContent(String templateName, String languageCode,
            String type) throws IOException, DocumentException {

        // Get the template
        Template template = templateService.getTemplateByName(templateName,
                languageCode, type);
        if (template == null)
            throw new IOException("could not locate template resource.");

        // Get template replacements
        Map<String, Object> templateReplacements = new HashMap<String, Object>();
        for (Replacement templRepl : template.getReplacements()) {
            templateReplacements.put(templRepl.getName(),
                    templRepl.getDefaultValue());
        }

        List<byte[]> source = new ArrayList<byte[]>();

        if (template != null) {
            List<TemplateContent> contents = template.getContents();

            Collections.sort(contents);

            // Generate each page individually
            for (TemplateContent tc : contents) {
                byte[] page = createPageXhtml(template, tc.getContent()
                        .getBytes(), templateReplacements);
                source.add(page);
            }
        }

        byte[] result = documentBuilder.mergeByte(source);

        return new String(result);
    }

    private byte[] createPagePdf(Template template, byte[] pageContent,
            AddressLabel addressLabel, Map<String, Object> templReplacements,
            Map<String, Object> letterBatchReplacements,
            Map<String, Object> letterReplacements)
            throws FileNotFoundException, IOException, DocumentException {

        @SuppressWarnings("unchecked")
        Map<String, Object> dataContext = createDataContext(template,
                addressLabel, templReplacements, letterBatchReplacements,
                letterReplacements);
        byte[] xhtml = documentBuilder.applyTextTemplate(pageContent,
                dataContext);
        return documentBuilder.xhtmlToPDF(xhtml);
    }

    /**
     * Create page as XHTML
     * 
     * @param template
     * @param pageContent
     * @param addressLabel
     * @param templReplacements
     * @param letterBatchReplacements
     * @param letterReplacements
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws DocumentException
     */
    private byte[] createPageXhtml(Template template, byte[] pageContent,
            Map<String, Object> templateReplacements)
            throws FileNotFoundException, IOException, DocumentException {

        @SuppressWarnings("unchecked")
        Map<String, Object> dataContext = createDataContext(template,
                templateReplacements);
        return documentBuilder.applyTextTemplate(pageContent, dataContext);
    }

    private Map<String, Object> createDataContext(Template template,
            AddressLabel addressLabel, Map<String, Object>... replacementsList) {

        Map<String, Object> data = new HashMap<String, Object>();
        for (Map<String, Object> replacements : replacementsList) {
            if (replacements != null) {
                for (String key : replacements.keySet()) {
                    if (replacements.get(key) instanceof String) {
                        data.put(
                                key,
                                cleanHtmlFromApi((String) replacements.get(key)));
                    } else {
                        data.put(key, replacements.get(key));
                    }
                }
            }
        }

        String styles = template.getStyles();
        if (styles == null) {
            styles = "";
        }
        data.put("letterDate",
                new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        data.put("osoite", new HtmlAddressLabelDecorator(addressLabel));
        data.put("addressLabel", new XmlAddressLabelDecorator(addressLabel));

        // liite specific handling
        if (data.containsKey("tulokset")) {
            List<Map<String, String>> tulokset = (List<Map<String, String>>) data
                    .get("tulokset");
            Map<String, Boolean> columns = distinctColumns(tulokset);
            data.put("tulokset", normalizeColumns(columns, tulokset));
            data.put("columns", columns);
        }

        data.put("tyylit", styles);
        return data;
    }

    /**
     * Create data context
     * 
     * @param template
     * @param replacementsList
     * @return
     */
    private Map<String, Object> createDataContext(Template template,
            Map<String, Object>... replacementsList) {

        Map<String, Object> data = new HashMap<String, Object>();
        for (Map<String, Object> replacements : replacementsList) {
            if (replacements != null) {
                for (String key : replacements.keySet()) {
                    if (replacements.get(key) instanceof String) {
                        data.put(
                                key,
                                cleanHtmlFromApi((String) replacements.get(key)));
                    } else {
                        data.put(key, replacements.get(key));
                    }
                }
            }
        }

        String styles = template.getStyles();
        if (styles == null) {
            styles = "";
        }
        data.put("tyylit", styles);
        return data;
    }

    private List<Map<String, String>> normalizeColumns(
            Map<String, Boolean> columns, List<Map<String, String>> tulokset) {
        for (Map<String, String> row : tulokset) {
            for (String column : columns.keySet()) {
                if (!row.containsKey(column) || row.get(column) == null) {
                    row.put(column, "");
                }
                row.put(column, cleanHtmlFromApi(row.get(column)));
            }
        }
        return tulokset;
    }

    private Map<String, Boolean> distinctColumns(
            List<Map<String, String>> tulokset) {
        Map<String, Boolean> printedColumns = new HashMap<String, Boolean>();
        for (Map<String, String> haku : tulokset) {
            for (String column : haku.keySet()) {
                printedColumns.put(column, true);
            }
        }
        return printedColumns;
    }

    private Map<String, Object> createIPostDataContext(
            final List<DocumentMetadata> documentMetadataList) {
        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, Object>> metadataList = new ArrayList<Map<String, Object>>();
        for (DocumentMetadata documentMetadata : documentMetadataList) {
            Map<String, Object> metadata = new HashMap<String, Object>();
            metadata.put("startPage", documentMetadata.getStartPage());
            metadata.put("pages", documentMetadata.getPages());
            metadata.put("addressLabel", new XmlAddressLabelDecorator(
                    documentMetadata.getAddressLabel()));
            metadataList.add(metadata);
        }
        data.put("metadataList", metadataList);
        data.put("ipostTest", Constants.IPOST_TEST);
        return data;
    }

    private String cleanHtmlFromApi(String string) {
        return Jsoup.clean(string, Whitelist.relaxed());
    }
    
    private void sendEmail(Letter letter) throws IOException {
        //if (letter.getEmailAddress() != null && letter.getEmailAddress().length() > 0) {
            emailComponent.sendEmail(letter);
        //}
    }
}
