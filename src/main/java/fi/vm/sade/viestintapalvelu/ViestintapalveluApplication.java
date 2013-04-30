package fi.vm.sade.viestintapalvelu;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class ViestintapalveluApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(SpikeResource.class);
		return classes;
	}
}
