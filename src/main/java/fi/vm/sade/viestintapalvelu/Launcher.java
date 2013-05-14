package fi.vm.sade.viestintapalvelu;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.deploy.FilterDef;
import org.apache.catalina.deploy.FilterMap;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Constants;
import org.apache.catalina.startup.ContextConfig;
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
		ContextConfig staticCtxConfig = new ContextConfig();
		ContextConfig apiCtxConfig = new ContextConfig();
		staticCtxConfig.setDefaultWebXml(Constants.NoDefaultWebXml);
		apiCtxConfig.setDefaultWebXml(Constants.NoDefaultWebXml);

		// staticCtx.addLifecycleListener(staticCtxConfig);

		Tomcat.initWebappDefaults(staticCtx);

		StandardContext apiCtx = (StandardContext) tomcat.addContext("/api/v1",
				tempDir());

		apiCtx.addApplicationLifecycleListener(new ViestintapalveluGuiceServletContextListener());
		apiCtx.setDefaultWebXml(tomcat.noDefaultWebXmlPath());
		apiCtx.addLifecycleListener(apiCtxConfig);

		FilterDef filterDef = new FilterDef();
		filterDef.setFilterName("guiceFilter");
		filterDef.setFilterClass(GuiceFilter.class.getName());

		FilterMap filterMap = new FilterMap();
		filterMap.setFilterName("guiceFilter");
		filterMap.addURLPattern("*");

		apiCtx.addFilterDef(filterDef);
		apiCtx.addFilterMap(filterMap);
		
		Tomcat.initWebappDefaults(apiCtx);

		// Injector injector = Guice.createInjector(new
		// ViestintapalveluModule());
		// GuiceContainer container =
		// injector.getInstance(GuiceContainer.class);
		// Wrapper wrapper = Tomcat.addServlet(apiCtx, "api", container);
		// FIXME vpeurala
		// wrapper.addInitParameter(JSONConfiguration.FEATURE_POJO_MAPPING,
		// "true");
		// apiCtx.addServletMapping("/*", "api");

		tomcat.start();
		return tomcat;
	}

	private static String tempDir() {
		return System.getProperty("java.io.tmpdir");
	}
}
