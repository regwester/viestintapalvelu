package fi.vm.sade.viestintapalvelu.letter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;
import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.AddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.HtmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.XmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.DocumentMetadata;
import fi.vm.sade.viestintapalvelu.document.MergedPdfDocument;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;
import fi.vm.sade.viestintapalvelu.template.Replacement;
import fi.vm.sade.viestintapalvelu.template.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateContent;
import fi.vm.sade.viestintapalvelu.template.TemplateService;

import fi.vm.sade.viestintapalvelu.Constants;

@Service
@Singleton
public class LetterBuilder {

    private DocumentBuilder documentBuilder;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private LetterService letterService;

    @Inject
    public LetterBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    public byte[] printZIP(LetterBatch batch) throws IOException,
            DocumentException {
        Map<String, byte[]> subZips = new HashMap<String, byte[]>();
        List<LetterBatch> subBatches = batch.split(Constants.IPOST_BATCH_LIMIT);
        for (int i = 0; i < subBatches.size(); i++) {
            LetterBatch subBatch = subBatches.get(i);
            MergedPdfDocument pdf = buildPDF(subBatch);
            Map<String, Object> context = createIPostDataContext(pdf.getDocumentMetadata());
            context.put("filename", batch.getTemplateName()+".pdf");
            byte[] ipostXml = documentBuilder.applyTextTemplate(Constants.LETTER_IPOST_TEMPLATE, context);
            Map<String, byte[]> documents = new HashMap<String, byte[]>();
            documents.put(batch.getTemplateName()+".pdf", pdf.toByteArray());
            documents.put(batch.getTemplateName()+".xml", ipostXml);
            subZips.put(batch.getTemplateName()+"_"+ (i + 1) + ".zip", documentBuilder.zip(documents));
        }
        return documentBuilder.zip(subZips);
    }

    public byte[] printPDF(LetterBatch batch) throws IOException,
            DocumentException {

        return buildPDF(batch).toByteArray();
    }

    private MergedPdfDocument buildPDF(LetterBatch batch) throws IOException, DocumentException {

        Template template = batch.getTemplate();

        if (template == null && batch.getTemplateName() != null
                && batch.getLanguageCode() != null) {
            template = templateService.getTemplateByName(
                    batch.getTemplateName(), batch.getLanguageCode());
            batch.setTemplateId(template.getId()); // Search was by name ==>
                                                   // update also to template Id
        }

        if (template == null && batch.getTemplateId() != null) {
            long templateId = batch.getTemplateId();
            template = templateService.findById(templateId);
        }

        if (template == null) {
            // still null ??
            throw new IOException("could not locate template resource.");
        }

        // Write LetterBatch to DB
        fi.vm.sade.viestintapalvelu.model.LetterBatch lb = letterService
                .createLetter(batch);

        Map<String, Object> templReplacements = new HashMap<String, Object>();
        for (Replacement templRepl : template.getReplacements()) {
            templReplacements.put(templRepl.getName(),
                    templRepl.getDefaultValue());
        }

        List<PdfDocument> source = new ArrayList<PdfDocument>();
        for (Letter letter : batch.getLetters()) {
            letter.getTemplateReplacements();
            // By default use LetterBatch tempalate
            Template letterTemplate = template;
            Map<String, Object> letterTemplReplacements = templReplacements;

            // If user specific language is defined use recipient specific
            // template language
            if (letter.getLanguageCode() != null
                    && !letter.getLanguageCode().equalsIgnoreCase(
                            template.getLanguage())) {
                Template temp = templateService.getTemplateByName(
                        letterTemplate.getName(), letter.getLanguageCode());
                if (temp != null) {
                    letterTemplate = temp;
                    letterTemplReplacements = new HashMap<String, Object>();
                    for (Replacement templRepl : letterTemplate
                            .getReplacements()) {
                        letterTemplReplacements.put(templRepl.getName(),
                                templRepl.getDefaultValue());
                    }
                }
            }

            if (letterTemplate != null) {
                List<TemplateContent> contents = letterTemplate.getContents();
                Collections.sort(contents);
                for (TemplateContent tc : contents) {
                    byte[] page = createPagePdf(letterTemplate, tc.getContent()
                            .getBytes(), letter.getAddressLabel(),
                            letterTemplReplacements, // Template, basic
                                                     // replacement
                            batch.getTemplateReplacements(), // LetterBatch,
                                                             // (last) sent
                                                             // replacements
                                                             // that might have
                                                             // overwritten the
                                                             // template values
                            letter.getTemplateReplacements()); // Letter, e.g
                                                               // student
                                                               // results,
                                                               // addressLabel,
                                                               // ...

                    PdfDocument dfDocument = new PdfDocument(letter.getAddressLabel(), page, null);
                    source.add(dfDocument);                    
//                    source.add(new PdfDocument(letter.getAddressLabel(), page, null));
                }
            }
        }
//        byte[] docu = documentBuilder.merge(source).toByteArray(); 
//        byte[] zippedDocu = zip("joku nimi", docu);
//        return docu;
        
//        // Write LetterBatch to DB
//        fi.vm.sade.viestintapalvelu.model.LetterBatch lb = letterService.createLetter(batch);
        
//        // Write LetterBatch to DB
//        fi.vm.sade.viestintapalvelu.model.LetterBatch lb = letterService.createLetter(batch);
        
        return documentBuilder.merge(source);
    }
    
    private byte[] createPagePdf(Template template, byte[] pageContent,
            AddressLabel addressLabel, Map<String, Object> templReplacements,
            Map<String, Object> letterBatchReplacements,
            Map<String, Object> letterReplacements)
            throws FileNotFoundException, IOException, DocumentException {

        @SuppressWarnings("unchecked")
        Map<String, Object> dataContext = createDataContext(template,
                new HtmlAddressLabelDecorator(addressLabel), templReplacements,
                letterBatchReplacements, letterReplacements);
        byte[] xhtml = documentBuilder.applyTextTemplate(pageContent,
                dataContext);
        return documentBuilder.xhtmlToPDF(xhtml);
    }

    private Map<String, Object> createDataContext(Template template,
            AddressLabelDecorator decorator,
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
        data.put("letterDate",
                new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        data.put("osoite", decorator);
        data.put("tyylit", styles);
        return data;
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
    
	public static byte[] zip(String attachmentName, byte[] attachment) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(attachment.length);
		
		Deflater deflater = new Deflater();  
		deflater.setInput(attachment);  
		deflater.finish(); 

		byte[] buffer = new byte[1024];   
		
		while (!deflater.finished()) {  
			int count = deflater.deflate(buffer);  
			outputStream.write(buffer, 0, count);   
		}  
		
		outputStream.close();    
		return outputStream.toByteArray();  
    }
}
