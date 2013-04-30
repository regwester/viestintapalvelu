package fi.vm.sade.viestintapalvelu;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import com.sun.jersey.api.core.ApplicationAdapter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Launcher {
	public static void main(String[] args) throws Exception {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);

		Context ctx = tomcat.addContext("/",
				System.getProperty("java.io.tmpdir"));

		ViestintapalveluApplication application = new ViestintapalveluApplication();

		ApplicationAdapter adapter = new ApplicationAdapter(application);
		adapter.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);

		ServletContainer container = new ServletContainer(application);
		Tomcat.addServlet(ctx, "viestintapalvelu", container);
		ctx.addServletMapping("/*", "viestintapalvelu");

		tomcat.start();
		tomcat.getServer().await();
	}
}
