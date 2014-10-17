package fi.vm.sade.viestintapalvelu.server;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.catalina.LifecycleState;
import org.apache.catalina.Server;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.exporter.ExplodedExporterImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import fi.vm.sade.viestintapalvelu.letter.LetterResource;

/**
 * Extend this class for embedded server support to your test class
 * @author Risto Salama
 *
 */
public abstract class ServerIntegrationTest {

    private Tomcat tomcat;
    private final static String WORKING_DIRECTORY = System.getProperty("java.io.tmpdir");
    private final static String SERVICE_NAME = "viestintapalvelu-service"; 
    private final static String HOST_NAME = "localhost";
    private final static Integer PORT = 9096;
    private final static String HOST_WITH_PORT = "http://" + HOST_NAME + ":" + PORT;
    
    @Before
    public void startServer() throws Exception {
        createWar();
        tomcat = new Tomcat();
        tomcat.setBaseDir(WORKING_DIRECTORY);
        tomcat.addWebapp(tomcat.getHost(), "/" + SERVICE_NAME, WORKING_DIRECTORY + "/" + SERVICE_NAME);   
        tomcat.setPort(PORT);
        tomcat.setBaseDir(WORKING_DIRECTORY);
        tomcat.setHostname(HOST_NAME);
        tomcat.getHost().setAppBase(WORKING_DIRECTORY);
        tomcat.getHost().setAutoDeploy(true);
        tomcat.getHost().setDeployOnStartup(true);
        tomcat.init();
        start();
    }
    
    @After
    public void tearDown() throws Exception {
        Server server = tomcat.getServer();
        if (server != null && server.getState() != LifecycleState.DESTROYED) {
            if (server.getState() != LifecycleState.STOPPED) {
                tomcat.stop();
            }
            tomcat.destroy();
            while(!tomcat.getServer().getState().equals(LifecycleState.DESTROYED)) {
                Thread.sleep(500);
            }
        }
    }
    
    @BeforeClass
    public static void cleanBefore() {
        deleteDeployedWebApp();
    }
    
    @AfterClass
    public static void cleanAfter() {
        deleteDeployedWebApp();
    }
    
    protected final void start() throws Exception {
        tomcat.start();
        while (!tomcat.getServer().getState().equals(LifecycleState.STARTED)) {
            Thread.sleep(500);
        }
    }
    
    protected final void stop() throws Exception {
        tomcat.stop();
        while (!tomcat.getServer().getState().equals(LifecycleState.STOPPED)) {
            Thread.sleep(500);
        }
    }
    
    protected final void restart() throws Exception {
        stop();
        start();
    }
    
    protected WebTarget createWebTarget(String rest) {
        return ClientBuilder.newClient().target(HOST_WITH_PORT).path("/" + SERVICE_NAME + rest);
    }
    
    private void createWar() throws ClassNotFoundException, IOException {
        this.getClass().getClassLoader().loadClass(LetterResource.class.getName());
        File contextFile = new File("src/test/resources/test-application-context.xml");
        WebArchive war = ShrinkWrap.create(WebArchive.class, SERVICE_NAME)
                .setWebXML(new File("src/test/resources/web.xml"))
                .addAsResource(contextFile, "spring/application-context.xml")
                .addPackages(true, java.lang.Package.getPackage("fi.vm.sade.viestintapalvelu"));
        new ExplodedExporterImpl(war).exportExploded(new File(WORKING_DIRECTORY));
    }
    
    private static void deleteDeployedWebApp() {
        try {
            FileUtils.deleteDirectory(new File(WORKING_DIRECTORY, SERVICE_NAME));
        } catch(Exception e){}
        try {
            new File(WORKING_DIRECTORY, SERVICE_NAME + ".war").delete();
        } catch (Exception e) {}
    }
    
}

