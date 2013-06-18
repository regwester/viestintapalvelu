package fi.vm.sade.viestintapalvelu;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ViestintapalveluGuiceServletContextListener extends
        GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ViestintapalveluModule());
    }
}
