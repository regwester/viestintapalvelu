package fi.vm.sade.viestintapalvelu;

import fi.vm.sade.integrationtest.tomcat.EmbeddedTomcat;
import fi.vm.sade.integrationtest.util.ProjectRootFinder;
import org.apache.catalina.LifecycleException;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.ServletException;

public class ViestintapalveluTomcat {
    static final String MODULE_ROOT = ProjectRootFinder.findProjectRoot() + "/viestintapalvelu-service";
    static final String CONTEXT_PATH = "/viestintapalvelu-service";
    static final int DEFAULT_PORT = 9091;
    static final int DEFAULT_AJP_PORT = 8507;
    private static EmbeddedTomcat tomcat = null;

    public static void main(String... args) throws ServletException, LifecycleException {
        create(Integer.parseInt(System.getProperty("viestintapalvelu-app.port", String.valueOf(DEFAULT_PORT))),
                Integer.parseInt(System.getProperty("viestintapalvelu-app.port.ajp", String.valueOf(DEFAULT_AJP_PORT)))
        ).start().await();
    }

    public static EmbeddedTomcat create(int port, int ajpPort) {
        useIntegrationTestSettingsIfNoProfileSelected();
        return new EmbeddedTomcat(port, ajpPort, MODULE_ROOT, CONTEXT_PATH);
    }

    synchronized public static void startForIntegrationTestIfNotRunning() {
        if(tomcat == null) {
            tomcat = create(DEFAULT_PORT, DEFAULT_AJP_PORT);
            tomcat.start();
        }
    }

    private static void useIntegrationTestSettingsIfNoProfileSelected() {
        //System.setProperty("application.system.cache", "false");
        if (System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", "it");
        }
        System.out.println("Running embedded with profile " + System.getProperty("spring.profiles.active"));
    }
}