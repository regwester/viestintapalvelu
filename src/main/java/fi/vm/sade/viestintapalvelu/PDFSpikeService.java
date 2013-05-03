package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.odftoolkit.odfdom.converter.core.utils.ByteArrayOutputStream;
import org.w3c.dom.Document;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

public class PDFSpikeService {
	
	private VelocityEngine templateEngine = new VelocityEngine();
	
	public PDFSpikeService() {
		templateEngine.init();
	}
	
	public byte[] printAddressLabels(AddressLabelBatch input) throws DocumentException, IOException {
		return xhtmlToPDF(toXHTML(input));
	}
	
	private byte[] xhtmlToPDF(Document input) {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(input, "/");
        renderer.layout();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] document = null;
        try {
			renderer.createPDF(os);
			document = os.toByteArray();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return document;
	}

	private Document toXHTML(AddressLabelBatch input) throws IOException {
		byte[] template = readTemplate(input.getTemplateName());
		byte[] document = bindDataToTemplate(template, input.getAddressLabels());
		return tidy(document);
	}

	private byte[] bindDataToTemplate(byte[] template, List<AddressLabel> addressLabels) {
		VelocityContext context = new VelocityContext();
		context.put("labelList", addressLabels);
		StringWriter writer = new StringWriter();
		templateEngine.evaluate(context, writer, "tarrat", new InputStreamReader(new ByteArrayInputStream(template)));
		return writer.toString().getBytes();
	}

	private Document tidy(byte[] document) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(document);
		Tidy tidy = new Tidy();
		tidy.setTidyMark(false);
		tidy.setDocType("omit");
		tidy.setXHTML(true);
		tidy.setCharEncoding(Configuration.ISO2022);
		Document doc = tidy.parseDOM(in, out);
		writeDocument("tidydoc", "html", out.toByteArray());
		return doc;
	}

	private byte[] readTemplate(String templateName) throws FileNotFoundException, IOException {
		InputStream in = getClass().getResourceAsStream(templateName);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(in);
		return baos.toByteArray();
	}

	private void writeDocument(String name, byte[] document) {
		writeDocument(name, "pdf", document);
	}

	private void writeDocument(String name, String type, byte[] document) {
		FileOutputStream fos = null;
		try {
			File file = new File("target/documents/" + name + "."+ type);
			file.getParentFile().mkdirs();
			fos = new FileOutputStream(file, false);
			fos.write(document, 0, document.length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		List<AddressLabel> labels = new ArrayList<AddressLabel>();
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Meiju", "Aalto-Setala", "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Aarnio", "Lumi", "Tilkankatu 7b 24", "00300", "HELSINKI", "Suomi"));
		labels.add(new AddressLabel("Maria", "Abbas-Hashimi", "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
		labels.add(new AddressLabel("Hodan", "Abdi", "Postirestante", "00840", "Helsinki", "Suomi"));
		AddressLabelBatch batch = new AddressLabelBatch("/osoitetarrat.html", labels);
		PDFSpikeService service = new PDFSpikeService();
		service.writeDocument("tarrat", service.printAddressLabels(batch));
	}

}
