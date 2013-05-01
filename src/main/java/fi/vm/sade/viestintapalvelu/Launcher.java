package fi.vm.sade.viestintapalvelu;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		System.setProperty("org.apache.catalina.STRICT_SERVLET_COMPLIANCE",
				"true");
		Tomcat tomcat = new Tomcat();
		tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
		tomcat.setPort(DEFAULT_PORT);

		StandardContext ctx = (StandardContext) tomcat.addContext("/",
				System.getProperty("java.io.tmpdir"));

		Injector injector = Guice.createInjector(new ViestintapalveluModule());

		// GuiceFilter f = new GuiceFilter() {
		// @Override
		// public void doFilter(ServletRequest servletRequest,
		// ServletResponse servletResponse, FilterChain filterChain)
		// throws IOException, ServletException {
		// System.out.println("GuiceFilter.doFilter");
		// super.doFilter(servletRequest, servletResponse, filterChain);
		// }
		// };
		//
		// FilterDef filterDef = new FilterDef();
		// filterDef.setFilterName("guiceFilter");
		// filterDef.setFilter(f);
		// ctx.addFilterDef(filterDef);
		// FilterMap filterMap = new FilterMap();
		// filterMap.setFilterName("guiceFilter");
		// filterMap.addURLPattern("/*");
		// ctx.addFilterMap(filterMap);

		// ViestintapalveluApplication application = new
		// ViestintapalveluApplication();

		// ServletContainer container = new ServletContainer();
		// tomcat.addServlet(ctx.getPath(), SERVLET_NAME, container);
		// ctx.addServletMapping("/*", SERVLET_NAME);
		Tomcat.addServlet(ctx, "default", new HttpServlet() {
			@Override
			protected void service(HttpServletRequest req,
					HttpServletResponse resp) throws ServletException,
					IOException {
				System.out.println("default servlet service");
				super.service(req, resp);
			}
		});
		ctx.addServletMapping("/default", "default");

		Tomcat.addServlet(ctx, "guice",
				injector.getInstance(GuiceContainer.class));
		ctx.addServletMapping("/*", "guice");

		tomcat.start();
		return tomcat;
	}
}
