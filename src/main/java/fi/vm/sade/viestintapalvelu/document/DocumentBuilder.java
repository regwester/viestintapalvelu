package fi.vm.sade.viestintapalvelu.document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.MediaReplacedElementFactory;

public class DocumentBuilder {
	private VelocityEngine templateEngine = new VelocityEngine();

	public DocumentBuilder() {
		templateEngine.init();
	}

	public byte[] xhtmlToPDF(byte[] xhtml) throws DocumentException, IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		newITextRenderer(xhtml).createPDF(output);
		return output.toByteArray();
	}

	public byte[] applyTextTemplate(String templateName, Map<String, Object> data) throws FileNotFoundException, IOException {
		byte[] template = readTemplate(templateName);
		StringWriter writer = new StringWriter();
		templateEngine.evaluate(new VelocityContext(data), writer, "LOG",
				new InputStreamReader(new ByteArrayInputStream(template)));
		return writer.toString().getBytes();
	}

	public MergedPdfDocument merge(List<PdfDocument> input) throws DocumentException, IOException {
        MergedPdfDocument mergedPDFDocument = new MergedPdfDocument();
        for (PdfDocument pdfDocument : input) {
        	mergedPDFDocument.write(pdfDocument);
        }
        mergedPDFDocument.flush();
        return mergedPDFDocument;
    }

	public byte[] zip(Map<String, byte[]> documents) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ZipOutputStream zipStream = new ZipOutputStream(out);
		for (String documentName : documents.keySet()) {
			zipStream.putNextEntry(new ZipEntry(documentName));
			zipStream.write(documents.get(documentName));
			zipStream.closeEntry();
		}
		zipStream.close();
		return out.toByteArray();
	}

	private ITextRenderer newITextRenderer(byte[] input) {
		ITextRenderer renderer = new ITextRenderer();
		renderer.getSharedContext().setReplacedElementFactory(new MediaReplacedElementFactory(renderer.getSharedContext().getReplacedElementFactory()));
		renderer.setDocumentFromString(new String(input));
		renderer.layout();
		return renderer;
	}

	private byte[] readTemplate(String templateName)
			throws FileNotFoundException, IOException {
		InputStream in = getClass().getResourceAsStream(templateName);
		if (in == null) {
			throw new FileNotFoundException("Template " + templateName + " not found");
		}
		return IOUtils.toByteArray(in);
	}
}
