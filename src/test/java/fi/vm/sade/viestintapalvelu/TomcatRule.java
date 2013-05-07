package fi.vm.sade.viestintapalvelu;

import org.apache.catalina.startup.Tomcat;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.rules.ExternalResource;

public class TomcatRule extends ExternalResource {
	private static Tomcat globalTomcat;

	@Override
	protected void before() throws Exception {
		if (globalTomcat == null) {
			globalTomcat = Launcher.start();
			// Do one REST api call to initialize Jersey & Guice before running
			// other tests
			DefaultHttpClient client = new DefaultHttpClient();
			client.getParams().setParameter("http.protocol.content-charset",
					"UTF-8");
			HttpPost post = new HttpPost(
					"http://localhost:8080/api/v1/addresslabel/download");
			post.setHeader("Content-Type", "application/json;charset=utf-8");
			client.execute(post);
		}
	}

	@Override
	protected void after() {
		// Do nothing, let Tomcat run until the JVM dies.
	}
}
