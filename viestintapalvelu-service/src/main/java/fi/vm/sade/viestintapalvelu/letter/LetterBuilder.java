package fi.vm.sade.viestintapalvelu.letter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.HtmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.XmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.attachment.AttachmentService;
import fi.vm.sade.viestintapalvelu.attachment.dto.LetterReceiverLEtterAttachmentSaveDto;
import fi.vm.sade.viestintapalvelu.attachment.impl.AttachmentUri;
import fi.vm.sade.viestintapalvelu.conversion.AddressLabelConverter;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.DocumentMetadata;
import fi.vm.sade.viestintapalvelu.document.MergedPdfDocument;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;
import fi.vm.sade.viestintapalvelu.email.EmailSourceData;
import fi.vm.sade.viestintapalvelu.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.viestintapalvelu.externalinterface.component.EmailComponent;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterBatchDetails;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.UsedTemplate;
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

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ObjectMapperProvider objectMapperProvider;

    @Inject
    public LetterBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    public byte[] printZIP(List<LetterReceiverLetter> receivers, String templateName, String zipName) {
        MergedPdfDocument pdf = getMergedPDFDocument(receivers);
        try {
            return getIpostiZip(pdf, templateName, zipName);
        } catch (IOException e) {
            LOG.error("Iposti generation failed ", e);
        }

        return null;
    }
    
    public byte[] printZIP(LetterBatch batch) throws IOException, DocumentException, Exception {
        boolean valid = LetterBatchValidator.isValid(batch);
        LOG.debug("Validated batch result: " + valid);

        Map<String, byte[]> subZips = new HashMap<String, byte[]>();
        List<LetterBatch> subBatches = batch.split(Constants.IPOST_BATCH_LIMIT);
        for (int i = 0; i < subBatches.size(); i++) {
            LetterBatch subBatch = subBatches.get(i);
            MergedPdfDocument pdf = buildPDF(subBatch);
            batch.setTemplateId(subBatch.getTemplateId()); // buildPDF fetches
                                                           // template id
            String templateName = batch.getTemplateName();
            String zipName = templateName + "_" + batch.getLanguageCode() + "_" + (i + 1) + ".zip";
            byte[] zip = getIpostiZip(pdf, templateName, zipName);
            subZips.put(zipName, zip);
            batch.addIPostiData(zipName, zip);
        }
        byte[] resultZip = documentBuilder.zip(subZips);
        letterService.createLetter(batch);
        return resultZip;
    }

    private byte[] getIpostiZip(MergedPdfDocument pdf, String templateName, String zipName) throws IOException {
        Map<String, Object> context = createIPostDataContext(pdf.getDocumentMetadata());
        context.put("filename", templateName + ".pdf");
        byte[] ipostXml = documentBuilder.applyTextTemplate(Constants.LETTER_IPOST_TEMPLATE, context);
        Map<String, byte[]> documents = new HashMap<String, byte[]>();
        pdf.flush();
        documents.put(templateName + ".pdf", pdf.toByteArray());
        documents.put(templateName + ".xml", ipostXml);
        byte[] zip = documentBuilder.zip(documents);
        return zip;
    }
    
    public byte[] printPDF(LetterBatch batch) throws IOException, DocumentException, Exception {
        boolean valid = LetterBatchValidator.isValid(batch);
        LOG.debug("Validated batch result: " + valid);

        MergedPdfDocument resultPDF = buildPDF(batch);
        // store batch to database
        letterService.createLetter(batch);
        return resultPDF.toByteArray();
    }

    private MergedPdfDocument getMergedPDFDocument(List<LetterReceiverLetter> receivers) {
        MergedPdfDocument result = null;
        try {
            result = new MergedPdfDocument();
            for (LetterReceiverLetter l : receivers) {
                LetterReceivers letterReceiver = l.getLetterReceivers();
                AddressLabel address = AddressLabelConverter.convert(letterReceiver.getLetterReceiverAddress());
                PdfDocument document = new PdfDocument(address, l.getLetter());
                result.write(document);
            }
        } catch (Exception e) {
            LOG.error("PDF building failed ", e);
        }
        return result;
    }

    private MergedPdfDocument buildPDF(LetterBatch batch) throws IOException, DocumentException {

        Template baseTemplate = getBaseTemplate(batch);
        Map<String, Object> baseReplacements = getTemplateReplacements(baseTemplate);

        List<PdfDocument> source = new ArrayList<PdfDocument>();

        // For updating letters content with the generated PdfDocument
        List<Letter> updatedLetters = new LinkedList<Letter>();

        for (Letter letter : batch.getLetters()) {
            // Use the base template and base replacements by default
            Template letterTemplate = baseTemplate;
            Map<String, Object> letterReplacements = baseReplacements;

            // Letter language != template language
            if (languageIsDifferent(baseTemplate, letter)) {
                // Get the template in user specific language
                Template template = templateService.getTemplateByName(
                        new TemplateCriteriaImpl().withName(letterTemplate.getName()).withLanguage(letter.getLanguageCode())
                                .withApplicationPeriod(batch.getApplicationPeriod()), true);
                if (template != null) {
                    letterTemplate = template;
                    letterReplacements = getTemplateReplacements(letterTemplate);
                }
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> dataContext = createDataContext(baseTemplate, letter.getAddressLabel(), letterReplacements, // Template
                                                                                                                            // replacements
                                                                                                                            // defaults
                                                                                                                            // from
                                                                                                                            // template
                    batch.getTemplateReplacements(), // LetterBatch replacements
                                                     // common for all
                                                     // recipients
                    letter.getTemplateReplacements()); // Letter recipient level
                                                       // replacements

            if (letterTemplate != null) {
                AttachmentUri attachmentUri = null;
                List<TemplateContent> contents = letterTemplate.getContents();
                PdfDocument currentDocument = new PdfDocument(letter.getAddressLabel());
                Collections.sort(contents);
                for (TemplateContent tc : contents) {
                    byte[] page = createPagePdf(letterTemplate, tc.getContent().getBytes(), dataContext);
                    currentDocument.addContent(page);
                }
                source.add(currentDocument);
                letter.setLetterContent(new LetterContent(documentBuilder.merge(currentDocument).toByteArray(), "application/pdf", new Date()));
            }

            updatedLetters.add(letter);
        }

        // Write LetterBatch to DB
        batch.setLetters(updatedLetters); // Contains now the generated
                                          // PdfDocuments
        // letterService.createLetter(batch);

        return documentBuilder.merge(source);
    }

    public void initTemplateId(LetterBatchDetails batch) {
        initTemplateId(batch, batch.getTemplate());
    }

    public Template initTemplateId(LetterBatchDetails batch, Template template) {
        if (template == null && batch.getTemplateName() != null && batch.getLanguageCode() != null) {
            template = templateService.getTemplateByName(new TemplateCriteriaImpl().withName(batch.getTemplateName()).withLanguage(batch.getLanguageCode())
                    .withApplicationPeriod(batch.getApplicationPeriod()), true);
            if (template != null) {
                batch.setTemplateId(template.getId()); // Search was by name ==>
                                                       // update also to
                                                       // template Id
            }
        }

        if (template == null && batch.getTemplateId() != null) { // If not found
                                                                 // by name
            long templateId = batch.getTemplateId();
            template = templateService.findById(templateId);
        }
        return template;
    }

    private boolean languageIsDifferent(Template baseTemplate, Letter letter) {
        return letter.getLanguageCode() != null && !letter.getLanguageCode().equalsIgnoreCase(baseTemplate.getLanguage());
    }

    private Template getBaseTemplate(LetterBatch batch) throws IOException {
        // Get the given value
        Template template = batch.getTemplate();

        // Search template by name
        if (template == null && batch.getTemplateName() != null && batch.getLanguageCode() != null) {
            template = templateService.getTemplateByName(new TemplateCriteriaImpl().withName(batch.getTemplateName()).withLanguage(batch.getLanguageCode())
                    .withApplicationPeriod(batch.getApplicationPeriod()), true);
            batch.setTemplateId(template.getId()); // update template Id
        }

        // Search template by id
        if (template == null && batch.getTemplateId() != null) {
            template = templateService.findById(batch.getTemplateId());
        }

        // Fail, if template is still not found
        if (template == null) {
            throw new IOException("Could not locate template resource.");
        }
        return template;
    }

    private Map<String, Object> getTemplateReplacements(Template template) {
        Map<String, Object> replacements = new HashMap<String, Object>();
        for (Replacement r : template.getReplacements()) {
            replacements.put(r.getName(), r.getDefaultValue());
        }
        return replacements;
    }

    /**
     * Create content
     * 
     * @param templateName
     * @param languageCode
     * @param type
     * @return email content
     */
    public String getTemplateContent(String templateName, String languageCode, String type) throws IOException, DocumentException {

        // Get the template
        Template template = templateService.getTemplateByName(templateName, languageCode, type);
        if (template == null)
            throw new IOException("could not locate template resource.");

        // Get template replacements
        Map<String, Object> templateReplacements = getTemplateReplacements(template);

        List<byte[]> source = new ArrayList<byte[]>();

        if (template != null) {
            List<TemplateContent> contents = template.getContents();

            Collections.sort(contents);

            // Generate each page individually
            for (TemplateContent tc : contents) {
                byte[] page = createPageXhtml(template, tc.getContent().getBytes(), templateReplacements);
                source.add(page);
            }
        }

        byte[] result = documentBuilder.mergeByte(source);

        return new String(result);
    }

    public byte[] createPagePdf(Template template, byte[] pageContent, AddressLabel addressLabel, Map<String, Object> templReplacements,
            Map<String, Object> letterBatchReplacements, Map<String, Object> letterReplacements) throws FileNotFoundException, IOException, DocumentException {

        @SuppressWarnings("unchecked")
        Map<String, Object> dataContext = createDataContext(template, addressLabel, templReplacements, letterBatchReplacements, letterReplacements);
        byte[] xhtml = documentBuilder.applyTextTemplate(pageContent, dataContext);
        return documentBuilder.xhtmlToPDF(xhtml);
    }

    private byte[] createPagePdf(Template template, byte[] pageContent, Map<String, Object> dataContext) throws FileNotFoundException, IOException,
            DocumentException {

        @SuppressWarnings("unchecked")
        // Map<String, Object> dataContext = createDataContext(template,
        // addressLabel, templReplacements, letterBatchReplacements,
        // letterReplacements);
        byte[] xhtml = documentBuilder.applyTextTemplate(pageContent, dataContext);
        return documentBuilder.xhtmlToPDF(xhtml);
    }

    /**
     * Create page as XHTML
     *
     * @param template
     * @param pageContent
     * @param templateReplacements
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws DocumentException
     */
    private byte[] createPageXhtml(Template template, byte[] pageContent, Map<String, Object> templateReplacements) throws FileNotFoundException, IOException,
            DocumentException {

        @SuppressWarnings("unchecked")
        Map<String, Object> dataContext = createDataContext(template, templateReplacements);
        return documentBuilder.applyTextTemplate(pageContent, dataContext);
    }

    public Map<String, Object> createDataContext(Template template, AddressLabel addressLabel, Map<String, Object>... replacementsList) {

        Map<String, Object> data = new HashMap<String, Object>();
        for (Map<String, Object> replacements : replacementsList) {
            if (replacements != null) {
                for (String key : replacements.keySet()) {
                    if (replacements.get(key) instanceof String) {
                        data.put(key, cleanHtmlFromApi((String) replacements.get(key)));
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
        data.put("letterDate", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        data.put("osoite", new HtmlAddressLabelDecorator(addressLabel));
        data.put("addressLabel", new XmlAddressLabelDecorator(addressLabel));

        // liite specific handling
        if (data.containsKey("tulokset")) {
            List<Map<String, String>> tulokset = (List<Map<String, String>>) data.get("tulokset");
            Map<String, Boolean> columns = distinctColumns(tulokset);
            data.put("tulokset", normalizeColumns(columns, tulokset));
            data.put("columns", columns);
        }
        if (data.containsKey("muut_hakukohteet")) {
            List<String> muidenHakukohteidenNimet = (List<String>) data.get("muut_hakukohteet");
            data.put("muut_hakukohteet", muidenHakukohteidenNimet);
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
    private Map<String, Object> createDataContext(Template template, Map<String, Object>... replacementsList) {

        Map<String, Object> data = new HashMap<String, Object>();
        for (Map<String, Object> replacements : replacementsList) {
            if (replacements != null) {
                for (String key : replacements.keySet()) {
                    if (replacements.get(key) instanceof String) {
                        data.put(key, cleanHtmlFromApi((String) replacements.get(key)));
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

    private List<Map<String, String>> normalizeColumns(Map<String, Boolean> columns, List<Map<String, String>> tulokset) {
        if (tulokset == null) {
            return null;
        }
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

    private Map<String, Boolean> distinctColumns(List<Map<String, String>> tulokset) {
        Map<String, Boolean> printedColumns = new HashMap<String, Boolean>();
        if (tulokset == null) {
            return printedColumns;
        }
        for (Map<String, String> haku : tulokset) {
            if (haku == null) {
                continue;
            }
            for (String column : haku.keySet()) {
                printedColumns.put(column, true);
            }
        }
        return printedColumns;
    }

    private Map<String, Object> createIPostDataContext(final List<DocumentMetadata> documentMetadataList) {
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

    private String cleanHtmlFromApi(String string) {
        return Jsoup.clean(string, Whitelist.relaxed());
    }

    private boolean shouldReceiveEmail(Letter letter) {
        return (letter.getEmailAddress() != null && !letter.getEmailAddress().isEmpty());
    }

    private void sendEmail(EmailSourceData source) throws IOException {
        if (source != null) {
            emailComponent.sendEmail(source);
        }
    }

    public void constructPDFForLetterReceiverLetter(LetterReceivers receiver, fi.vm.sade.viestintapalvelu.model.LetterBatch batch,
            Map<String, Object> batchReplacements, Map<String, Object> letterReplacements) throws IOException, DocumentException {
        LetterReceiverLetter letter = receiver.getLetterReceiverLetter();

        Template template = determineTemplate(receiver, batch);

        Map<String, Object> templReplacements = formReplacementMap(template.getReplacements());

        ObjectMapper mapper = objectMapperProvider.getContext(getClass());
        templReplacements.putAll(formReplacementMap(letter.getLetterReceivers().getLetterReceiverReplacement(), mapper));

        List<TemplateContent> contents = template.getContents();
        AddressLabel address = AddressLabelConverter.convert(letter.getLetterReceivers().getLetterReceiverAddress());
        PdfDocument currentDocument = new PdfDocument(address);
        Collections.sort(contents);

        for (TemplateContent tc : contents) {
            if (!tc.getName().equalsIgnoreCase("email_body")) {
                byte[] page = createPagePdf(template, tc.getContent().getBytes(), address, templReplacements, batchReplacements, letterReplacements);
                if (letter.getLetterReceivers().getEmailAddress() != null && !letter.getLetterReceivers().getEmailAddress().isEmpty()
                        && "liite".equals(tc.getName()) && receiver.getLetterReceiverEmail() == null) {
                    saveLetterReceiverAttachment(tc.getName(), page, receiver.getLetterReceiverLetter().getId());
                }
                currentDocument.addContent(page);
            }
        }
        letter.setLetter(documentBuilder.merge(currentDocument).toByteArray());
        letter.setContentType("application/pdf");
       
    }

    private long saveLetterReceiverAttachment(String name, byte[] page, long letterReceiverLetterId) {
        LetterReceiverLEtterAttachmentSaveDto attachment = new LetterReceiverLEtterAttachmentSaveDto();
        attachment.setName(Optional.fromNullable(name).or("liite") + ".pdf");
        attachment.setContentType("application/pdf");
        attachment.setContents(page);
        attachment.setLetterReceiverLetterId(letterReceiverLetterId);
        return attachmentService.saveReceiverAttachment(attachment);
    }

    public Template determineTemplate(LetterReceivers receiver, fi.vm.sade.viestintapalvelu.model.LetterBatch batch) {
        if (receiver.getWantedLanguage() != null) {
            for (UsedTemplate usedTemplate : batch.getUsedTemplates()) {
                if (usedTemplate.getTemplate().getLanguage().equals(receiver.getWantedLanguage())) {
                    return templateService.findById(usedTemplate.getTemplate().getId());
                }
            }
        }
        return templateService.findById(batch.getTemplateId());
    }

    public Map<String, Object> formReplacementMap(Set<LetterReceiverReplacement> replacements, ObjectMapper mapper) throws IOException {
        Map<String, Object> templReplacements = new HashMap<String, Object>();
        for (LetterReceiverReplacement replacement : replacements) {
            templReplacements.put(replacement.getName(), replacement.getEffectiveValue(mapper));
        }
        return templReplacements;
    }

    public Map<String, Object> formReplacementMap(List<Replacement> replacements) {
        Map<String, Object> templReplacements = new HashMap<String, Object>();
        for (Replacement templRepl : replacements) {
            templReplacements.put(templRepl.getName(), templRepl.getDefaultValue());
        }
        return templReplacements;
    }

    public void setObjectMapperProvider(ObjectMapperProvider objectMapperProvider) {
        this.objectMapperProvider = objectMapperProvider;
    }
}
