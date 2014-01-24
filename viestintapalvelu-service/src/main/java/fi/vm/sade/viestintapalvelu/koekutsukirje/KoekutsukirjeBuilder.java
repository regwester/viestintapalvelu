package fi.vm.sade.viestintapalvelu.koekutsukirje;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.propertyeditors.URLEditor;
import org.springframework.stereotype.Service;

import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.Constants;
import fi.vm.sade.viestintapalvelu.Utils;
import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.AddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.HtmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;

@Service
@Singleton
public class KoekutsukirjeBuilder {

    private DocumentBuilder documentBuilder;

    @Inject
    public KoekutsukirjeBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    public byte[] printPDF(KoekutsukirjeBatch batch) throws IOException, DocumentException {
        List<PdfDocument> source = new ArrayList<PdfDocument>();
        for (Koekutsukirje kirje : batch.getLetters()) {
            String kirjeTemplateName = Utils.resolveTemplateName(Constants.KOEKUTSUKIRJE_TEMPLATE,
                    kirje.getLanguageCode());
            byte[] frontPage = createFirstPagePDF(kirjeTemplateName, kirje.getAddressLabel(), kirje.getHakukohde(), kirje.getLetterBodyText());
            source.add(new PdfDocument(kirje.getAddressLabel(), frontPage, null));
        }
        return documentBuilder.merge(source).toByteArray();
    }

    private byte[] createFirstPagePDF(String templateName, AddressLabel addressLabel, String hakukohde, String letterBodyText)
            throws FileNotFoundException, IOException, DocumentException {
        Map<String, Object> dataContext = createDataContext(new HtmlAddressLabelDecorator(addressLabel), hakukohde, letterBodyText);
        byte[] xhtml = documentBuilder.applyTextTemplate(templateName, dataContext);
        return documentBuilder.xhtmlToPDF(xhtml);
    }

    private Map<String, Object> createDataContext(AddressLabelDecorator decorator, String hakukohde, String letterBodyText) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("osoite", decorator);
        data.put("hakukohde", 	StringEscapeUtils.escapeHtml(hakukohde));
        data.put("letterDate", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
       	data.put("letterBodyText", escapeAllButGtLt(letterBodyText));  // Scandics escaped properly, but HTML markup retained
        return data;
    }

  private String escapeAllButGtLt(String letterBody) {
	return StringEscapeUtils.escapeHtml(letterBody).replaceAll("&gt;", ">").replaceAll("&lt;", "<");
}
    
}
