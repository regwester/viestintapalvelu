package fi.vm.sade.viestintapalvelu;

import org.apache.catalina.startup.Tomcat;
import org.junit.rules.ExternalResource;

public class TomcatRule extends ExternalResource {
	private static Tomcat globalTomcat;

	@Override
	protected void before() throws Exception {
		if (globalTomcat == null) {
			globalTomcat = Launcher.start();
		}
	}

	@Override
	protected void after() {
		// Do nothing, let Tomcat run until the JVM dies.
	}
}
