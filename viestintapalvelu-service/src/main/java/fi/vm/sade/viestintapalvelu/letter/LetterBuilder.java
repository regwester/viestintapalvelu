package fi.vm.sade.viestintapalvelu.letter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
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
public class LetterBuilder {

	private DocumentBuilder documentBuilder;

	@Inject
	public LetterBuilder(DocumentBuilder documentBuilder) {
		this.documentBuilder = documentBuilder;
	}

	public byte[] printPDF(LetterBatch batch) throws IOException,
			DocumentException {
		List<PdfDocument> source = new ArrayList<PdfDocument>();
		for (Letter kirje : batch.getLetters()) {
			String kirjeTemplateName = Utils.resolveTemplateName(
					Constants.LETTER_TEMPLATE, kirje.getLanguageCode());
			String tarjoaja = Strings.nullToEmpty(kirje.getTarjoaja());
			byte[] frontPage = createFirstPagePDF(kirjeTemplateName,
					kirje.getAddressLabel(), kirje.getHakukohde(), tarjoaja,
					kirje.getLetterBodyText());
			source.add(new PdfDocument(kirje.getAddressLabel(), frontPage, null));
		}
		return documentBuilder.merge(source).toByteArray();
	}

	private byte[] createFirstPagePDF(String templateName,
			AddressLabel addressLabel, String hakukohde, String tarjoaja,
			String letterBodyText) throws FileNotFoundException, IOException,
			DocumentException {
		Map<String, Object> dataContext = createDataContext(
				new HtmlAddressLabelDecorator(addressLabel), hakukohde,
				tarjoaja, letterBodyText);
		byte[] xhtml = documentBuilder.applyTextTemplate(templateName,
				dataContext);
		return documentBuilder.xhtmlToPDF(xhtml);
	}

	private Map<String, Object> createDataContext(
			AddressLabelDecorator decorator, String hakukohde, String tarjoaja,
			String letterBodyText) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("osoite", decorator);
		data.put("hakukohde", StringEscapeUtils.escapeHtml(hakukohde));
		data.put("tarjoaja", StringEscapeUtils.escapeHtml(tarjoaja));
		data.put("letterDate",
				new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
		data.put("letterBodyText", cleanHtmlFromApi(letterBodyText));
		return data;
	}

	private String cleanHtmlFromApi(String letterBody) {
		return Jsoup.clean(letterBody, Whitelist.relaxed());
	}
}
