package fi.vm.sade.viestintapalvelu;

//
//import com.google.inject.servlet.GuiceFilter;
//import org.apache.catalina.LifecycleException;
//import org.apache.catalina.core.StandardContext;
//import org.apache.catalina.deploy.FilterDef;
//import org.apache.catalina.deploy.FilterMap;
//import org.apache.catalina.startup.Tomcat;
//
//import javax.servlet.ServletException;
//import java.io.File;
//
//public class Launcher {
//    public static final int DEFAULT_PORT = 8080;
//
//    public static void main(String[] args) throws Exception {
//        Tomcat tomcat = start();
//        tomcat.getServer().await();
//    }
//
//    public static Tomcat start() throws LifecycleException, ServletException {
//        Tomcat tomcat = new Tomcat();
//        tomcat.setBaseDir(tempDir());
//        tomcat.setPort(DEFAULT_PORT);
//
//        File staticResources = new File("src/main/webapp");
//        StandardContext staticCtx = (StandardContext) tomcat.addContext("/",
//                staticResources.getAbsolutePath());
//        staticCtx.setDefaultWebXml(tomcat.noDefaultWebXmlPath());
//
//        staticCtx
//                .addApplicationLifecycleListener(new ViestintapalveluGuiceServletContextListener());
//
//        FilterDef filterDef = new FilterDef();
//        filterDef.setFilterName("guiceFilter");
//        filterDef.setFilterClass(GuiceFilter.class.getName());
//
//        FilterMap filterMap = new FilterMap();
//        filterMap.setFilterName("guiceFilter");
//        filterMap.addURLPattern("/" + Urls.API_PATH + "/*");
//
//        staticCtx.addFilterDef(filterDef);
//        staticCtx.addFilterMap(filterMap);
//        Tomcat.initWebappDefaults(staticCtx);
//        staticCtx.setCachingAllowed(false);
//
//        tomcat.start();
//        return tomcat;
//    }
//
//    private static String tempDir() {
//        return System.getProperty("java.io.tmpdir");
//    }
// }
