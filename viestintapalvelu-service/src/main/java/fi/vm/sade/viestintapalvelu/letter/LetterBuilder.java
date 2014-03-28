package fi.vm.sade.viestintapalvelu.letter;

import java.io.FileNotFoundException;
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

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.AddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.HtmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.TemplateContent;
import fi.vm.sade.viestintapalvelu.template.TemplateService;

@Service
@Singleton
public class LetterBuilder {

    private DocumentBuilder documentBuilder;

    @Autowired
    private TemplateService templateService;

    @Inject
    public LetterBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;

    }

    public byte[] printPDF(LetterBatch batch) throws IOException,
            DocumentException {

        List<PdfDocument> source = new ArrayList<PdfDocument>();
        for (Letter letter : batch.getLetters()) {
            letter.getTemplateReplacements();
            Template template = letter.getTemplate();
            if (template == null) {
                template = templateService.getTemplateFromFiles(
                        letter.getLanguageCode(), Constants.LETTER_TEMPLATE,
                        Constants.LIITE_TEMPLATE);
            }
            if (template != null) {
                System.out.println("template: " + template);
                Set<TemplateContent> contents = template.getContents();
                List<TemplateContent> cList = new ArrayList<TemplateContent>(
                        contents);
                Collections.sort(cList);
                for (TemplateContent tc : cList) {
                    byte[] page = createPagePdf(tc.getContent().getBytes(),
                            letter.getAddressLabel(),
                            letter.getTemplateReplacements(),
                            batch.getTemplateReplacements());
                    source.add(new PdfDocument(letter.getAddressLabel(), page,
                            null));
                }
            }
        }
        return documentBuilder.merge(source).toByteArray();
    }

    private byte[] createPagePdf(byte[] pageContent, AddressLabel addressLabel,
            Map<String, Object> replacements,
            Map<String, Object> patchReplacements)
            throws FileNotFoundException, IOException, DocumentException {

        @SuppressWarnings("unchecked")
        Map<String, Object> dataContext = createDataContext(
                new HtmlAddressLabelDecorator(addressLabel), replacements,
                patchReplacements);
        byte[] xhtml = documentBuilder.applyTextTemplate(pageContent,
                dataContext);
        return documentBuilder.xhtmlToPDF(xhtml);
    }

    private Map<String, Object> createDataContext(
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

        data.put("letterDate",
                new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        data.put("osoite", decorator);

        return data;
    }

    private String cleanHtmlFromApi(String string) {
        return Jsoup.clean(string, Whitelist.relaxed());
    }
}
