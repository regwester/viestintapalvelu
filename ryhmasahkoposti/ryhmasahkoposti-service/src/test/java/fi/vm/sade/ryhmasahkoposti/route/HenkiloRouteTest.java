package fi.vm.sade.ryhmasahkoposti.route;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.authentication.model.Henkilo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-bundle-context.xml")
public class HenkiloRouteTest {
	@Autowired
	private HenkiloRoute henkiloRoute;
	
	@Test
	public void testHaeHenkilo() {
		Henkilo henkilo = henkiloRoute.haeHenkilo("1.2.246.562.24.00000000001");
		
		assertNotNull(henkilo);
	}

}
