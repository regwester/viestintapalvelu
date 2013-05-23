package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.AddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.HtmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.XmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.document.DocumentMetadata;
import fi.vm.sade.viestintapalvelu.document.MergedPdfDocument;
import fi.vm.sade.viestintapalvelu.document.PdfDocument;
import fi.vm.sade.viestintapalvelu.liite.LiiteBuilder;

public class JalkiohjauskirjeBuilder {

	private final static String JALKIOHJAUSKIRJE_TEMPLATE = "/jalkiohjauskirje.html";
	private final static String LIITE_TEMPLATE = "/liite_FI.html";
	private final static String IPOST_TEMPLATE = "/ipost.xml";
	
	private DocumentBuilder documentBuilder;
	private LiiteBuilder liiteBuilder;
	
	@Inject
	public JalkiohjauskirjeBuilder(DocumentBuilder documentBuilder, LiiteBuilder liiteBuilder) {
		this.documentBuilder = documentBuilder;
		this.liiteBuilder = liiteBuilder;
	}
	
	public byte[] printPDF(JalkiohjauskirjeBatch batch) throws IOException, DocumentException {
		return createJalkiohjauskirjeBatch(batch).toByteArray();
	}

	public byte[] printZIP(JalkiohjauskirjeBatch batch) throws IOException, DocumentException, NoSuchAlgorithmException {
		MergedPdfDocument pdf = createJalkiohjauskirjeBatch(batch);
		Map<String, Object> context = createDataContext(pdf.getDocumentMetadata());
		byte[] ipostXml = documentBuilder.applyTextTemplate(IPOST_TEMPLATE, context);
		Map<String, byte[]> documents = new HashMap<String, byte[]>();
		documents.put("jalkiohjauskirje.pdf", pdf.toByteArray());
		documents.put("jalkiohjauskirje.xml", ipostXml);
		return documentBuilder.zip(documents);
	}

	public MergedPdfDocument createJalkiohjauskirjeBatch(JalkiohjauskirjeBatch batch) throws IOException, DocumentException {
		List<PdfDocument> source = new ArrayList<PdfDocument>();
		for (Jalkiohjauskirje kirje : batch.getLetters()) {
			byte[] frontPage = createFirstPagePDF(JALKIOHJAUSKIRJE_TEMPLATE, kirje.getAddressLabel());
			byte[] attachment = liiteBuilder.printPDF(LIITE_TEMPLATE, kirje.getTulokset());
			source.add(new PdfDocument(kirje.getAddressLabel(), frontPage, attachment));
		}
		return documentBuilder.merge(source);
	}

	private byte[] createFirstPagePDF(String templateName, AddressLabel addressLabel) throws FileNotFoundException, IOException, DocumentException {
		Map<String, Object> dataContext = createDataContext(new HtmlAddressLabelDecorator(addressLabel));
		byte[] xhtml = documentBuilder.applyTextTemplate(templateName, dataContext);
		return documentBuilder.xhtmlToPDF(xhtml);
	}

	private Map<String, Object> createDataContext(AddressLabelDecorator decorator) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("osoite", decorator);
		return data;
	}

	private Map<String, Object> createDataContext(List<DocumentMetadata> documentMetadataList) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> metadataList = new ArrayList<Map<String,Object>>();
		for (DocumentMetadata documentMetadata : documentMetadataList) {
			 Map<String, Object> metadata = new HashMap<String, Object>();
			 metadata.put("startPage", documentMetadata.getStartPage());
			 metadata.put("pages", documentMetadata.getPages());
			 metadata.put("addressLabel", new XmlAddressLabelDecorator(documentMetadata.getAddressLabel()));
			 metadataList.add(metadata);
		}
		data.put("metadataList", metadataList);
		return data;
	}
}
