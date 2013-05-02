package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * curl -H "Content-Type: application/json" -X POST -d '["Ville Peurala", "Iina
 * Sipilä"]' -i http://localhost:8080/spike
 */
public class SpikeTest {
	@BeforeClass
	public static void setUp() throws Exception {
		Launcher.start();
	}

	@Test
	public void restApiWorks() throws Exception {
		new File("target/documents/Pekka.pdf").delete();
		new File("target/documents/Jussi.pdf").delete();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://localhost:8080/api/v1/spike");
		httpPost.setHeader("Content-Type", "application/json");
		// TODO vpeurala 30.4.2013: special characters don't work, encoding
		// problem somewhere
		httpPost.setEntity(new StringEntity("['Pekka', 'Jussi']"));
		HttpResponse response = httpClient.execute(httpPost);
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertEquals("{\"status\":\"ok\"}", readResponseBody(response));
		assertTrue(new File("target/documents/Pekka.pdf").exists());
		assertTrue(new File("target/documents/Jussi.pdf").exists());
	}

	@Test
	public void staticResourcesWork() throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://localhost:8080/index.html");
		HttpResponse response = httpClient.execute(httpGet);
		assertEquals(200, response.getStatusLine().getStatusCode());
	}

	private String readResponseBody(HttpResponse response) throws IOException {
		InputStream stream = response.getEntity().getContent();
		StringBuilder stringBuilder = new StringBuilder();
		int c = -1;
		while ((c = stream.read()) != -1) {
			stringBuilder.append((char) c);
		}
		return stringBuilder.toString();
	}
}
