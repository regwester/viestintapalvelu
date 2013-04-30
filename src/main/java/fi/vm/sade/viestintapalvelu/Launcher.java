package fi.vm.sade.viestintapalvelu;

import java.io.File;
import org.apache.catalina.startup.Tomcat;

public class Launcher {
    public static void main(String[] args) throws Exception {
        String webappDirLocation = "src/main/webapp";
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(8080);
        tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        tomcat.start();
        tomcat.getServer().await();
    }
}

