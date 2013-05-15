package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.odftoolkit.odfdom.converter.core.utils.ByteArrayOutputStream;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

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

	public byte[] docxToPDF(byte[] docx) throws DocumentException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(docx));
		PdfOptions pdfOptions = createPDFOptions();
        PdfConverter.getInstance().convert(document, out, pdfOptions);
        return out.toByteArray();
	}

	public byte[] applyTextTemplate(String templateName, Map<String, Object> data) throws FileNotFoundException, IOException {
		byte[] template = readTemplate(templateName);
		StringWriter writer = new StringWriter();
		templateEngine.evaluate(new VelocityContext(data), writer, "LOG",
				new InputStreamReader(new ByteArrayInputStream(template)));
		return writer.toString().getBytes();
	}

	public byte[] applyDocxTemplate(String templateName, Map<String, Object> data) throws FileNotFoundException, IOException, XDocReportException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IXDocReport report = newIXDocReport(readTemplate(templateName));
		report.process(data, out);
		return out.toByteArray();
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

	private PdfOptions createPDFOptions() {
		PdfOptions pdfOptions = PdfOptions.create();
		pdfOptions.fontEncoding("Cp1252");
 		return pdfOptions;
	}

	private ITextRenderer newITextRenderer(byte[] input) {
		ITextRenderer renderer = new ITextRenderer();
		renderer.getSharedContext().setReplacedElementFactory(new MediaReplacedElementFactory(renderer.getSharedContext().getReplacedElementFactory()));
		renderer.setDocumentFromString(new String(input));
		renderer.layout();
		return renderer;
	}

	private IXDocReport newIXDocReport(byte[] template) throws FileNotFoundException, IOException, XDocReportException {
		return XDocReportRegistry.getRegistry().loadReport(new ByteArrayInputStream(template), TemplateEngineKind.Velocity);
	}
	
	private byte[] readTemplate(String templateName)
			throws FileNotFoundException, IOException {
		InputStream in = getClass().getResourceAsStream(templateName);
		if (in == null) {
			throw new FileNotFoundException("Template " + templateName + " not found");
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(in);
		return baos.toByteArray();
	}
}
