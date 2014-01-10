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

        Wrapper w = Tomcat.addServlet(staticCtx, jerseyServletName,
                org.glassfish.jersey.servlet.ServletContainer.class.getName());
        w.addInitParameter("javax.ws.rs.Application", "fi.vm.sade.viestintapalvelu.ViestintapaveluConfiguration");
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
