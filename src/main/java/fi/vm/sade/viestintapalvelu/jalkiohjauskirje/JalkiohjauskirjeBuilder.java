package fi.vm.sade.viestintapalvelu.jalkiohjauskirje;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.address.AddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.DocxAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.address.HtmlAddressLabelDecorator;
import fi.vm.sade.viestintapalvelu.liite.LiiteBuilder;
import fr.opensagres.xdocreport.core.XDocReportException;

public class JalkiohjauskirjeBuilder {

	private DocumentBuilder documentBuilder;
	private LiiteBuilder liiteBuilder;
	
	@Inject
	public JalkiohjauskirjeBuilder(DocumentBuilder documentBuilder, LiiteBuilder liiteBuilder) {
		this.documentBuilder = documentBuilder;
		this.liiteBuilder = liiteBuilder;
	}
	
	public byte[] printPDF(JalkiohjauskirjeBatch batch) throws IOException, XDocReportException, DocumentException {
		List<byte[]> source = new ArrayList<byte[]>();
		for (Jalkiohjauskirje kirje : batch.getLetters()) {
			if (isDocxTemplate(batch.getKirjeTemplateName())) {
				source.add(createFirstPagePDF(batch.getKirjeTemplateName(), kirje.getAddressLabel()));
			} else {
				source.add(createFirstPage(batch.getKirjeTemplateName(), kirje.getAddressLabel()));
			}
			source.add(liiteBuilder.printPDF(batch.getLiiteTemplateName(), kirje.getTulokset()));
		}
		return documentBuilder.mergePDFs(source);
	}

	private boolean isDocxTemplate(String templateName) {
		return templateName.toLowerCase().endsWith("docx");
	}

	private byte[] createFirstPage(String templateName, AddressLabel addressLabel) throws FileNotFoundException, IOException, DocumentException {
		Map<String, Object> dataContext = createDataContext(new HtmlAddressLabelDecorator(addressLabel));
		byte[] xhtml = documentBuilder.applyTextTemplate(templateName, dataContext);
		return documentBuilder.xhtmlToPDF(xhtml);
	}

	private byte[] createFirstPagePDF(String templateName, AddressLabel addressLabel)
			throws FileNotFoundException, IOException, XDocReportException, DocumentException {
		Map<String, Object> dataContext = createDataContext(new DocxAddressLabelDecorator(addressLabel));
		byte[] docx = documentBuilder.applyDocxTemplate(templateName, dataContext);
		return documentBuilder.docxToPDF(docx);
	}
	
	private Map<String, Object> createDataContext(AddressLabelDecorator decorator) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("osoite", decorator);
		return data;
	}
}
