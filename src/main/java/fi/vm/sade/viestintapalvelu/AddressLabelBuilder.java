package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.odftoolkit.odfdom.converter.core.utils.ByteArrayOutputStream;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

public class AddressLabelBuilder {

	private VelocityEngine templateEngine = new VelocityEngine();

	public AddressLabelBuilder() {
		templateEngine.init();
	}

	public byte[] printAddressLabels(AddressLabelBatch input)
			throws DocumentException, IOException {
		// TODO vpeurala 6.5.2013: Refactor this pdf/csv branching somehow
		if (isPDFTemplate(input.getTemplateName())) {
			return toPDF(toXHtml(input));
		} else {
			return evaluateTemplate(input);
		}
	}

	public boolean isPDFTemplate(String templateName) {
		return templateName != null
				&& templateName.toUpperCase().endsWith(".HTML");
	}

	private byte[] toPDF(byte[] input) throws UnsupportedEncodingException,
			DocumentException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		newITextRenderer(input).createPDF(output);
		return output.toByteArray();
	}

	private byte[] toXHtml(AddressLabelBatch input) throws IOException {
		return evaluateTemplate(input);
	}

	private byte[] evaluateTemplate(AddressLabelBatch input)
			throws FileNotFoundException, IOException {
		byte[] template = readTemplate(input.getTemplateName());
		return bindDataToTemplate(template, input.getAddressLabels());
	}

	private byte[] bindDataToTemplate(byte[] template, List<AddressLabel> labels)
			throws UnsupportedEncodingException {
		StringWriter writer = new StringWriter();
		templateEngine.evaluate(newContext(labels), writer, "LOG",
				new InputStreamReader(new ByteArrayInputStream(template)));
		return writer.toString().getBytes();
	}

	private ITextRenderer newITextRenderer(byte[] input) {
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(new String(input), "/");
		renderer.layout();
		return renderer;
	}

	private VelocityContext newContext(List<AddressLabel> addressLabels) {
		VelocityContext context = new VelocityContext();
		context.put("labelList", addressLabels);
		return context;
	}

	private byte[] readTemplate(String templateName)
			throws FileNotFoundException, IOException {
		InputStream in = getClass().getResourceAsStream(templateName);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(in);
		return baos.toByteArray();
	}

	private void writeDocument(String name, byte[] document) throws IOException {
		FileOutputStream fos = null;
		try {
			File file = new File("target/documents/" + name);
			file.getParentFile().mkdirs();
			fos = new FileOutputStream(file, false);
			fos.write(document, 0, document.length);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		List<AddressLabel> labels = new ArrayList<AddressLabel>();
		labels.add(new AddressLabel("Meiju", "Aalto-Setälä", "dfvdfv", "00740",
				"HELSINKI", "Finland"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setälä", "ds", "00740",
				"HELSINKI", "Sweden"));
		AddressLabelBatch batch = new AddressLabelBatch("/osoitetarrat.html",
				labels);
		AddressLabelBuilder service = new AddressLabelBuilder();
		service.writeDocument("tarrat.pdf", service.printAddressLabels(batch));
	}

}
