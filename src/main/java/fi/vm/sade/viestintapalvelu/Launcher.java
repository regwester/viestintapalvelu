package fi.vm.sade.viestintapalvelu;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class Launcher {
	private static final int DEFAULT_PORT = 8080;

	public static void main(String[] args) throws Exception {
		Tomcat tomcat = start();
		tomcat.getServer().await();
	}

	public static Tomcat start() throws LifecycleException, ServletException {
		Tomcat tomcat = new Tomcat();
		tomcat.setBaseDir(tempDir());
		tomcat.setPort(DEFAULT_PORT);

		File staticResources = new File("src/main/resources");

		Context rootCtx = tomcat.addContext("/",
				staticResources.getAbsolutePath());
		Context apiCtx = tomcat.addContext("/api/v1", tempDir());

		Injector injector = Guice.createInjector(new ViestintapalveluModule());

		Tomcat.addServlet(apiCtx, "api",
				injector.getInstance(GuiceContainer.class));
		apiCtx.addServletMapping("/*", "api");

		Tomcat.addServlet(rootCtx, "default", DefaultServlet.class.getName());
		rootCtx.addServletMapping("/*", "default");

		tomcat.start();
		return tomcat;
	}

	private static String tempDir() {
		return System.getProperty("java.io.tmpdir");
	}
}
