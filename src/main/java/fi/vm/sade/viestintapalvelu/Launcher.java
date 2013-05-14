package fi.vm.sade.viestintapalvelu;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.deploy.FilterDef;
import org.apache.catalina.deploy.FilterMap;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;

import com.google.inject.servlet.GuiceFilter;

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
		StandardContext staticCtx = (StandardContext) tomcat.addContext("/",
				staticResources.getAbsolutePath());
		staticCtx.setDefaultWebXml(tomcat.noDefaultWebXmlPath());
		staticCtx.setCachingAllowed(false);
		Tomcat.addServlet(staticCtx, "defaultServlet", new DefaultServlet());

		Tomcat.initWebappDefaults(staticCtx);

		StandardContext apiCtx = (StandardContext) tomcat.addContext("/api/v1",
				tempDir());

		apiCtx.addApplicationLifecycleListener(new ViestintapalveluGuiceServletContextListener());
		apiCtx.setDefaultWebXml(tomcat.noDefaultWebXmlPath());

		FilterDef filterDef = new FilterDef();
		filterDef.setFilterName("guiceFilter");
		filterDef.setFilterClass(GuiceFilter.class.getName());

		FilterMap filterMap = new FilterMap();
		filterMap.setFilterName("guiceFilter");
		filterMap.addURLPattern("*");

		apiCtx.addFilterDef(filterDef);
		apiCtx.addFilterMap(filterMap);

		Tomcat.initWebappDefaults(apiCtx);

		tomcat.start();
		return tomcat;
	}

	private static String tempDir() {
		return System.getProperty("java.io.tmpdir");
	}
}
