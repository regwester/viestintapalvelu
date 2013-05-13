package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.OptionsHelper;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class JalkiohjauskirjeBuilder {

	public byte[] printJalkiohjauskirje(JalkiohjauskirjeBatch batch) throws IOException, XDocReportException, COSVisitorException, DocumentException {
		List<InputStream> source = new ArrayList<InputStream>();
		for (Jalkiohjauskirje kirje : batch.getLetters()) {
			source.add(createDocument(batch.getTemplateName(), kirje.getAddressLabel()));
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		doMerge(source, output);
		return output.toByteArray();
		
	}
	
	public static void doMerge(List<InputStream> list, OutputStream outputStream)
            throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        for (InputStream in : list) {
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
    }
	
	public InputStream createDocument(String templateName, AddressLabel addressLabel) throws IOException,
			XDocReportException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IXDocReport report = readTemplate(templateName);
		Options options = Options.getTo(ConverterTypeTo.PDF).via(
				ConverterTypeVia.XWPF);
		OptionsHelper.setFontEncoding(options, "windows-1250");
		report.convert(createDataContext(addressLabel, report), options, out);
		writeDocument("" + new Date().getTime(), out.toByteArray());
		return new ByteArrayInputStream(out.toByteArray());
	}

	private IContext createDataContext(AddressLabel addressLabel, IXDocReport report)
			throws XDocReportException {
		IContext context = report.createContext();
		context.put("osoite", addressLabel);
		return context;
	}
	
	private void writeDocument(String name, byte[] document) throws IOException {
		FileOutputStream fos = null;
		try {
			File file = new File("target/documents/" + name + ".pdf");
			file.getParentFile().mkdirs();
			fos = new FileOutputStream(file, false);
			fos.write(document, 0, document.length);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}


	private IXDocReport readTemplate(String templateName)
			throws FileNotFoundException, IOException, XDocReportException {
		InputStream in = getClass().getResourceAsStream(templateName);
		return XDocReportRegistry.getRegistry().loadReport(in,
				TemplateEngineKind.Velocity);
	}
}
