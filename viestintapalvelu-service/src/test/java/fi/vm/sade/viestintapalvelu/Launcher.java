package fi.vm.sade.viestintapalvelu;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

public class Launcher {
    public static final int DEFAULT_PORT = PortFinder.findFreePort();
    //public static ApplicationResourceConfig applicationResourceConfig = new ApplicationResourceConfig();

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = start();
        tomcat.getServer().await();
    }

    public static Tomcat start() throws LifecycleException, ServletException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(tempDir());
        tomcat.setPort(DEFAULT_PORT);

        File staticResources = new File("src/main/webapp");
        StandardContext context = (StandardContext) tomcat.addContext("/", staticResources.getAbsolutePath());
        context.setDefaultWebXml(tomcat.noDefaultWebXmlPath());
        context.addParameter("contextConfigLocation", "classpath:test-application-context.xml");
        context.addApplicationListener("org.springframework.web.context.ContextLoaderListener");

        Tomcat.addServlet(context, "jersey-servlet", ServletContainer.class.getName());
        context.addServletMapping("/api/v1/*", "jersey-servlet");

        Tomcat.initWebappDefaults(context);
        context.setCachingAllowed(false);

        tomcat.start();

        return tomcat;
    }

    private static String tempDir() {
        return System.getProperty("java.io.tmpdir");
    }
}
