package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.ClassRule;
import org.junit.Test;

/*
 * curl -H "Content-Type: application/json" -X POST -d '["Ville Peurala", "Iina
 * Sipilä"]' -i http://localhost:8080/spike
 */
public class IntegrationTest {
	@ClassRule
	public static TomcatRule tomcat = new TomcatRule();

	@Test
	public void staticResourcesWork() throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://localhost:8080/index.html");
		HttpResponse response = httpClient.execute(httpGet);
		assertEquals(200, response.getStatusLine().getStatusCode());
	}

	@Test
	public void addressLabelPrinting() throws Exception {
		String json = new Scanner(getClass().getResourceAsStream(
				"/addresslabel_pdf.json"), "UTF-8").useDelimiter("\u001a")
				.next();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://localhost:8080/api/v1/addresslabel/pdf");
		post.setHeader("Content-Type", "application/json");
		post.setEntity(new StringEntity(json));
		HttpResponse response = client.execute(post);
		String documentId = readResponseBody(response);
		HttpGet get = new HttpGet(
				"http://localhost:8080/api/v1/download/document/"
						+ documentId);
		response = client.execute(get);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("Content-Type: application/pdf;charset=utf-8", response
				.getFirstHeader("Content-Type").toString());
		assertEquals(
				"Content-Disposition: attachment; filename=\"addresslabels.pdf\"",
				response.getFirstHeader("Content-Disposition").toString());
	}

	@Test
	public void addressLabelXLSPrinting() throws Exception {
		String json = new Scanner(getClass().getResourceAsStream(
				"/addresslabel_xls.json"), "UTF-8").useDelimiter("\u001a")
				.next();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://localhost:8080/api/v1/addresslabel/xls");
		post.setHeader("Content-Type", "application/json");
		post.setEntity(new StringEntity(json));
		HttpResponse response = client.execute(post);
		String documentId = readResponseBody(response);
		HttpGet get = new HttpGet(
				"http://localhost:8080/api/v1/download/document/"
						+ documentId);
		response = client.execute(get);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("Content-Type: application/vnd.ms-excel", response
				.getFirstHeader("Content-Type").toString());
		assertEquals(
				"Content-Disposition: attachment; filename=\"addresslabels.xls\"",
				response.getFirstHeader("Content-Disposition").toString());
	}

	@Test
	public void jalkiohjauskirjePDFPrinting() throws Exception {
		String json = new Scanner(getClass().getResourceAsStream(
				"/jalkiohjauskirje_pdf.json"), "UTF-8").useDelimiter("\u001a")
				.next();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://localhost:8080/api/v1/jalkiohjauskirje/pdf");
		post.setHeader("Content-Type", "application/json");
		post.setEntity(new StringEntity(json));
		HttpResponse response = client.execute(post);
		String documentId = readResponseBody(response);
		HttpGet get = new HttpGet(
				"http://localhost:8080/api/v1/download/document/"
						+ documentId);
		response = client.execute(get);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("Content-Type: application/pdf;charset=utf-8", response
				.getFirstHeader("Content-Type").toString());
		assertEquals(
				"Content-Disposition: attachment; filename=\"jalkiohjauskirje.pdf\"",
				response.getFirstHeader("Content-Disposition").toString());
	}

	private String readResponseBody(HttpResponse response) throws IOException {
		return IOUtils.toString(response.getEntity().getContent());
	}
}
