package fi.vm.sade.viestintapalvelu.server;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.vm.sade.viestintapalvelu.letter.LetterResource;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Extend this class for embedded server support to your test class
 * @author Risto Salama
 *
 */
public class ServerIntegrationTest {

    private Tomcat tomcat;
    private final static String WORKING_DIRECTORY = System.getProperty("java.io.tmpdir");
    private final static String SERVICE_NAME = "viestintapalvelu-service"; 
    private final static String HOST_NAME = "localhost";
    private final static Integer PORT = 9096;
    private final static String HOST_WITH_PORT = "http://" + HOST_NAME + ":" + PORT;
    
    @Before
    public void startServer() throws Exception {
        createWar();
        String contextPath = "/" + SERVICE_NAME;
        tomcat = new Tomcat();
        tomcat.addWebapp(tomcat.getHost(), contextPath, WORKING_DIRECTORY + "/" + SERVICE_NAME);   
        tomcat.setPort(PORT);
        tomcat.setBaseDir(WORKING_DIRECTORY);
        tomcat.setHostname(HOST_NAME);
        tomcat.getHost().setAppBase(WORKING_DIRECTORY);
        tomcat.getHost().setAutoDeploy(true);
        tomcat.getHost().setDeployOnStartup(true);
        tomcat.init();
        tomcat.start();
        Thread.sleep(500);
    }
    
    @Test
    public void quickTest() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(HOST_WITH_PORT).path("/" + SERVICE_NAME + "/api/v1/letter/async/letter/status/1");
        Response resp = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertNotNull(resp);
    }
    
    @Test
    public void anotherTest() throws Exception {
        WebTarget target = ClientBuilder.newClient().target(HOST_WITH_PORT).path("/" + SERVICE_NAME + "/api/v1/template/store");
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(DocumentProviderTestData.getTemplate());
        Response resp = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(s));
        assertEquals(200, resp.getStatus());
    }
    
    @After
    public void tearDown() throws Exception {
        Server server = tomcat.getServer();
        if (server != null && server.getState() != LifecycleState.DESTROYED) {
            if (server.getState() != LifecycleState.STOPPED) {
                tomcat.stop();
            }
            tomcat.destroy();
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
    
    protected void start() throws Exception {
        tomcat.start();
    }
    
    protected void stop() throws Exception {
        tomcat.stop();
    }
    
    protected void restart() throws Exception {
        stop();
        start();
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

