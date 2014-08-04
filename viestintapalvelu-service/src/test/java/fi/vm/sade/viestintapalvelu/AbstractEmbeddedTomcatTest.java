package fi.vm.sade.viestintapalvelu;


import org.apache.catalina.LifecycleState;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/** NOTE! This class is not in use at the moment. */

/* Prepares the tomcat server with jersey servlet to do integration testing.*/
public abstract class AbstractEmbeddedTomcatTest {
    /** Tomcat instance */
    private Tomcat tomcat;
    /** Temporary working directory for tomcat and webapps */
    private String tmpDir = System.getProperty("java.io.tmpdir");
    /** The class logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEmbeddedTomcatTest.class);

    /** Starts the Tomcat server. */
    @Before
    public final void setup() throws Throwable {
        LOGGER.info("Tomcat's base directory : {}", tmpDir);

        LOGGER.info("Creates a new server...");
        tomcat = new Tomcat();
        tomcat.setPort(0);
        tomcat.setBaseDir(tmpDir);
        tomcat.getHost().setAppBase(tmpDir);
        tomcat.getHost().setAutoDeploy(true);
        tomcat.getHost().setDeployOnStartup(true);

        LOGGER.info("Prepares and adds the web app");
        String contextPath = "/" + getApplicationId();
        File webApp = new File(tmpDir, getApplicationId());
        File oldWebApp = new File(webApp.getAbsolutePath());
        FileUtils.deleteDirectory(oldWebApp);
        //new ZipExporterImpl(createWebArchive()).exportTo(new File(tmpDir + "/" + getApplicationId() + ".war"), true);
        tomcat.addWebapp(tomcat.getHost(), contextPath, webApp.getAbsolutePath());


        LOGGER.info("Init users and roles");
        tomcat.addUser("admin", "admin");
        tomcat.addUser("user", "user");
        tomcat.addRole("admin", "admin");
        tomcat.addRole("admin", "user");
        tomcat.addRole("user", "user");

        LOGGER.info("Start the server...");
        tomcat.start();
    }

    /** Stops the Tomcat server. */
    @After
    public final void teardown() throws Throwable {
        LOGGER.info("Stop the server...");

        if (tomcat.getServer() != null && tomcat.getServer().getState() != LifecycleState.DESTROYED) {
            if (tomcat.getServer().getState() != LifecycleState.STOPPED) {
                tomcat.stop();
            }
            tomcat.destroy();
        }
    }

    /** Return the port tomcat is running on */
    protected int getTomcatPort() {
        return tomcat.getConnector().getLocalPort();
    }

    /** @return a web archive that will be deployed on the embedded tomcat. */
    //protected abstract WebArchive createWebArchive();

    /** @return the name of the application to test. */
    protected abstract String getApplicationId();
}
