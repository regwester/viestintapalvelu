package fi.vm.sade.viestintapalvelu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

/*
 * curl -H "Content-Type: application/json" -X POST -d '["Ville Peurala", "Iina
 * Sipilä"]' http://localhost:8080/spike
 */
public class SpikeTest {
	@Test
	public void test() throws Exception {
		new PDFService();
		new File("target/documents/Pekka.pdf").delete();
		new File("target/documents/Jussi.pdf").delete();
		Launcher.start();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://localhost:8080/spike");
		httpPost.setHeader("Content-Type", "application/json");
		// TODO vpeurala 30.4.2013: special characters don't work, encoding
		// problem somewhere
		httpPost.setEntity(new StringEntity("['Pekka', 'Jussi']"));
		System.out.println("Now sending request from client");
		HttpResponse response = httpClient.execute(httpPost);
		System.out.println(response);
		System.out.println(Arrays.asList(response.getAllHeaders()));
		assertEquals(200, response.getStatusLine().getStatusCode());
		assertTrue(new File("target/documents/Pekka.pdf").exists());
		assertTrue(new File("target/documents/Jussi.pdf").exists());
	}
}
