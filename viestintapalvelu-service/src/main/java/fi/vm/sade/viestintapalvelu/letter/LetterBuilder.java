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
 */
package fi.vm.sade.viestintapalvelu.letter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Supplier;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.address.HtmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.XmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.attachment.AttachmentService;
import fi.vm.sade.viestintapalvelu.attachment.dto.LetterReceiverLEtterAttachmentSaveDto;
import fi.vm.sade.viestintapalvelu.conversion.AddressLabelConverter;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.DocumentMetadata;
import fi.vm.sade.viestintapalvelu.document.MergedPdfDocument;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;
import fi.vm.sade.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.viestintapalvelu.externalinterface.component.EmailComponent;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterBatchDetails;
import fi.vm.sade.viestintapalvelu.letter.html.Cleaner;
import fi.vm.sade.viestintapalvelu.letter.html.XhtmlCleaner;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverReplacement;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.UsedTemplate;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.template.Contents;
import fi.vm.sade.viestintapalvelu.template.Replacement;
import fi.vm.sade.viestintapalvelu.template.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateContent;
import fi.vm.sade.viestintapalvelu.template.TemplateService;

@Service
@Singleton
@ComponentScan(value = { "fi.vm.sade.externalinterface" })
public class LetterBuilder {
    private final Logger LOG = LoggerFactory.getLogger(LetterBuilder.class);

    private DocumentBuilder documentBuilder;
    private TemplateService templateService;
    private AttachmentService attachmentService;
    private ObjectMapperProvider objectMapperProvider;

    @Autowired(required = false)
    public LetterBuilder(DocumentBuilder documentBuilder, TemplateService templateService, AttachmentService attachmentService, ObjectMapperProvider objectMapperProvider) {
        this.documentBuilder = documentBuilder;
        this.templateService = templateService;
        this.attachmentService = attachmentService;
        this.objectMapperProvider = objectMapperProvider;
    }
    public LetterBuilder() {
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

    private byte[] getIpostiZip(final MergedPdfDocument pdf, String templateName, String zipName) throws IOException {
        Map<String, Object> context = createIPostDataContext(pdf.getDocumentMetadata());
        context.put("filename", templateName + ".pdf");
        final byte[] ipostXml = documentBuilder.applyTextTemplate(Constants.LETTER_IPOST_TEMPLATE, context);
        Map<String, Supplier<byte[]>> documents = new HashMap<>();
        pdf.flush();
        documents.put(templateName + ".pdf", new Supplier<byte[]>() {
            @Override
            public byte[] get() {
                return pdf.toByteArray();
            }
        });
        documents.put(templateName + ".xml", new Supplier<byte[]>() {
            @Override
            public byte[] get() {
                return ipostXml;
            }
        });
        return documentBuilder.zip(documents);
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

    public void initTemplateId(LetterBatchDetails batch) {
        initTemplateId(batch, batch.getTemplate());
    }

    public Template initTemplateId(LetterBatchDetails batch, Template template) {
        if (template == null && batch.getTemplateName() != null && batch.getLanguageCode() != null) {
            template = templateService.getTemplateByName(new TemplateCriteriaImpl(batch.getTemplateName(),
                            batch.getLanguageCode(), ContentStructureType.letter)
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
            template = templateService.findById(templateId, ContentStructureType.letter);
        }
        return template;
    }

    public Map<String, Object> getTemplateReplacements(Template template) {
        Map<String, Object> replacements = new HashMap<>();
        for (Replacement r : template.getReplacements()) {
            replacements.put(r.getName(), r.getDefaultValue());
        }
        return replacements;
    }

    public byte[] createPagePdf(Template template, byte[] pageContent, AddressLabel addressLabel, Map<String, Object> templReplacements,
            Map<String, Object> letterBatchReplacements, Map<String, Object> letterReplacements) throws IOException, DocumentException {
        @SuppressWarnings("unchecked")
        Map<String, Object> dataContext = createDataContext(XhtmlCleaner.INSTANCE,
                template, addressLabel, templReplacements, letterBatchReplacements, letterReplacements);
        byte[] xhtml = documentBuilder.applyTextTemplate(pageContent, dataContext);
        return documentBuilder.xhtmlToPDF(xhtml);
    }

    public Map<String, Object> createDataContext(Cleaner cleaner,
                     Template template, AddressLabel addressLabel, Map<String, Object>... replacementsList) {
        Map<String, Object> data = cleanValues(cleaner, replacementsList);

        String styles = template.getStyles();
        if (styles == null) {
            styles = "";
        }
        data.put("letterDate", new SimpleDateFormat("d.M.yyyy").format(new Date()));
        data.put("osoite", new HtmlAddressLabelDecorator(addressLabel));
        data.put("addressLabel", new XmlAddressLabelDecorator(addressLabel));

        if (data.containsKey("muut_hakukohteet")) {
            List<String> muidenHakukohteidenNimet = (List<String>) data.get("muut_hakukohteet");
            data.put("muut_hakukohteet", muidenHakukohteidenNimet);
        }

        data.put("tyylit", styles);
        return data;
    }

    private Map<String, Object> cleanValues(Cleaner cleaner, Map<String, Object>... replacementsList) {
        Map<String, Object> data = new HashMap<>();
        for (Map<String, Object> replacements : replacementsList) {
            if (replacements != null) {
                for(Map.Entry<String, Object> entry : replacements.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        data.put(entry.getKey(), cleanString(cleaner, entry.getValue()));
                    } else if(entry.getValue() instanceof Map){
                        Map<String,Object> v = (Map<String,Object>)entry.getValue();
                        data.put(entry.getKey(), cleanValues(cleaner,v));
                    } else if(entry.getValue() instanceof List){
                        List<Object> values = (List<Object>)entry.getValue();
                        List<Object> nv = new ArrayList<>();
                        for(Object v : values) {
                            if(v instanceof String) {
                                nv.add(cleanString(cleaner, v));
                            } else if(v instanceof Map) {
                                Map<String, Object> adsf = (Map<String, Object>)v;
                                nv.add(cleanValues(cleaner, adsf));
                            } else {
                                nv.add(v);
                            }
                        }
                        data.put(entry.getKey(), nv);
                    } else {
                        data.put(entry.getKey(), entry.getValue());
                    }

                }
            }
        }
        return data;
    }

    private String cleanString(Cleaner cleaner, Object entry) {
        return StringEscapeUtils.unescapeHtml(((String) entry))
                .replaceAll("&", "").replaceAll("\u200B","");
    }

    private List<Map<String, Object>> normalizeColumns(Cleaner cleaner,
                               Map<String, Boolean> columns, List<Map<String, Object>> tulokset) {
        if (tulokset == null) {
            return null;
        }
        for (Map<String, Object> row : tulokset) {
            for (String column : columns.keySet()) {
                if (!row.containsKey(column) || row.get(column) == null) {
                    row.put(column, "");
                }
                Object obj = row.get(column);
                if(obj instanceof String) {
                    row.put(column, cleaner.clean((String) obj));
                } else if(obj instanceof ArrayList<?> && (((ArrayList<?>)obj).size() > 0 && ((ArrayList<?>)obj).get(0) instanceof Map<?, ?>)) {
                    for (Map<String, Object> map: (ArrayList<Map<String, Object>>) obj) {
                        for(Map.Entry<String, Object> entry: map.entrySet()) {
                            if (entry.getValue() != null) {
                                entry.setValue(cleaner.clean(entry.getValue().toString()));
                            }
                        }
                    }
                    row.put(column, obj);
                } else {
                    row.put(column, "");
                }

            }
        }
        return tulokset;
    }

    private Map<String, Boolean> distinctColumns(List<Map<String, Object>> tulokset) {
        Map<String, Boolean> printedColumns = new HashMap<>();
        if (tulokset == null) {
            return printedColumns;
        }
        for (Map<String, Object> haku : tulokset) {
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
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> metadataList = new ArrayList<>();
        for (DocumentMetadata documentMetadata : documentMetadataList) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("startPage", documentMetadata.getStartPage());
            metadata.put("pages", documentMetadata.getPages());
            metadata.put("addressLabel", new XmlAddressLabelDecorator(documentMetadata.getAddressLabel()));
            metadataList.add(metadata);
        }
        data.put("metadataList", metadataList);
        data.put("ipostTest", Constants.IPOST_TEST);
        return data;
    }

    public LetterReceiverLetter constructPDFForLetterReceiverLetter(LetterReceivers receiver, fi.vm.sade.viestintapalvelu.model.LetterBatch batch,
            Map<String, Object> batchReplacements, Map<String, Object> letterReplacements) throws IOException, DocumentException {
        Template template = determineTemplate(receiver, batch);
        return constructPDFForLetterReceiverLetter(receiver, template, batchReplacements, letterReplacements);
    }

    public LetterReceiverLetter constructPDFForLetterReceiverLetter(LetterReceivers receiver, Template template,
            Map<String, Object> batchReplacements, Map<String, Object> letterReplacements) throws IOException, DocumentException {
        LetterReceiverLetter letter = receiver.getLetterReceiverLetter();

        Map<String, Object> templReplacements = formReplacementMap(template.getReplacements());

        ObjectMapper mapper = objectMapperProvider.getContext(getClass());
        templReplacements.putAll(formReplacementMap(letter.getLetterReceivers().getLetterReceiverReplacement(), mapper));

        List<TemplateContent> contents = template.getContents();
        AddressLabel address = AddressLabelConverter.convert(letter.getLetterReceivers().getLetterReceiverAddress());
        PdfDocument currentDocument = new PdfDocument(address);
        Collections.sort(contents);

        for (TemplateContent tc : Contents.letterContents().filter(contents)) {
            byte[] page = createPagePdf(template, tc.getContent().getBytes(), address, templReplacements, batchReplacements, letterReplacements);
            if (letter.getLetterReceivers().getEmailAddress() != null
                    && !letter.getLetterReceivers().getEmailAddress().isEmpty()
                    && Contents.ATTACHMENT.equals(tc.getName())
                    && receiver.getLetterReceiverEmail() == null) {
                saveLetterReceiverAttachment(tc.getName(), page, receiver.getLetterReceiverLetter().getId());
            }
            currentDocument.addContent(page);
        }
        letter.setLetter(documentBuilder.merge(currentDocument).toByteArray());
        letter.setContentType("application/pdf");
        return letter;
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
                    return templateService.findById(usedTemplate.getTemplate().getId(), ContentStructureType.letter);
                }
            }
        }
        return templateService.findById(batch.getTemplateId(), ContentStructureType.letter);
    }

    public Map<String, Object> formReplacementMap(Set<LetterReceiverReplacement> replacements, ObjectMapper mapper) throws IOException {
        Map<String, Object> templReplacements = new HashMap<>();
        for (LetterReceiverReplacement replacement : replacements) {
            templReplacements.put(replacement.getName(), replacement.getEffectiveValue(mapper));
        }
        return templReplacements;
    }

    public static Map<String, Object> formReplacementMap(List<Replacement> replacements) {
        Map<String, Object> templReplacements = new HashMap<>();
        for (Replacement templRepl : replacements) {
            templReplacements.put(templRepl.getName(), templRepl.getDefaultValue());
        }
        return templReplacements;
    }

    public void setObjectMapperProvider(ObjectMapperProvider objectMapperProvider) {
        this.objectMapperProvider = objectMapperProvider;
    }
}
