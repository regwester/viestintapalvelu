package fi.vm.sade.viestintapalvelu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFText2HTML;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.odftoolkit.odfdom.converter.core.utils.ByteArrayOutputStream;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.Jalkiohjauskirje;
import fi.vm.sade.viestintapalvelu.jalkiohjauskirje.JalkiohjauskirjeBatch;

@RunWith(Enclosed.class)
public class LiitePDFTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	private static AddressLabel label = new AddressLabel("Åle", "Öistämö",
			"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330", "Stockholm", "SL", "Sweden");

	public static class WhenCreatingLiiteWithOneHakutoive {

		private static Map<String,String> tulos = createHaku("Diakonissaopisto", "Hoitaja", "20", "30", "10", "1", "28", "25", "1", "E");
		private static List<String[]> pdf;
		private static List<String> hakutoive;

		@SuppressWarnings("unchecked")
		@BeforeClass
		public static void setUp() throws Exception {
			Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, Arrays.asList(tulos));
			pdf = callGenerateJalkiohjauskirje(Arrays.asList(kirje));
			hakutoive = findHakutoiveTaulukko(pdf).get(0);
		}

		@Test
		public void kouluIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("koulu"), hakutoive.get(0));
		}

		@Test
		public void hakutoiveIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("hakutoive"), hakutoive.get(1));
		}

		@Test
		public void ensisijaisetHakijatIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("ensisijaisetHakijat"), hakutoive.get(2));
		}

		@Test
		public void kaikkiHakijatIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("kaikkiHakijat"), hakutoive.get(4));
		}

		@Test
		public void aloituspaikatIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("aloituspaikat"), hakutoive.get(5));
		}

		@Test
		public void varasijaIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("varasija"), hakutoive.get(6));
		}

		@Test
		public void alinHyvaksyttyIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("alinHyvaksytty"), hakutoive.get(7));
		}

		@Test
		public void omatPisteesiIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("omatPisteesi"), hakutoive.get(8));
		}

		@Test
		public void paasyJaSoveltuvuusKoeIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("paasyJaSoveltuvuusKoe"), hakutoive.get(9));
		}

		@Test
		public void hylkayksenSyyIsPrinted() throws Exception {
			Assert.assertEquals(tulos.get("hylkayksenSyy"), hakutoive.get(10));
		}
	}

	public static class WhenCreatingLiiteWithTwoHakutoive {

		private static Map<String,String> toive1 = createHaku("Diakonissaopisto", "Hoitaja", "20", "30", "10", "1", "28", "25", "1", "E");
		private static Map<String,String> toive2 = createHaku("SLK", "Merkonomi", "21", "31", "11", "2", "29", "26", "2", "W");
		private static List<String[]> pdf;
		private static List<List<String>> hakutoive;

		@SuppressWarnings("unchecked")
		@BeforeClass
		public static void setUp() throws Exception {
			Jalkiohjauskirje kirje = new Jalkiohjauskirje(label, Arrays.asList(toive1, toive2));
			pdf = callGenerateJalkiohjauskirje(Arrays.asList(kirje));
			hakutoive = findHakutoiveTaulukko(pdf);
		}

		@Test
		public void twoHakutoivettaIsPrinted() throws Exception {
			Assert.assertEquals(2, hakutoive.size());
		}

		@Test
		public void hakutoiveetArePrintedInSameOrderAsTheyAreSent() throws Exception {
			Assert.assertEquals(toive1.get("koulu"), hakutoive.get(0).get(0));
			Assert.assertEquals(toive2.get("koulu"), hakutoive.get(1).get(0));
		}
	}

	private static List<String[]> readDownloadResponseBody(HttpResponse response)
			throws IOException, DocumentException {
		PDDocument document = PDDocument
				.load(response.getEntity().getContent());
		PDFText2HTML stripper = new PDFText2HTML("UTF-8");
		StringWriter writer = new StringWriter();
		stripper.setLineSeparator("<br/>");
		stripper.setStartPage(2);
		stripper.setEndPage(2);
		stripper.writeText(document, writer);
		document.close();
		return parseHTML(new String(toXhtml(writer.toString().getBytes())));
	}

	private static String readCreateDocumentResponseBody(HttpResponse response)
			throws IOException, DocumentException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(response.getEntity().getContent());
		return out.toString("UTF-8");
	}

	private static byte[] toXhtml(byte[] document) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		newTidy().parseDOM(new ByteArrayInputStream(document), out);
		return out.toByteArray();
	}

	private static Tidy newTidy() {
		Tidy tidy = new Tidy();
		tidy.setTidyMark(false);
		tidy.setDocType("omit");
		tidy.setXHTML(true);
		tidy.setCharEncoding(Configuration.UTF8);
		tidy.setQuiet(true);
		return tidy;
	}

	private static List<String[]> parseHTML(String xml)
			throws DocumentException {
		SAXReader reader = new SAXReader();
		List<String[]> labels = new ArrayList<String[]>();
		Document document = reader.read(new StringReader(xml));
		for (Object object : document.selectNodes("//div/p")) {
			Node p = (Node) object;
			labels.add(p.getText().split("\n"));
		}
		return labels;
	}

	private final static String KIRJE_TEMPLATE = "/jalkiohjauskirje.html";
	private final static String LIITE_TEMPLATE = "/liite.html";

	private static List<String[]> callGenerateJalkiohjauskirje(List<Jalkiohjauskirje> letters)
			throws UnsupportedEncodingException, IOException,
			JsonGenerationException, JsonMappingException,
			ClientProtocolException, DocumentException {
		JalkiohjauskirjeBatch batch = new JalkiohjauskirjeBatch(KIRJE_TEMPLATE, LIITE_TEMPLATE, letters);
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");
		HttpPost post = new HttpPost(
				"http://localhost:8080/api/v1/jalkiohjauskirje/pdf");
		post.setHeader("Content-Type", "application/json;charset=utf-8");
		post.setEntity(new StringEntity(new ObjectMapper()
				.writeValueAsString(batch), ContentType.APPLICATION_JSON));
		HttpResponse response = client.execute(post);
		String documentId = readCreateDocumentResponseBody(response);
		HttpGet get = new HttpGet(
				"http://localhost:8080/api/v1/download/document/"
						+ documentId);
		response = client.execute(get);
		return readDownloadResponseBody(response);
	}

	private static List<List<String>> findHakutoiveTaulukko(List<String[]> pdf) {
		int firstFieldIndex = Iterables.indexOf(pdf, new Predicate<String[]>() {
			public boolean apply(String[] element) {
				return element.length == 2 && element[0].equals("Hylkäyksen") && element[1].equals("syy");
			}
		}) + 1;
		int lastFieldIndex = Iterables.indexOf(pdf, new Predicate<String[]>() {
			public boolean apply(String[] element) {
				return element[0].startsWith("HYLKÄYKSEN SYY:");
			}
		}) - 1;
		List<List<String>> toiveet = new ArrayList<List<String>>();
		int i = firstFieldIndex;
		while (i < lastFieldIndex) {
			List<String> haku = new ArrayList<String>();
			toiveet.add(haku);
			haku.add(pdf.get(i++)[0]);
			haku.add(pdf.get(i)[0]);
			haku.addAll(Arrays.asList(pdf.get(i)[1].split(" ")));
			i++;
		}
		return toiveet;
	}
	
	private static Map<String, String> createHaku(String koulu,
			String hakutoive, String ensisijaiset, String kaikki, String aloituspaikat,
			String varasija, String alinHyvaksytty, String omatPisteet, String koeTulos, String syy) {
		Map<String, String> toive = new HashMap<String, String>();
		toive.put("koulu", koulu);
		toive.put("hakutoive", hakutoive);
		toive.put("ensisijaisetHakijat", ensisijaiset);
		toive.put("kaikkiHakijat", kaikki);
		toive.put("aloituspaikat", aloituspaikat);
		toive.put("varasija", varasija);
		toive.put("alinHyvaksytty", alinHyvaksytty);
		toive.put("omatPisteesi", omatPisteet);
		toive.put("paasyJaSoveltuvuusKoe", koeTulos);
		toive.put("hylkayksenSyy", syy);
		return toive;
	}
}
