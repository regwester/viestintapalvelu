package fi.vm.sade.viestintapalvelu;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.rules.ExternalResource;

import fi.vm.sade.viestintapalvelu.application.Urls;

public class TomcatRule extends ExternalResource {
	private static Tomcat globalTomcat;

	@Override
	protected void before() throws Exception {
		if (globalTomcat == null) {
			launchAndInitialize();
		}
	}

	private void launchAndInitialize() throws LifecycleException,
			ServletException, IOException, ClientProtocolException {
		try {
			globalTomcat = Launcher.start();
			// Do one REST api call to initialize Jersey & Guice 
			// before running other tests
			DefaultHttpClient client = new DefaultHttpClient();
			client.getParams().setParameter("http.protocol.content-charset",
					"UTF-8");
			HttpPost post = new HttpPost(Urls.localhost()
					.addresslabelDownload());
			post.setHeader("Content-Type", "application/json;charset=utf-8");
			client.execute(post);
		} catch (Exception e) {
			// We can safely ignore launcher failures at this point, since
			// this class is used only in tests and the tests themselves
			// will fail if launcher fails.
			// The most probable reason of launcher failures here is that
			// a server is already running on port 8080, which can
			// happen for instance if tests are run in parallel 
			// in separate JVMs.
		}
	}

	@Override
	protected void after() {
		// Do nothing, let Tomcat run until the JVM dies.
	}
}
