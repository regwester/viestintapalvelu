package fi.vm.sade.viestintapalvelu;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Launcher {
	private static final String SERVLET_NAME = "viestintapalvelu";
	private static final int DEFAULT_PORT = 8080;

	public static void main(String[] args) throws Exception {
		Tomcat tomcat = start();
		tomcat.getServer().await();
	}

	public static Tomcat start() throws LifecycleException {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(DEFAULT_PORT);

		Context ctx = tomcat.addContext("/",
				System.getProperty("java.io.tmpdir"));

		ViestintapalveluApplication application = new ViestintapalveluApplication();

		ServletContainer container = new ServletContainer(application);
		tomcat.addServlet(ctx.getPath(), SERVLET_NAME, container);
		ctx.addServletMapping("/*", SERVLET_NAME);

		tomcat.start();
		return tomcat;
	}
}
