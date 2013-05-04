package fi.vm.sade.viestintapalvelu;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.catalina.startup.Tomcat;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class AddressLabelCSVTest {
		
	@BeforeClass
	public static void setUp() throws Exception {
		Launcher.start();
	}

	public static class WhenRequestingCSVLabelsForTwoStudents {

		private static String responseBody;

		@BeforeClass
		public static void setUp() throws Exception {
			Launcher.start();
			String json = new Scanner(AddressLabelCSVTest.class.getResourceAsStream(
					"/addresslabel_csv.json"), "UTF-8").useDelimiter("\u001a")
					.next();
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(
					"http://localhost:8080/api/v1/addresslabel");
			post.setHeader("Content-Type", "application/json");
			post.setEntity(new StringEntity(json));
			HttpResponse response = client.execute(post);
			responseBody = readResponseBody(response);
		}

		@Test
		public void responseContainsTwoRows() throws Exception {
			Assert.assertEquals(2, responseBody.split("\n").length);
		}
	}

	private static String readResponseBody(HttpResponse response) throws IOException {
		InputStream stream = response.getEntity().getContent();
		StringBuilder stringBuilder = new StringBuilder();
		int c = -1;
		while ((c = stream.read()) != -1) {
			stringBuilder.append((char) c);
		}
		return stringBuilder.toString();
	}
}
