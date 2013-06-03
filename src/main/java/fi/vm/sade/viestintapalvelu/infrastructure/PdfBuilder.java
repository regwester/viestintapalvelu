package fi.vm.sade.viestintapalvelu.infrastructure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.inject.Inject;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.application.Constants;
import fi.vm.sade.viestintapalvelu.domain.address.HtmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;
import fi.vm.sade.viestintapalvelu.domain.address.PostalAddressDecorator;
import fi.vm.sade.viestintapalvelu.domain.address.XmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje.Hyvaksymiskirje;
import fi.vm.sade.viestintapalvelu.domain.hyvaksymiskirje.HyvaksymiskirjeBatch;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.JalkiohjauskirjeBatch;

public class PdfBuilder {
	private DocumentBuilder documentBuilder;
	private LiiteBuilder liiteBuilder;

	@Inject
	public PdfBuilder(DocumentBuilder documentBuilder, LiiteBuilder liiteBuilder) {
		this.documentBuilder = documentBuilder;
		this.liiteBuilder = liiteBuilder;
	}

	public byte[] printPDF(JalkiohjauskirjeBatch batch) throws IOException,
			DocumentException {
		return createJalkiohjauskirjeBatch(batch).toByteArray();
	}

	public byte[] printZIP(JalkiohjauskirjeBatch batch) throws IOException,
			DocumentException, NoSuchAlgorithmException {
		Map<String, byte[]> subZips = new HashMap<String, byte[]>();
		List<JalkiohjauskirjeBatch> subBatches = batch
				.split(Constants.IPOST_BATCH_LIMIT);
		for (int i = 0; i < subBatches.size(); i++) {
			JalkiohjauskirjeBatch subBatch = subBatches.get(i);
			MergedPdfDocument pdf = createJalkiohjauskirjeBatch(subBatch);
			Map<String, Object> context = createDataContext(pdf
					.getDocumentMetadata());
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

	public byte[] printPDF(HyvaksymiskirjeBatch batch) throws IOException,
			DocumentException {
		List<PdfDocument> source = new ArrayList<PdfDocument>();
		for (Hyvaksymiskirje kirje : batch.getLetters()) {
			String kirjeTemplateName = PdfBuilder
					.resolveTemplateName(Constants.HYVAKSYMISKIRJE_TEMPLATE,
							kirje.getLanguageCode());
			byte[] frontPage = createFirstPagePDF(kirjeTemplateName,
					kirje.getPostalAddress(), kirje.getKoulu(),
					kirje.getKoulutus());
			String liiteTemplateName = PdfBuilder.resolveTemplateName(
					Constants.LIITE_TEMPLATE, kirje.getLanguageCode());
			byte[] attachment = liiteBuilder.printPDF(liiteTemplateName,
					kirje.getTulokset());
			source.add(new PdfDocument(kirje.getPostalAddress(), frontPage,
					attachment));
		}
		return documentBuilder.merge(source).toByteArray();
	}

	public MergedPdfDocument createJalkiohjauskirjeBatch(
			JalkiohjauskirjeBatch batch) throws IOException, DocumentException {
		List<PdfDocument> source = new ArrayList<PdfDocument>();
		for (Jalkiohjauskirje kirje : batch.getLetters()) {
			String kirjeTemplateName = PdfBuilder.resolveTemplateName(
					Constants.JALKIOHJAUSKIRJE_TEMPLATE,
					kirje.getLanguageCode());
			byte[] frontPage = createFirstPagePDF(kirjeTemplateName,
					kirje.getPostalAddress());
			String liiteTemplateName = PdfBuilder.resolveTemplateName(
					Constants.LIITE_TEMPLATE, kirje.getLanguageCode());
			byte[] attachment = liiteBuilder.printPDF(liiteTemplateName,
					kirje.getTulokset());
			source.add(new PdfDocument(kirje.getPostalAddress(), frontPage,
					attachment));
		}
		return documentBuilder.merge(source);
	}

	private byte[] createFirstPagePDF(String templateName,
			PostalAddress postalAddress) throws FileNotFoundException,
			IOException, DocumentException {
		Map<String, Object> dataContext = createDataContext(new HtmlAddressLabelDecorator(
				postalAddress));
		byte[] xhtml = documentBuilder.applyTextTemplate(templateName,
				dataContext);
		return documentBuilder.xhtmlToPDF(xhtml);
	}

	private byte[] createFirstPagePDF(String templateName,
			PostalAddress postalAddress, String koulu, String koulutus)
			throws FileNotFoundException, IOException, DocumentException {
		Map<String, Object> dataContext = createDataContext(
				new HtmlAddressLabelDecorator(postalAddress), koulu, koulutus);
		byte[] xhtml = documentBuilder.applyTextTemplate(templateName,
				dataContext);
		return documentBuilder.xhtmlToPDF(xhtml);
	}

	private Map<String, Object> createDataContext(
			PostalAddressDecorator decorator) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("osoite", decorator);
		return data;
	}

	private Map<String, Object> createDataContext(
			PostalAddressDecorator decorator, String koulu, String koulutus) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("osoite", decorator);
		data.put("koulu", StringEscapeUtils.escapeHtml(koulu));
		data.put("koulutus", StringEscapeUtils.escapeHtml(koulutus));
		return data;
	}

	private Map<String, Object> createDataContext(
			List<DocumentMetadata> documentMetadataList) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> metadataList = new ArrayList<Map<String, Object>>();
		for (DocumentMetadata documentMetadata : documentMetadataList) {
			Map<String, Object> metadata = new HashMap<String, Object>();
			metadata.put("startPage", documentMetadata.getStartPage());
			metadata.put("pages", documentMetadata.getPages());
			metadata.put("addressLabel", new XmlAddressLabelDecorator(
					documentMetadata.getPostalAddress()));
			metadataList.add(metadata);
		}
		data.put("metadataList", metadataList);
		return data;
	}

	public static String resolveTemplateName(String template,
			String languageCode) {
		languageCode = languageCode == null || "".equals(languageCode) ? "FI"
				: languageCode;
		languageCode = "SE".equalsIgnoreCase(languageCode)
				|| "FI".equalsIgnoreCase(languageCode) ? languageCode : "EN";
		return template.replace("{LANG}", languageCode.toUpperCase());
	}
}
