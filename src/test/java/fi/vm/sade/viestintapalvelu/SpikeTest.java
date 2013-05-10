package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.ClassRule;
import org.junit.Test;
import org.odftoolkit.odfdom.converter.core.utils.ByteArrayOutputStream;

/*
 * curl -H "Content-Type: application/json" -X POST -d '["Ville Peurala", "Iina
 * Sipilä"]' -i http://localhost:8080/spike
 */
public class SpikeTest {
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
				"/addresslabelbatch1.json"), "UTF-8").useDelimiter("\u001a")
				.next();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://localhost:8080/api/v1/addresslabel/createDocument");
		post.setHeader("Content-Type", "application/json");
		post.setEntity(new StringEntity(json));
		HttpResponse response = client.execute(post);
		String documentId = readResponseBody(response);
		HttpGet get = new HttpGet(
				"http://localhost:8080/api/v1/addresslabel/download/"
						+ documentId);
		response = client.execute(get);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("Content-Type: application/pdf;charset=utf-8", response
				.getFirstHeader("Content-Type").toString());
		assertEquals(
				"Content-Disposition: attachment; filename=\"addresslabels.pdf\"",
				response.getFirstHeader("Content-Disposition").toString());
		assertEquals("Content-Length: 1124",
				response.getFirstHeader("Content-Length").toString());
	}

	@Test
	public void addressLabelCSVPrinting() throws Exception {
		String json = new Scanner(getClass().getResourceAsStream(
				"/addresslabel_csv.json"), "UTF-8").useDelimiter("\u001a")
				.next();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://localhost:8080/api/v1/addresslabel/createDocument");
		post.setHeader("Content-Type", "application/json");
		post.setEntity(new StringEntity(json));
		HttpResponse response = client.execute(post);
		String documentId = readResponseBody(response);
		HttpGet get = new HttpGet(
				"http://localhost:8080/api/v1/addresslabel/download/"
						+ documentId);
		response = client.execute(get);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("Content-Type: text/csv;charset=utf-8", response
				.getFirstHeader("Content-Type").toString());
		assertEquals(
				"Content-Disposition: attachment; filename=\"addresslabels.csv\"",
				response.getFirstHeader("Content-Disposition").toString());
		assertEquals("Content-Length: 138",
				response.getFirstHeader("Content-Length").toString());
	}

	private String readResponseBody(HttpResponse response) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(response.getEntity().getContent());
		return out.toString();
	}
}
