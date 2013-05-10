package fi.vm.sade.viestintapalvelu;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.jersey.api.json.JSONConfiguration;
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

		File staticResources = new File("src/main/webapp");
		tomcat.addWebapp("/", staticResources.getAbsolutePath());
		Context apiCtx = tomcat.addContext("/api/v1", tempDir());
		
		Injector injector = Guice.createInjector(new ViestintapalveluModule());
		GuiceContainer container = injector.getInstance(GuiceContainer.class);
		Wrapper wrapper = Tomcat.addServlet(apiCtx, "api", container);
		wrapper.addInitParameter(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
		apiCtx.addServletMapping("/*", "api");

		tomcat.start();
		return tomcat;
	}

	private static String tempDir() {
		return System.getProperty("java.io.tmpdir");
	}
}
