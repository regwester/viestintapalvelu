package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class PDFService {
	public void createDocuments(List<String> hakijat) {
		for (String hakija : hakijat) {
			writeDocument(hakija, createDocument(hakija));
		}
	}

	public byte[] createDocument(String hakija) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			IXDocReport report = readTemplate();
			report.process(createDataContext(hakija, report), out);
			Options options = Options.getTo(ConverterTypeTo.PDF).via(
					ConverterTypeVia.ODFDOM);
			report.convert(createDataContext(hakija, report), options, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XDocReportException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	private IContext createDataContext(String hakija, IXDocReport report)
			throws XDocReportException {
		IContext context = report.createContext();
		context.put("hakijan_nimi", hakija);
		return context;
	}

	private IXDocReport readTemplate() throws FileNotFoundException,
			IOException, XDocReportException {
		InputStream in = getClass()
				.getResourceAsStream("/jalkiohjauskirje.odt");
		return XDocReportRegistry.getRegistry().loadReport(in,
				TemplateEngineKind.Velocity);
	}

	private void writeDocument(String name, byte[] document) {
		FileOutputStream fos = null;
		try {
			File file = new File("target/documents/" + name + ".pdf");
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
		new PDFService().createDocuments(Arrays.asList(args));
	}

}
