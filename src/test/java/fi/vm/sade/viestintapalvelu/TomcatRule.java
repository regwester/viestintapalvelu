package fi.vm.sade.viestintapalvelu;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.rules.ExternalResource;

public class TomcatRule extends ExternalResource {
	private Tomcat tomcat;

	@Override
	protected void before() throws Exception {
		tomcat = Launcher.start();
	}

	@Override
	protected void after() {
		if (tomcat != null) {
			try {
				tomcat.destroy();
			} catch (LifecycleException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
