package fi.vm.sade.viestintapalvelu.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;

import fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje.Jalkiohjauskirje;

public class ViestintapalveluObjectMapper extends ObjectMapper {
	public ViestintapalveluObjectMapper() {
		super();
		MrBeanModule mrBean = new MrBeanModule();
		registerModule(mrBean);
		addMixInAnnotations(Jalkiohjauskirje.class, JalkiohjauskirjeMixin.class);
	}
}