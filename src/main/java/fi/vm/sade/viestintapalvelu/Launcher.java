package fi.vm.sade.viestintapalvelu;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class Launcher {
	private static final String SERVLET_NAME = "viestintapalvelu";
	private static final int DEFAULT_PORT = 8080;

	public static void main(String[] args) throws Exception {
		Tomcat tomcat = start();
		tomcat.getServer().await();
	}

	public static Tomcat start() throws LifecycleException, ServletException {
		Tomcat tomcat = new Tomcat();
		tomcat.setBaseDir(tempDir());
		tomcat.setPort(DEFAULT_PORT);

		StandardContext ctx = (StandardContext) tomcat.addContext("/",
				tempDir());

		Injector injector = Guice.createInjector(new ViestintapalveluModule());

		Tomcat.addServlet(ctx, SERVLET_NAME,
				injector.getInstance(GuiceContainer.class));
		ctx.addServletMapping("/*", SERVLET_NAME);

		tomcat.start();
		return tomcat;
	}

	private static String tempDir() {
		return System.getProperty("java.io.tmpdir");
	}
}
