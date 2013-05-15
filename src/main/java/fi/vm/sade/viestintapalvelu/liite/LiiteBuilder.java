package fi.vm.sade.viestintapalvelu.liite;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.DocumentBuilder;
import fr.opensagres.xdocreport.core.XDocReportException;

public class LiiteBuilder {

	private DocumentBuilder documentBuilder;
	
	@Inject
	public LiiteBuilder(DocumentBuilder documentBuilder) {
		this.documentBuilder = documentBuilder;
	}

	public byte[] printPDF(String templateName, List<Map<String, String>> tulokset) throws FileNotFoundException, IOException, DocumentException {
		Map<String, Object> dataContext = createDataContext(tulokset);
		byte[] xhtml = documentBuilder.applyTextTemplate(templateName, dataContext);
		return documentBuilder.xhtmlToPDF(xhtml);
	}

	public byte[] printDocxPDF(String templateName, List<Map<String, String>> tulokset) throws XDocReportException, FileNotFoundException, IOException, DocumentException {
		Map<String, Object> dataContext = createDataContext(tulokset);
		byte[] docx = documentBuilder.applyDocxTemplate(templateName, dataContext);
		return documentBuilder.docxToPDF(docx);
	}

	private Map<String, Object> createDataContext(List<Map<String, String>> tulokset) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("tulokset", tulokset);
		return data;
	}
}
