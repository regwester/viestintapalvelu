package fi.vm.sade.viestintapalvelu.hyvaksymiskirje;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.inject.Inject;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.AddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.HtmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;
import fi.vm.sade.viestintapalvelu.liite.LiiteBuilder;

public class HyvaksymiskirjeBuilder {

	private final static String HYVAKSYMISKIRJE_TEMPLATE = "/hyvaksymiskirje.html";
	private final static String LIITE_TEMPLATE = "/liite.html";

	private DocumentBuilder documentBuilder;
	private LiiteBuilder liiteBuilder;
	
	@Inject
	public HyvaksymiskirjeBuilder(DocumentBuilder documentBuilder, LiiteBuilder liiteBuilder) {
		this.documentBuilder = documentBuilder;
		this.liiteBuilder = liiteBuilder;
	}
	
	public byte[] printPDF(HyvaksymiskirjeBatch batch) throws IOException, DocumentException {
		List<PdfDocument> source = new ArrayList<PdfDocument>();
		for (Hyvaksymiskirje kirje : batch.getLetters()) {
			byte[] frontPage = createFirstPagePDF(HYVAKSYMISKIRJE_TEMPLATE, kirje.getAddressLabel(), kirje.getKoulu(), kirje.getKoulutus());
			byte[] attachment = liiteBuilder.printPDF(LIITE_TEMPLATE, kirje.getTulokset());
			source.add(new PdfDocument(kirje.getAddressLabel(), frontPage, attachment));
		}
		return documentBuilder.merge(source).toByteArray();
	}

	private byte[] createFirstPagePDF(String templateName, AddressLabel addressLabel, String koulu, String koulutus) throws FileNotFoundException, IOException, DocumentException {
		Map<String, Object> dataContext = createDataContext(new HtmlAddressLabelDecorator(addressLabel), koulu, koulutus);
		byte[] xhtml = documentBuilder.applyTextTemplate(templateName, dataContext);
		return documentBuilder.xhtmlToPDF(xhtml);
	}

	private Map<String, Object> createDataContext(AddressLabelDecorator decorator, String koulu, String koulutus) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("osoite", decorator);
		data.put("koulu", StringEscapeUtils.escapeHtml(koulu));
		data.put("koulutus", StringEscapeUtils.escapeHtml(koulutus));
		return data;
	}
}
