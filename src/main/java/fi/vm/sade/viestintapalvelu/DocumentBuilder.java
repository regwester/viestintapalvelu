package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

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

	public byte[] mergePDFs(List<byte[]> input) throws DocumentException, IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		List<InputStream> inputStreams = createInputStreams(input);
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        for (InputStream in : inputStreams) {
            PdfReader reader = new PdfReader(in);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                PdfImportedPage page = writer.getImportedPage(reader, i);
                cb.addTemplate(page, 0, 0);
            }
        }
        outputStream.flush();
        document.close();
        outputStream.close();
        return outputStream.toByteArray();
    }
	
	private static List<InputStream> createInputStreams(List<byte[]> input) {
		List<InputStream> inputStreams = Lists.transform(input, new Function<byte[], InputStream>() {
			public InputStream apply(byte[] byteArray) {
				return new ByteArrayInputStream(byteArray);
			}
		});
		return inputStreams;
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
