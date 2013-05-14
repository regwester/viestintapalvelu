package fi.vm.sade.viestintapalvelu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.lowagie.text.DocumentException;

import fr.opensagres.xdocreport.core.XDocReportException;

public class JalkiohjauskirjeBuilder {

	private DocumentBuilder documentBuilder;
	
	@Inject
	public JalkiohjauskirjeBuilder(DocumentBuilder documentBuilder) {
		this.documentBuilder = documentBuilder;
	}
	
	public byte[] printPDF(JalkiohjauskirjeBatch batch) throws IOException, XDocReportException, DocumentException {
		List<byte[]> source = new ArrayList<byte[]>();
		for (Jalkiohjauskirje kirje : batch.getLetters()) {
			source.add(createKirjePDF(batch.getKirjeTemplateName(), kirje.getAddressLabel()));
		}
		return documentBuilder.mergePDFs(source);
		
	}

	private byte[] createKirjePDF(String templateName, AddressLabel addressLabel)
			throws FileNotFoundException, IOException, XDocReportException, DocumentException {
		Map<String, Object> dataContext = createDataContext(addressLabel);
		byte[] docx = documentBuilder.applyDocxTemplate(templateName, dataContext);
		return documentBuilder.docxToPDF(docx);
	}
	
	private Map<String, Object> createDataContext(AddressLabel addressLabel) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("osoite", addressLabel);
		return data;
	}
}
