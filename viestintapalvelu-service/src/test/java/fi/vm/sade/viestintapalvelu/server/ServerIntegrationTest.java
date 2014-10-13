package fi.vm.sade.viestintapalvelu.server;

import java.io.File;
import java.io.IOException;

import org.apache.catalina.LifecycleState;
import org.apache.catalina.Server;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.exporter.ExplodedExporterImpl;
import org.jboss.shrinkwrap.impl.base.exporter.zip.ZipExporterImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import fi.vm.sade.viestintapalvelu.letter.LetterResource;

/**
 * Extend this class for embedded server support to your test class
 * @author risal1
 *
 */
//@WebAppConfiguration
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(value = "classpath:test-application-context.xml")
public class ServerIntegrationTest {

    private static Tomcat tomcat;
    private String workingDir = System.getProperty("java.io.tmpdir");
    private static final String WEBAPP_SRC = "src/main/webapp";
    //.addAsWebInfResource("src/test/resources/test-application.context.xml")
    @Before
    public void startServer() throws Exception {
        File webApp = createWar();
        String contextPath = "/viestintapalvelu-service";
        tomcat = new Tomcat();
        tomcat.addWebapp(tomcat.getHost(), contextPath, workingDir + "/viestintapalvelu-service");   
        tomcat.setPort(9096);
        tomcat.setBaseDir(workingDir);
        tomcat.setHostname("localhost");
        tomcat.getHost().setAppBase(workingDir);
        tomcat.getHost().setAutoDeploy(true);
        tomcat.getHost().setDeployOnStartup(true);
//        Context ctx = server.addWebapp("", "src/main/webapp");
//        ctx.setConfigFile(new File("src/main/webapp/WEB-INF/web.xml").toURI().toURL());
        tomcat.init();
        tomcat.start();
        Thread.sleep(500);
    }

    
    @Test
    public void quickTest() {
        //assertTrue(server.);
    }
    
//    @Test
//    public void anotherTest() {
//    }
    
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
    
    @AfterClass
    public static void stopServer() throws Exception {
        tomcat.stop();
    }
    
    protected void start() throws Exception {
        tomcat.start();
    }
    
    protected void stop() throws Exception {
        tomcat.stop();
    }
    
    protected void restart() throws Exception {
        start();
        stop();
    }
    
    private File createWar() throws ClassNotFoundException, IOException {
        this.getClass().getClassLoader().loadClass(LetterResource.class.getName());
        File contextFile = new File("src/test/resources/test-application-context.xml");
        WebArchive war = ShrinkWrap.create(WebArchive.class, "viestintapalvelu-service")
                .setWebXML(new File("src/test/resources/web.xml"))
                .addAsResource(contextFile, "spring/application-context.xml")
                .addPackages(true, java.lang.Package.getPackage("fi.vm.sade.viestintapalvelu"));
        File webApp = new File(workingDir, "viestintapalvelu-service");
        File oldWebApp = new File(webApp.getAbsolutePath());
        try {
            FileUtils.deleteDirectory(oldWebApp);
        } catch(IOException e){}
        new ExplodedExporterImpl(war).exportExploded(new File(workingDir));
        return webApp;
    }
}

