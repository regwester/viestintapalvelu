package fi.vm.sade.viestintapalvelu.domain.jalkiohjauskirje;

import com.google.inject.Inject;

import fi.vm.sade.viestintapalvelu.infrastructure.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.infrastructure.LiiteBuilder;
import fi.vm.sade.viestintapalvelu.infrastructure.PdfBuilder;

public class JalkiohjauskirjeBuilder extends PdfBuilder {
	@Inject
	public JalkiohjauskirjeBuilder(DocumentBuilder documentBuilder,
			LiiteBuilder liiteBuilder) {
		super(documentBuilder, liiteBuilder);
	}
}
