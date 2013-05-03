package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class PDFService {
	public byte[] printAddressLabels(AddressLabelBatch input) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			IXDocReport report = readTemplate(input.getTemplateName());
			Options options = Options.getTo(ConverterTypeTo.PDF).via(
					ConverterTypeVia.ODFDOM);
			report.convert(createDataContext(input.getAddressLabels(), report),
					options, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XDocReportException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	public void createDocuments(List<String> hakijat) {
		for (String hakija : hakijat) {
			writeDocument(hakija, createDocument(hakija));
		}
	}

	public byte[] createDocument(String hakija) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			IXDocReport report = readTemplate("/jalkiohjauskirje.odt");
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

	private IContext createDataContext(List<AddressLabel> labels,
			IXDocReport report) throws XDocReportException {
		IContext context = report.createContext();
		context.put("labels", labels);
		FieldsMetadata metadata = new FieldsMetadata();
		metadata.addFieldAsList("labels.FirstName");
		metadata.addFieldAsList("labels.LastName");
		metadata.addFieldAsList("labels.StreetAddress");
		metadata.addFieldAsList("labels.PostalCode");
		metadata.addFieldAsList("labels.PostOffice");
		metadata.addFieldAsList("labels.Country");
		report.setFieldsMetadata(metadata);
		return context;
	}

	private IXDocReport readTemplate(String templateName)
			throws FileNotFoundException, IOException, XDocReportException {
		InputStream in = getClass().getResourceAsStream(templateName);
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

	// public static void main(String[] args) throws Exception {
	// AddressLabelBatch batch = new AddressLabelBatch();
	// List<AddressLabel> labels = new ArrayList<AddressLabel>();
	// labels.add(new AddressLabel("Meiju", "", "Aalto-Setala",
	// "Kauriintie 3 D 31", "00740", "HELSINKI", "Suomi"));
	// labels.add(new AddressLabel("Aarnio", "", "Lumi", "Tilkankatu 7b 24",
	// "00300", "HELSINKI", "Suomi"));
	// labels.add(new AddressLabel("Maria", "", "Abbas-Hashimi",
	// "Petter wetterin tie 6 c 46", "00810", "Helsinki", "Suomi"));
	// labels.add(new AddressLabel("Hodan", "", "Abdi", "Postirestante",
	// "00840", "Helsinki", "Suomi"));
	// batch.setAddressLabels(labels);
	// batch.setTemplateName("/osoitetarrat.odt");
	// PDFService service = new PDFService();
	// service.writeDocument("tarrat", service.printAddressLabels(batch));
	// }
}
