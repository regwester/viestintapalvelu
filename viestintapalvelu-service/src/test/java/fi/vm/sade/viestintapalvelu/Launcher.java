package fi.vm.sade.viestintapalvelu;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

public class Launcher {
    public static final int DEFAULT_PORT = PortFinder.findFreePort();

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = start();
        tomcat.getServer().await();
    }

    public static Tomcat start() throws LifecycleException, ServletException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(tempDir());
        tomcat.setPort(DEFAULT_PORT);

        File staticResources = new File("src/main/webapp");
        String jerseyServletName = "Jersey REST Service";
        StandardContext staticCtx = (StandardContext) tomcat.addContext("/", staticResources.getAbsolutePath());
        staticCtx.setDefaultWebXml(tomcat.noDefaultWebXmlPath());
        staticCtx.addParameter("contextConfigLocation", "classpath:test-application-context.xml");
        staticCtx.addApplicationListener("org.springframework.web.context.ContextLoaderListener");

        // <servlet>
        // <servlet-name>Jersey REST Service</servlet-name>
        // <servlet-class>com.sun.jersey.spi.spring.container.servlet.SpringServlet</servlet-class>
        // <init-param>
        // <param-name>com.sun.jersey.spi.container.ContainerResponseFilters</param-name>
        // <param-value>fi.vm.sade.generic.rest.CorsFilter</param-value>
        // </init-param>
        // <init-param>
        // <param-name>com.sun.jersey.api.json.POJOMappingFeature</param-name>
        // <param-value>true</param-value>
        // </init-param>
        // <load-on-startup>1</load-on-startup>
        // </servlet>
        // <servlet-mapping>
        // <servlet-name>Jersey REST Service</servlet-name>
        // <url-pattern>/api/v1/*</url-pattern>
        // </servlet-mapping>

        Wrapper w = Tomcat.addServlet(staticCtx, jerseyServletName,
                com.sun.jersey.spi.spring.container.servlet.SpringServlet.class.getName());
        w.addInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters",
                "fi.vm.sade.generic.rest.CorsFilter");
        w.addInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        staticCtx.addServletMapping("/api/v1/*", jerseyServletName);
        Tomcat.initWebappDefaults(staticCtx);
        staticCtx.setCachingAllowed(false);

        tomcat.start();

        return tomcat;
    }

    private static String tempDir() {
        return System.getProperty("java.io.tmpdir");
    }
}
