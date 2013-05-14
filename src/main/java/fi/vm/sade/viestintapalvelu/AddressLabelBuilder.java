package fi.vm.sade.viestintapalvelu;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.lowagie.text.DocumentException;

public class AddressLabelBuilder {
	private DocumentBuilder documentBuilder;

	@Inject
	public AddressLabelBuilder(DocumentBuilder documentBuilder) {
		this.documentBuilder = documentBuilder;
	}

	public byte[] printPDF(AddressLabelBatch input) throws DocumentException, IOException {
		byte[] xhtml = documentBuilder.applyTextTemplate(input.getTemplateName(), 
				createDataContext(input.getAddressLabels()));
		return documentBuilder.xhtmlToPDF(xhtml);
	}

	public byte[] printCSV(AddressLabelBatch input) throws DocumentException, IOException {
		return documentBuilder.applyTextTemplate(input.getTemplateName(), 
				createDataContext(input.getAddressLabels()));
	}
	
	private Map<String, Object> createDataContext(List<AddressLabel> addressLabels) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("labelList", addressLabels);
		return data;
	}
}
